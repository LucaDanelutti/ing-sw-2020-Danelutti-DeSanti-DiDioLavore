package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.playerstate.*;

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

        }
        //Normal case
        else {
            game.getBoard().pawnConstruct(constructAction.getChosenPosition(), constructAction.getSelectedBlockType());
        }
        //we update the pawns inside of ActionState for the user in ActionState (as they are a copy of the actual pawns in the board)
        updateSelectedPawnInActionState(constructAction.getSelectedPawn().getPosition());
        //generally we will load the next action or switch to the next player if turn ends
        loadNextAction();
    }
    public void executeAction(MoveAction moveAction){
        Position oldPos =moveAction.getSelectedPawn().getPosition();
        Position newPos =moveAction.getChosenPosition();

        //let's search if in the newPos a pawn is present
        Pawn opponentPawn = game.getBoard().getMatrixCopy()[newPos.getX()][newPos.getY()].getPawn();

        if(opponentPawn==null){ //no pawn is detected in newPos
            game.getBoard().updatePawnPosition(oldPos,newPos);
            //Special case for Athena
            if(moveAction.getDenyMoveUpEnable() && moveAction.getSelectedPawn().getDeltaHeight()>0) {
                disableMoveUpOfOtherPlayers();
            }
            //special case for Triton
            else if (moveAction.getAddMoveIfOn()!=null){
                //in this case we have to add another instance of moveAction to the actionList
                if(moveAction.getAddMoveIfOn().contains(newPos)){
                    ActionState actionState = (ActionState) game.getPlayersIn(PlayerStateType.ActionState).get(0).getState();
                    //TODO: implement addActionAfterCurrentOne inside of ActionState
                    //actionState.addActionAfterCurrentOne(moveAction.duplicate());
                }
            }
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

        updateSelectedPawnInActionState(newPos);
        updateSelectedPawnInCurrentAction(newPos);

        //after a move action is executed always check if the payer won
        if(moveAction.checkWin(game.getBoard().getMatrixCopy())){
            someoneWon(game.getPlayersIn(PlayerStateType.ActionState).get(0));
        }
        else{
            //TODO: if denyMoveBackInNextMove is set, we have to set NotAvaiableCell in the next optional move to the currentPos of the selectedPawn
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
        Player currentPlayer = actionStatePlayers.get(0);
        for (Action action : ((ActionState) currentPlayer.getState()).getActionList()) {
            if (action.getActionType() == ActionType.MOVE) {
                ((MoveAction) action).setMoveUpEnable(true);
            }
        }

    }
    private void disableMoveUpOfOtherPlayers() {
        for (Player player : game.getPlayersIn(PlayerStateType.IdleState)) {
            for (Action action : player.getCurrentCard().getCurrentActionList()) {
                if (action.getActionType() == ActionType.MOVE) {
                    ((MoveAction) action).setMoveUpEnable(false);
                }
            }
        }
    }
    private void loadNextAction(){
        ArrayList<Player> actionStatePlayers=game.getPlayersIn(PlayerStateType.ActionState);
        Player currentPlayer = actionStatePlayers.get(0);
        ((ActionState)currentPlayer.getState() ).setCurrentAction();
        //TODO: change to getCurrentAction
        if(((ActionState)currentPlayer.getState()).getCurrentAction()==null)
            passTurnToNextPlayer();
    }
    private void passTurnToNextPlayer() {
        Player nextPlayer = game.getNextActionStatePlayer();
        Player currentPlayer = game.getPlayersIn(PlayerStateType.ActionState).get(0);

        ArrayList<Action> toBeLoadedInNextPlayer=nextPlayer.getCurrentCard().getCurrentActionList();
        nextPlayer.setState(new ActionState(toBeLoadedInNextPlayer));

        currentPlayer.getCurrentCard().resetCurrentActionList();
        currentPlayer.setState(new IdleState());
    }
    private void updateSelectedPawnInActionState(Position selectedPawnPos){
        ArrayList<Player> players = game.getPlayersIn(PlayerStateType.ActionState);
        ActionState actionState=(ActionState)players.get(0).getState();
        actionState.setSelectedPawn(game.getBoard().getPawnCopy(selectedPawnPos));
    }
    private void updateSelectedPawnInCurrentAction(Position selectedPawnPos){
        ArrayList<Player> players = game.getPlayersIn(PlayerStateType.ActionState);
        ActionState actionState=(ActionState)players.get(0).getState();
        Action currentAction = actionState.getCurrentAction();
        currentAction.setSelectedPawn(game.getBoard().getPawnCopy(selectedPawnPos));
    }
    private void someoneWon(Player winner){
        winner.setState(new WinnerState());
        for(Player loser : game.getPlayersIn(PlayerStateType.IdleState)){
            loser.setState(new LoserState());
        }
    }

}
