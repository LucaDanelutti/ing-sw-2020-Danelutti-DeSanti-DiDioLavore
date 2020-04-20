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
            game.getBoard().pawnConstruct(constructAction.getSelectedPawn().getPosition(),constructAction.getChosenPosition(), constructAction.getSelectedBlockType());
        }

        //we update the pawns inside of ActionState for the user in ActionState (as they are a copy of the actual pawns in the board)
        //and in the currentAction
        getActionStateForCurrentPlayer().updatePawns(game.getBoard().getPawnCopy(constructAction.getSelectedPawn().getPosition()),game.getBoard().getPawnCopy(constructAction.getNotSelectedPawn().getPosition()));

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
                    actionState.addActionAfterCurrentOne(moveAction.duplicate());
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

        //this functions updates the copies of the pawns inside of both ActionState and the currentAction before calling checkWin
        getActionStateForCurrentPlayer().updatePawns(game.getBoard().getPawnCopy(newPos),game.getBoard().getPawnCopy(moveAction.getNotSelectedPawn().getPosition()));

        //after a move action is executed always check if the payer won
        if(moveAction.checkWin(game.getBoard().getMatrixCopy())){
            someoneWon(game.getPlayersIn(PlayerStateType.ActionState).get(0));
        }
        else{
            loadNextAction();
        }
    }
    public void executeAction(GeneralAction generalAction){
        //this is the special case for Charon
//        if(generalAction.getPushEnable()){
//            //delta is yourPawn - opponentPawn
//            int deltaX=generalAction.getSelectedPawn().getPosition().getX()-generalAction.getChosenPosition().getX();
//            int deltaY=generalAction.getSelectedPawn().getPosition().getY()-generalAction.getChosenPosition().getY();
//            Position otherSide=new Position(generalAction.getSelectedPawn().getPosition().getX()-deltaX,generalAction.getSelectedPawn().getPosition().getY()-deltaY);
//            game.getBoard().updatePawnPosition(generalAction.getChosenPosition(),otherSide);
//        }
        //this is the special case for Medusa
        if(generalAction.getDestroyPawnAndBuildEnable()){
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
                            game.getBoard().pawnConstruct(null, currentPawnPos,BlockType.LEVEL1);
                        }
                        else if(currentPeek==BlockType.LEVEL1){
                            game.getBoard().pawnConstruct(null, currentPawnPos,BlockType.LEVEL2);
                        }
                        otherPlayer.removePawn(p);
                    }
                }


            }

        }

        //TODO: load next action!!
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
        getActionStateForCurrentPlayer().setCurrentAction();
        if(getActionStateForCurrentPlayer().getCurrentAction()==null) {
            passTurnToNextPlayer();
        }
    }
    private void passTurnToNextPlayer() {
        Player nextPlayer = game.getNextPlayer(PlayerStateType.ActionState);
        Player currentPlayer = game.getPlayersIn(PlayerStateType.ActionState).get(0);

        if (nextPlayer == null) {
            //vuol dire che i next player sono già tutti in loser, allora io divento il vincente!
            currentPlayer.setState(new WinnerState());
        }
        else if (nextPlayer.getPawnList().size() == 0) {
                //se il next player non ha più pawn allora ha perso!
                nextPlayer.setState(new LoserState());
                if(game.getPlayers().size()==2){
                    //se siamo in due io ho automaticamente vinto!
                    currentPlayer.setState(new WinnerState());
                }
                else if(game.getPlayers().size()==3){
                    //se siamo in tre devo controllare anche l'altro giocatore
                    nextPlayer = game.getNextPlayer(PlayerStateType.ActionState);
                    if (nextPlayer == null){
                        //se non ci sono altri giocatori -> tutti in loser -> ho vinto
                        currentPlayer.setState(new WinnerState());
                    }
                    else{
                        if (nextPlayer.getPawnList().size() == 0) {
                            //se anche l'altro avversario c'è , ma ha finito i pawn -> lui ha perso e io ho vinto
                            nextPlayer.setState(new LoserState());
                            currentPlayer.setState(new WinnerState());
                        }
                    }
                }

            }
        else {
            ArrayList<Action> toBeLoadedInNextPlayer = nextPlayer.getCurrentCard().getCurrentActionList();
            nextPlayer.setState(new ActionState(toBeLoadedInNextPlayer));

            currentPlayer.getCurrentCard().resetCurrentActionList();
            currentPlayer.setState(new IdleState());
        }


    }
    private void someoneWon(Player winner){
        winner.setState(new WinnerState());
        for(Player loser : game.getPlayersIn(PlayerStateType.IdleState)){
            loser.setState(new LoserState());
        }
    }
    private ActionState getActionStateForCurrentPlayer(){
        return (ActionState) game.getPlayersIn(PlayerStateType.ActionState).get(0).getState();
    }

    //INTERFACE EXPOSED TO THE CONTROLLER

    /**
     * This function is called once the player enters the ActionState; currentAction and selectedPawn are null by default
     * we have to set the selectedPawn/unselectedPawn and load the first action by calling setCurrentAction
     * once setCurrentAction has done it's job the view will receive the notification that an action is ready to be run
     * @param selectedPawnPosition the selectedPawn
     * @param unselectedPawnPosition UnselectedPawn
     * @return the success of the operation
     */
    public Boolean setSelectedPawn(Position selectedPawnPosition, Position unselectedPawnPosition){
        ActionState actionState= getActionStateForCurrentPlayer();
        actionState.updatePawns(game.getBoard().getPawnCopy(selectedPawnPosition),game.getBoard().getPawnCopy(unselectedPawnPosition));
        //load the first action to be executed
        actionState.setCurrentAction();
        return true;
    }

    /**
     * This function is called when the view pass to the controller the position for the current action
     * if this is moveAction setChosenPosition will call back the gameLogicExecutor to execute the action
     * if this is constructAction setChosenPosition will wait to call back the gameLogicExecutor because it will
     * execute the action when the BlockType is selected
     * if this is a generalAction setChosenPosition will call back the gameLogicExecutor to execute the action
     * this function is used with chosenPos set to null when special generalAction are run, or when the user wants
     * to skip an optionalAction
     * @param chosenPos the chosen position
     * @return the success of the operation
     */
    public Boolean setChosenPosition(Position chosenPos){
        ActionState actionState= getActionStateForCurrentPlayer();
        if(chosenPos==null) {
            if (!actionState.getCurrentAction().getIsOptional()) {
                //TODO: throw an expection! only optional action can have chosenPos set to null
            }
            else if(actionState.getCurrentAction().getActionType()!=ActionType.GENERAL){
                //TODO: throw an expetion! only a general action can have chosenPos set to null (medusa)
            }
        }
        actionState.getCurrentAction().setChosenPosition(chosenPos);
        return true;
    }

    /**
     * This function sets the ChosenBlockType if the action is a ConstructAction or else it will return false
     * This will launch the execution of that specific ConstructAction via visitor pattern activated by the
     * setSelectedBlockType
     * @param blockType the block type
     * @return the success of the operation
     */
    public Boolean setChosenBlockType(BlockType blockType){
        ActionState actionState=getActionStateForCurrentPlayer();
        if(actionState.getCurrentAction().getActionType()==ActionType.CONSTRUCT) {
            ((ConstructAction) actionState.getCurrentAction()).setSelectedBlockType(blockType);
            return true;
        }
        else
            return false;
    }

    public Boolean setInGameCards(ArrayList<Integer> cards){
        //TODO: how do we handle the creation of cards? will the user just send the card IDs?
        return true;
    }

    public Boolean setChosenCard(int cardId){
        //TODO: how do we handle the selection of a card? will the user just send the card ID?
        return true;
    }

    public Boolean setPawnsPositions(ArrayList<Position> positions){
        //TODO: we could check if the positions are ok, but for now, nah...?
        Player currentPlayer=game.getPlayersIn(PlayerStateType.ChoosePawnsPositionState).get(0);
        game.getBoard().setPawnPosition(currentPlayer.getPawnList().get(0),positions.get(0));
        game.getBoard().setPawnPosition(currentPlayer.getPawnList().get(1),positions.get(1));
        return true;
    }

    /**
     * addPlayer method to add a player to the game. The first user to connect is set as the HOST player
     * and he is the only one that can start the game
     * @return the return value of Game method AddPlayer()
     */
    public Boolean addPlayer(String name){
        //if the user is the first make him the HOST
        if (game.getPlayers().size() == 0) {
            return game.addPlayer(new Player(name, new HostWaitOtherPlayersState()));
        }
        else {
            return game.addPlayer(new Player(name, new ClientWaitStartState()));
        }
    }

    /**
     * startGame method to start the game. After shuffling the players one player is set in SelectGameCardsState,
     * the other players are set in IdleState
     */
    public Boolean startGame() {
        //the most godLike -> random shuffle will chose the Cards in the game
        if (game.getPlayers().size() > 1) {
            game.shufflePlayers();
            game.getPlayers().get(0).setState(new SelectGameCardsState());
            game.getPlayers().get(1).setState(new IdleState());
            if (game.getPlayers().size() == 3)
                game.getPlayers().get(2).setState(new IdleState());
            return true;
        } else {
            return false;
        }
    }


}
