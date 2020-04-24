package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.playerstate.*;
import it.polimi.ingsw.utility.ActionDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.abs;

public class GameLogicExecutor implements ActionObserver, ActionVisitor {
    private Game game;

    /**
     * This is the constructor for the class GameLogicExecutor
     * @param game the game to be controlled
     */
    public GameLogicExecutor(Game game){
        this.game=game;
    }

    /**
     * This function is a part of the visitor pattern used to call the correct executeAction using dynamic binding
     * @param action the action to be performed
     */
    @Override public void update(Action action) {
        action.accept(this);
    }

    /**
     * This function executes a CONSTRUCT action called via visitor pattern.
     * @param constructAction the action to be executed
     */
    public void executeAction(ConstructAction constructAction){
        //This is an optional construct action to be skipped because the chosen position is not set!
        if(constructAction.getIsOptional()&&constructAction.getChosenPosition()==null){
            //Special condition for prometheus
//            if(constructAction.getEnableMoveUp()){
//                enableMoveUpForCurrentPlayer();
//            }

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

    /**
     * This function executes a MOVE action called via visitor pattern.
     * @param moveAction the action to be executed
     */
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

    /**
     * This function executes a GENERAL action called via visitor pattern.
     * @param generalAction the action to be executed
     */
    public void executeAction(GeneralAction generalAction){
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
        //This is the special case for Hera
        else if(generalAction.getEnableNoWinIfOnPerimeter()){
            enableNoWinIfOnPerimeterForOpponents();
        }

        loadNextAction();
    }

    /**
     * This function is called to turn to true the parameter NoWinIfOnPerimeter on each opponent moveAction.
     * It is called when a generalAction with enableNoWinIfOnPerimeter is executed
     */
    private void enableNoWinIfOnPerimeterForOpponents(){
        for(Player p : game.getPlayersIn(PlayerStateType.IdleState)){
            for(Action a : p.getCurrentCard().getCurrentActionList()){
                if(a.getActionType()==ActionType.MOVE){
                    ((MoveAction)a).setNoWinIfOnPerimeter(true);
                }
            }
        }
    }

    /**
     * This function is called to turn to TRUE the parameter moveUp for each moveAction for the current player in actionState
     */
    private void enableMoveUpForCurrentPlayer() {
        ArrayList<Player> actionStatePlayers = game.getPlayersIn(PlayerStateType.ActionState);
        Player currentPlayer = actionStatePlayers.get(0);
        for (Action action : ((ActionState) currentPlayer.getState()).getActionList()) {
            if (action.getActionType() == ActionType.MOVE) {
                ((MoveAction) action).setMoveUpEnable(true);
            }
        }

    }

    /**
     * This function is called to turn to FALSE the moveUp parameter on each opponent moveAction
     */
    private void disableMoveUpOfOtherPlayers() {
        for (Player player : game.getPlayersIn(PlayerStateType.IdleState)) {
            for (Action action : player.getCurrentCard().getCurrentActionList()) {
                if (action.getActionType() == ActionType.MOVE) {
                    ((MoveAction) action).setMoveUpEnable(false);
                }
            }
        }
    }

    /**
     * This function is called to load the next action and if no action is available it will pass the turn to the next player
     */
    private void loadNextAction(){
        getActionStateForCurrentPlayer().setCurrentAction();
        if(getActionStateForCurrentPlayer().getCurrentAction()==null) {
            passTurnToNextPlayer();
        }
    }

    /**
     * This function is called to pass the turn to the next player. It also handles the following cases:
     * 1) The current player is the last one remained in the game, so the current player is the winner
     * 2) The next player or all of them are unable to perform a turn (for example no pawn remained), so the current player is the winner
     */
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

    /**
     * This function is called when after an execution of a MoveAction the checkWin function (for that specific action) return true.
     * It places the passed player into winner state and the others in loser state.
     * @param winner the player who won
     */
    private void someoneWon(Player winner){
        winner.setState(new WinnerState());
        for(Player loser : game.getPlayersIn(PlayerStateType.IdleState)){
            loser.setState(new LoserState());
        }
    }

    /**
     * This function is used to return the ActionState for the currentPlayer
     * @return the ActionState for the current player
     */
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
        if(chosenPos==null && (!actionState.getCurrentAction().getIsOptional() || actionState.getCurrentAction().getActionType()!=ActionType.GENERAL) ) {
                //TODO: throw an expetion! only a general action or optional Action can have chosenPos set to null
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


    //TODO: mettere questo metodo in una apposita helper class
    /**
     * Helper method used to load the json file within the resources folder
     */
    private String getResource(String resource) {
        StringBuilder json = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(resource)),
                            StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null)
                json.append(str);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Caught exception reading resource " + resource, e);
        }
        return json.toString();
    }

    /**
     * Setup method loadCards to load cards in the game. We read cards from a JSON config file
     */
    public Boolean loadCards() {
        String json = getResource("configFiles/config.json");

        //Sets Action typeAdapter so as to instance the correct subtype of Action
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Action.class, new ActionDeserializer());
        Gson gson = gsonBuilder.create();
        
        //Deserialization
        Type cardListType = new TypeToken<ArrayList<Card>>(){}.getType();
        ArrayList<Card> cardList = gson.fromJson(json, cardListType);
        game.setLoadedCardsCopy(cardList);

        return true;
    }

    /**
     * Setup method setInGameCards to set the cards that will be available to players
     * Players will choose a card in ChooseCardState
     */
    public Boolean setInGameCards(ArrayList<Integer> cards){
        //TODO: how do we handle the creation of cards? will the user just send the card IDs?
        return true;
    }

    /**
     * Setup method setChosenCard to set the chosen card in the player
     */
    public Boolean setChosenCard(Card card){
        if (game.getPlayersIn(PlayerStateType.ChooseCardState).size() == 1) {
            Player currentPlayer = game.getPlayersIn(PlayerStateType.ChooseCardState).get(0);
            if (game.getAvailableCards().contains(card)) {
                currentPlayer.setCurrentCard(new Card(card));
                //Pass turn
                Player nextPlayer = game.getNextPlayer(PlayerStateType.ChooseCardState);
                if (game.getAvailableCards().size() == 0) { //All cards are linked to a player
                    currentPlayer.setState(new SelectFirstPlayerState());
                } else {
                    currentPlayer.setState(new IdleState());
                    nextPlayer.setState(new ChooseCardState());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Setup method setPawnsPositions to set the two pawns of the current player
     */
    public Boolean setPawnsPositions(ArrayList<Position> positions){
        //TODO: we could check if the positions are ok, but for now, nah...?
        if (game.getPlayersIn(PlayerStateType.ChoosePawnsPositionState).size() == 1) {
            Player currentPlayer=game.getPlayersIn(PlayerStateType.ChoosePawnsPositionState).get(0);
            game.getBoard().setPawnPosition(currentPlayer.getPawnList().get(0),positions.get(0));
            game.getBoard().setPawnPosition(currentPlayer.getPawnList().get(1),positions.get(1));
            return true;
        }
        return false;
    }

    /**
     * Setup method addPlayer to add a player to the game. The first user to connect is set as the HOST player
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
