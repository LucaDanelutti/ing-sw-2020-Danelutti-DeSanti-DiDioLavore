package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.playerstate.ActionState;
import it.polimi.ingsw.model.playerstate.IdleState;
import it.polimi.ingsw.model.playerstate.PlayerState;
import it.polimi.ingsw.model.playerstate.PlayerStateType;
import org.graalvm.compiler.lir.sparc.SPARCMove;

import java.util.ArrayList;

public class GameLogicExecutor implements ActionObserver, ActionVisitor {
    private Game game;

    public GameLogicExecutor(Game game){
        this.game=game;
    }

    public void executeAction(ConstructAction constructAction){
        if(constructAction.getIsOptional()&&constructAction.getChosenPosition()==null){
            //this is an optional construct action to be skipped because the chosen position is not set!
            if(constructAction.getEnableMoveUp()){
                enableMoveUpForCurrentPlayer();
            }
            loadNextAction();
        }
        else {
            game.getBoard().pawnConstruct(constructAction.getChosenPosition(), constructAction.getSelectedBlockType());
        }
    }
    public void executeAction(MoveAction moveAction){
        Position oldPos =moveAction.getSelectedPawn().getPosition();
        Position newPos=moveAction.getChosenPosition();
        Pawn opponentPawn = game.getBoard().getMatrixCopy()[newPos.getX()][newPos.getY()].getPawn();

        if(opponentPawn==null){
            game.getBoard().updatePawnPosition(oldPos,newPos);
            if(moveAction.getDenyMoveUpEnable() && moveAction.getSelectedPawn().getDeltaHeight()>0)
                disableMoveUpOfOtherPlayers();
        }
        else if(moveAction.getSwapEnable()){
            game.getBoard().updatePawnPosition(moveAction.getSelectedPawn().getPosition(),moveAction.getChosenPosition());
            game.getBoard().setPawnPosition(opponentPawn,oldPos);
        }
        else if (moveAction.getPushEnable()){
            Position opponentPawnNewPos;
            int deltaX=newPos.getX()-oldPos.getX();
            int deltaY=newPos.getY()-oldPos.getY();
            opponentPawnNewPos=new Position(newPos.getX()+deltaX,newPos.getY()+deltaY);
            game.getBoard().updatePawnPosition(oldPos,newPos);
            game.getBoard().setPawnPosition(opponentPawn,opponentPawnNewPos);
        }


        //after a move action is executed always check if the payer won
        if(moveAction.checkWin(game.getBoard().getMatrixCopy())){
            //TODO: you have to set the playerState for all the players
        }
        else{
            //TODO: you have to switch to the next action or pass the turn to another player
        }
    }

    private void enableMoveUpForCurrentPlayer(){
        ArrayList<Player> actionStatePlayers=game.getPlayersIn(PlayerStateType.ActionState);
        if(actionStatePlayers.size()!=1){
            //TODO: error should be thrown -> exception!
        }
        else {
            Player currentPlayer = actionStatePlayers.get(0);
            for (Action action : ((ActionState) currentPlayer.getState()).getActionList()) {
                if (action.getActionType() == ActionType.MOVE) {
                    ((MoveAction) action).setMoveUpEnable(true);
                }
            }
        }
    }
    private void disableMoveUpOfOtherPlayers() {
        //TODO: should we use a visitor pattern to do this thing? so that we can remove the check for the ActionType.MOVE?
        for (Player player : game.getPlayersIn(PlayerStateType.IdleState)) {
            for (Action action : ((IdleState) player.getState()).getActionList()) {
                if (action.getActionType() == ActionType.MOVE) {
                    ((MoveAction) action).setMoveUpEnable(false);
                }
            }
        }
    }
    private void loadNextAction(){
        ArrayList<Player> actionStatePlayers=game.getPlayersIn(PlayerStateType.ActionState);
        if(actionStatePlayers.size()!=1){
            //TODO: error should be thrown -> exception!
        }
        else {
            Player currentPlayer = actionStatePlayers.get(0);
            //TODO: add this line of code when @luca implements setCurrentAction and getCurrentAction
            //((ActionState)currentPlayer.getState() ).setCurrentAction();
            //if(((ActionState)currentPlayer.getState()).getCurrentAction()==null){
            //    passTurnToNextPlayer();
            //}
        }
    }
    private void passTurnToNextPlayer(){
        Player nextPlayer=game.getNextActionStatePlayer();
        ArrayList<Player> currentPlayers=game.getPlayersIn(PlayerStateType.ActionState);
        if(currentPlayers.size()!=1){
            //TODO: error should be thrown -> exception!
        }
        else {
            Player currentPlayer = currentPlayers.get(0);
            ArrayList<Action> toBeLoaded;
            toBeLoaded=((IdleState)nextPlayer.getState()).getActionList();
            nextPlayer.setState(new ActionState(toBeLoaded));
            toBeLoaded=currentPlayer.getCurrentCard().getActionList();
            IdleState i=new IdleState();
            i.setActionList(toBeLoaded);
            currentPlayer.setState(i);
        }
    }

    @Override
    public void update(Action action) {
        action.accept(this);
    }
}
