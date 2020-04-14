package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.playerstate.ActionState;
import it.polimi.ingsw.model.playerstate.IdleState;
import it.polimi.ingsw.model.playerstate.PlayerStateType;
import java.util.ArrayList;
import static java.lang.Math.abs;

public class GameLogicExecutor implements ActionObserver, ActionVisitor {
    private Game game;

    public GameLogicExecutor(Game game){
        this.game=game;
    }

    @Override public void update(Action action) {
        action.accept(this);
    }

    public void executeAction(ConstructAction constructAction){
        //This is an optional construct action to be skipped because the chosen position is not set!
        if(constructAction.getIsOptional()&&constructAction.getChosenPosition()==null){
            //Special condition for prometheus
            if(constructAction.getEnableMoveUp()){
                enableMoveUpForCurrentPlayer();
            }

            //generally we will load the next action or switch to the next player if turn ends
            loadNextAction();
        }
        //Normal case
        else {
            game.getBoard().pawnConstruct(constructAction.getChosenPosition(), constructAction.getSelectedBlockType());
        }

    }
    public void executeAction(MoveAction moveAction){
        Position oldPos =moveAction.getSelectedPawn().getPosition();
        Position newPos =moveAction.getChosenPosition();

        //let's search if in the newPos a pawn is present
        Pawn opponentPawn = game.getBoard().getMatrixCopy()[newPos.getX()][newPos.getY()].getPawn();

        if(opponentPawn==null){ //no pawn is detected in newPos
            game.getBoard().updatePawnPosition(oldPos,newPos);
            //Special case for Athena
            if(moveAction.getDenyMoveUpEnable() && moveAction.getSelectedPawn().getDeltaHeight()>0)
                disableMoveUpOfOtherPlayers();
        }
        else if(moveAction.getSwapEnable()){ //an opponent pawn is present && you have to swap the pawns
            game.getBoard().updatePawnPosition(oldPos,newPos,oldPos);
        }
        else if (moveAction.getPushEnable()){ //an opponent pawn is present && you have to push him
            Position opponentPawnNewPos;
            int deltaX=newPos.getX()-oldPos.getX();
            int deltaY=newPos.getY()-oldPos.getY();
            opponentPawnNewPos=new Position(newPos.getX()+deltaX,newPos.getY()+deltaY);
            game.getBoard().updatePawnPosition(oldPos,newPos,opponentPawnNewPos);
        }

        //after a move action is executed always check if the payer won
        if(moveAction.checkWin(game.getBoard().getMatrixCopy())){
            //TODO: you have to set the playerState for all the players
        }
        else{
            loadNextAction();
        }
    }
    public void executeAction(GeneralAction generalAction){
        //this is the special case for Charon
        if(generalAction.getPushEnable()){
            //delta is yourPawn - opponentPawn
            int deltaX=generalAction.getSelectedPawn().getPosition().getX()-generalAction.getChosenPosition().getX();
            int deltaY=generalAction.getSelectedPawn().getPosition().getY()-generalAction.getChosenPosition().getY();
            Position otherSide=new Position(generalAction.getSelectedPawn().getPosition().getX()-deltaX,generalAction.getSelectedPawn().getPosition().getY()-deltaY);
            game.getBoard().updatePawnPosition(generalAction.getChosenPosition(),otherSide);
        }
        //this is the special case for Medusa
        else if(generalAction.getDestroyPawnAndBuildEnable()){
            Pawn worker1=generalAction.getSelectedPawn();
            Pawn worker2=generalAction.getNotSelectedPawn();
            Position pos1=worker1.getPosition();
            Position pos2=worker2.getPosition();
            int height1= game.board.getMatrixCopy()[pos1.getX()][pos1.getY()].getSize();
            int height2= game.board.getMatrixCopy()[pos2.getX()][pos2.getY()].getSize();

            ArrayList<Player> otherPlayers=game.getPlayersIn(PlayerStateType.IdleState);
            Position currentPawnPos;
            int currentPawnHeight,deltaX1,deltaY1,deltaX2,deltaY2;
            BlockType currentPeek;

            for(Player otherPlayer : otherPlayers){
                for(Pawn p : otherPlayer.getPawnList()){
                    currentPawnPos=p.getPosition();
                    currentPawnHeight=game.board.getMatrixCopy()[currentPawnPos.getX()][currentPawnPos.getY()].getSize();
                    currentPeek=game.board.getMatrixCopy()[currentPawnPos.getX()][currentPawnPos.getY()].peekBlock();
                    deltaX1=abs(currentPawnPos.getX()-pos1.getX());
                    deltaY1=abs(currentPawnPos.getY()-pos1.getY());
                    deltaX2=abs(currentPawnPos.getX()-pos2.getX());
                    deltaY2=abs(currentPawnPos.getY()-pos2.getY());

                    //we have to remove the pawn from the game
                    if((deltaX1==1&&deltaY1==1&&height1>currentPawnHeight) || (deltaX2==1&&deltaY2==1&&height2>currentPawnHeight)){
                        game.getBoard().removePawnFromGame(p);
                        if(currentPeek==BlockType.TERRAIN){
                            game.getBoard().pawnConstruct(currentPawnPos,BlockType.LEVEL1);
                        }
                        else if(currentPeek==BlockType.LEVEL1){
                            game.getBoard().pawnConstruct(currentPawnPos,BlockType.LEVEL2);
                        }
                        otherPlayer.removePawn(p);
                    }
                }


            }

        }
    }

    private void enableMoveUpForCurrentPlayer() {
        ArrayList<Player> actionStatePlayers = game.getPlayersIn(PlayerStateType.ActionState);
        //TODO: error should be thrown by getPlayersIn-> exception!
        Player currentPlayer = actionStatePlayers.get(0);
        for (Action action : ((ActionState) currentPlayer.getState()).getActionList()) {
            if (action.getActionType() == ActionType.MOVE) {
                ((MoveAction) action).setMoveUpEnable(true);
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
        //TODO: error should be thrown by getPlayersIn -> exception!
        Player currentPlayer = actionStatePlayers.get(0);
        //TODO: add this line of code when @luca implements setCurrentAction and getCurrentAction
        //((ActionState)currentPlayer.getState() ).setCurrentAction();
        //if(((ActionState)currentPlayer.getState()).getCurrentAction()==null)
        //    passTurnToNextPlayer();
    }
    private void passTurnToNextPlayer() {
        Player nextPlayer = game.getNextActionStatePlayer();
        ArrayList<Player> currentPlayers = game.getPlayersIn(PlayerStateType.ActionState);
        //TODO: error should be thrown by getPlayersIn-> exception!
        Player currentPlayer = currentPlayers.get(0);
        ArrayList<Action> toBeLoaded;
        toBeLoaded = ((IdleState) nextPlayer.getState()).getActionList();
        nextPlayer.setState(new ActionState(toBeLoaded));
        toBeLoaded = currentPlayer.getCurrentCard().getDefaultActionList();
        //TODO: it would be usefull to have a parametric initialize of IdleState with the actionList
        IdleState i = new IdleState();
        i.setActionList(toBeLoaded);
        currentPlayer.setState(i);
    }

}
