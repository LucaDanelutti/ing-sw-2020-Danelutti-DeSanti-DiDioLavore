package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.utility.ActionDeserializer;
import it.polimi.ingsw.utility.UtilityClass;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;
import javafx.geometry.Pos;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class GameLogicExecutor extends RequestAndUpdateObservable implements ActionVisitor {
    private Game game;
    private ArrayList<String> lobby;
    private int numberOfPlayers; //set to -1 by constructor

    /**
     * This is the constructor for the class GameLogicExecutor
     * @param game the game to be controlled
     */
    public GameLogicExecutor(Game game){
        this.lobby=new ArrayList<>();
        this.game=game;
        this.numberOfPlayers=-1;
    }



    /* ------------------------------------------------------------------------------------------------------------------------------------ */
                                            //ACTION PROCESS FUNCTIONS
    /* ------------------------------------------------------------------------------------------------------------------------------------ */
    public void processAction(MoveAction moveAction) {
        if (moveAction.getChosenPosition() == null) {
            notifyListeners(generateChosenPositionRequest());

        } else {
            executeAction(moveAction);
        }

    }
    public void processAction(ConstructAction constructAction){
        if(constructAction.getChosenPosition()==null){
            notifyListeners(generateChosenPositionRequest());
        }
        else if(constructAction.getSelectedBlockType()==null){
            notifyListeners(generateChosenBlockTypeRequest(constructAction));
        }
        else{
            executeAction(constructAction);
        }
    }
    public void processAction(GeneralAction generalAction){
        executeAction(generalAction);
    }
    /* ------------------------------------------------------------------------------------------------------------------------------------ */





    /* ------------------------------------------------------------------------------------------------------------------------------------ */
                                            //ACTION EXECUTORS
    /* ------------------------------------------------------------------------------------------------------------------------------------ */
    /**
     * This function executes a CONSTRUCT action called via visitor pattern.
     * @param constructAction the action to be executed
     */
    public void executeAction(ConstructAction constructAction) {
        //if the construct action is not skipped we update the necessary variables
        if(! (constructAction.getIsOptional() && constructAction.getChosenPosition()==null)) {
            game.getBoard().pawnConstruct(constructAction.getSelectedPawn().getPosition(), constructAction.getChosenPosition(), constructAction.getSelectedBlockType());
            //special case for prometheus
            if (constructAction.getdisableMoveUp()) {
                disableMoveUpForCurrentPlayer();
            }

            //we update the pawns inside of ActionState for the user in ActionState (as they are a copy of the actual pawns in the board)
            //and in the currentAction
            game.updatePawns(game.getBoard().getPawnCopy(constructAction.getSelectedPawn().getPosition()), game.getBoard().getPawnCopy(constructAction.getNotSelectedPawn().getPosition()));

            //notify listeners of the change in the board
            notifyListeners(generateCellUpdate(constructAction.getSelectedBlockType(),constructAction.getChosenPosition()));
        }
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

        if(!(moveAction.getIsOptional() && newPos==null) ) {
            //let's search if in the newPos a pawn is present
            Pawn opponentPawn = game.getBoard().getMatrixCopy()[newPos.getX()][newPos.getY()].getPawn();

            if (opponentPawn == null) { //no pawn is detected in newPos
                game.getBoard().updatePawnPosition(oldPos, newPos);
                //this functions updates the copies of the pawns inside of both ActionState and the currentAction before calling checkWin
                game.updatePawns(game.getBoard().getPawnCopy(newPos), game.getBoard().getPawnCopy(moveAction.getNotSelectedPawn().getPosition()));

                //Special case for Athena
                if (moveAction.getDenyMoveUpEnable() && moveAction.getSelectedPawn().getDeltaHeight() > 0) {
                    disableMoveUpOfOtherPlayers();
                }
                //special case for Triton
                else if (moveAction.getAddMoveIfOn() != null) {
                    //in this case we have to add another instance of moveAction to the actionList
                    if (moveAction.getAddMoveIfOn().contains(newPos)) {
                        game.addActionAfterCurrentOne(moveAction.duplicate());
                    }
                }

                //now i have to send the update to all the players
                notifyListeners(generatePawnPositionUpdate());

            }
            else if (moveAction.getSwapEnable()) { //an opponent pawn is present && you have to swap the pawns
                game.getBoard().updatePawnPosition(oldPos, newPos, oldPos);

                //this functions updates the copies of the pawns inside of both ActionState and the currentAction before calling checkWin
                game.updatePawns(game.getBoard().getPawnCopy(newPos), game.getBoard().getPawnCopy(moveAction.getNotSelectedPawn().getPosition()));

                //notify all players of the pawns position change
                notifyListeners(generateDoublePawnPositionUpdate(moveAction.getSelectedPawn().getId(),opponentPawn.getId(),newPos,oldPos));

            }
            else if (moveAction.getPushEnable()) { //an opponent pawn is present && you have to push him
                Position opponentPawnNewPos;
                int deltaX = newPos.getX() - oldPos.getX();
                int deltaY = newPos.getY() - oldPos.getY();
                opponentPawnNewPos = new Position(newPos.getX() + deltaX, newPos.getY() + deltaY);
                game.getBoard().updatePawnPosition(oldPos, newPos, opponentPawnNewPos);

                //this functions updates the copies of the pawns inside of both ActionState and the currentAction before calling checkWin
                game.updatePawns(game.getBoard().getPawnCopy(newPos), game.getBoard().getPawnCopy(moveAction.getNotSelectedPawn().getPosition()));

                //notify all the players of the change
                notifyListeners(generateDoublePawnPositionUpdate(moveAction.getSelectedPawn().getId(),opponentPawn.getId(),newPos,opponentPawnNewPos));
            }


            //after a move action is executed always check if the payer won
            if(moveAction.checkWin(game.getBoard().getMatrixCopy())){
                someoneWon(game.getCurrentPlayer());
            }
            else{
                loadNextAction();
            }
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

            ArrayList<Player> otherPlayers=new ArrayList<>();
            for(Player p : game.getPlayers()){
                if(!p.getName().equals(game.getCurrentPlayer().getName())){
                    otherPlayers.add(p);
                }
            }

            Position currentPawnPos;
            int currentPawnHeight,deltaX1,deltaY1,deltaX2,deltaY2;
            BlockType currentPeek;

            for(Player otherPlayer : otherPlayers){
                ArrayList<Pawn> toBeRemoved = new ArrayList<>();
                ArrayList<Pawn> pawnList = otherPlayer.getPawnList();

                for (Pawn p : pawnList) {
                    currentPawnPos = p.getPosition();
                    currentPawnHeight = game.board.getMatrixCopy()[currentPawnPos.getX()][currentPawnPos.getY()].getSize();
                    currentPeek = game.board.getMatrixCopy()[currentPawnPos.getX()][currentPawnPos.getY()].peekBlock();

                    //distance vector from the first pawn of medusa
                    deltaX1 = abs(currentPawnPos.getX() - pos1.getX());
                    deltaY1 = abs(currentPawnPos.getY() - pos1.getY());

                    //distance vector from the second pawn of medusa
                    deltaX2 = abs(currentPawnPos.getX() - pos2.getX());
                    deltaY2 = abs(currentPawnPos.getY() - pos2.getY());

                    //we have to remove the pawn from the game
                    if ((deltaX1 <=1 && deltaY1 <= 1 && height1 > currentPawnHeight) || (deltaX2 <=1 && deltaY2 <= 1 && height2 > currentPawnHeight)) {
                        game.getBoard().removePawnFromGame(p);

                        //notify all listeners of the removed pawn
                        notifyListeners(generatePawnRemoveUpdate(p.getId()));

                        if (currentPeek == BlockType.TERRAIN) {
                            game.getBoard().pawnConstruct(null, currentPawnPos, BlockType.LEVEL1);
                            notifyListeners(generateCellUpdate(BlockType.LEVEL1,currentPawnPos));
                        } else if (currentPeek == BlockType.LEVEL1) {
                            game.getBoard().pawnConstruct(null, currentPawnPos, BlockType.LEVEL2);
                            notifyListeners(generateCellUpdate(BlockType.LEVEL2,currentPawnPos));
                        } else if (currentPeek == BlockType.LEVEL2) {
                            game.getBoard().pawnConstruct(null, currentPawnPos, BlockType.LEVEL3);
                            notifyListeners(generateCellUpdate(BlockType.LEVEL3,currentPawnPos));

                        }
                        toBeRemoved.add(p);
                    }
                }
                //at this point we have the pawns to be removed from the opponent player and we remove them from the player
                for(Pawn pawn : toBeRemoved){
                    otherPlayer.removePawn(pawn);
                }
            }
        }

        //This is the special case for Hera
        else if(generalAction.getEnableNoWinIfOnPerimeter()){
            enableNoWinIfOnPerimeterForOpponents();
        }

        loadNextAction();
    }
    /* ------------------------------------------------------------------------------------------------------------------------------------ */




    /* ------------------------------------------------------------------------------------------------------------------------------------ */
                                            //PRIVATE COMMODITY FUNCTIONS
    /* ------------------------------------------------------------------------------------------------------------------------------------ */
    /**
     * This function is called to turn to true the parameter NoWinIfOnPerimeter on each opponent moveAction.
     * It is called when a generalAction with enableNoWinIfOnPerimeter is executed
     */
    private void enableNoWinIfOnPerimeterForOpponents(){
        for(Player p : game.getPlayers()){
            if(!p.getName().equals(game.getCurrentPlayer().getName())){
                for(Action a : p.getCurrentCard().getCurrentActionList()){
                    a.disablePerimeterWin();
                }
            }
        }
    }
    /**
     * This function is called to turn to FALSE the parameter moveUp for each moveAction for the current player in actionState
     */
    private void disableMoveUpForCurrentPlayer() {
        for (Action action : game.getCurrentPlayer().getCurrentCard().getCurrentActionList()) {
            action.disableClimbing();
        }

    }
    /**
     * This function is called to turn to FALSE the moveUp parameter on each opponent moveAction
     */
    private void disableMoveUpOfOtherPlayers() {
        for (Player p : game.getPlayers()) {
            if(!p.getName().equals(game.getCurrentPlayer().getName())){
                for (Action action : p.getCurrentCard().getCurrentActionList()) {
                    action.disableClimbing();
                }
            }
        }
    }
    /**
     * This function is called to load the next action and if no action is available it will pass the turn to the next player
     */
    private void loadNextAction(){
        game.setCurrentAction();
        if(game.getCurrentAction()==null) {
            passTurnToNextPlayer();
        }
        else{
            game.getCurrentAction().acceptForProcess();
        }
    }
    /**
     * This function is called to pass the turn to the next player. It also handles the following cases:
     * 1) The current player is the last one remained in the game, so the current player is the winner
     * 2) The next player or all of them are unable to perform a turn (for example no pawn remained), so the current player is the winner
     */
    private void passTurnToNextPlayer() {
        Player nextPlayer = game.getNextNonLoserPlayer();

        if (nextPlayer == null) {
            //every other player is in loser state, you are the winner
            game.getCurrentPlayer().setWinner(true);
            notifyListeners(generateYouWon());
            notifyListeners(generateYouLostAndSomeOneWon(game.getCurrentPlayer().getName()));
        }
        else if (nextPlayer.getPawnList().size() == 0) {
                //se il next player non ha più pawn allora ha perso!
                nextPlayer.setLoser(true);
                if(game.getPlayers().size()==2){
                    //se siamo in due io ho automaticamente vinto!
                    game.getCurrentPlayer().setWinner(true);
                    notifyListeners(generateYouLostAndSomeOneWon(game.getCurrentPlayer().getName()));
                    notifyListeners(generateYouWon());
                }
                else if(game.getPlayers().size()==3){
                    //se siamo in tre devo controllare anche l'altro giocatore
                    Player tempLoser=nextPlayer;
                    nextPlayer = game.getNextNonLoserPlayer();
                    if (nextPlayer == null){
                        //se non ci sono altri giocatori -> tutti in loser -> ho vinto
                        game.getCurrentPlayer().setWinner(true);
                        notifyListeners(generateYouLostAndSomeOneWon(game.getCurrentPlayer().getName()));
                        notifyListeners(generateYouWon());
                    }
                    else if (nextPlayer.getPawnList().size() == 0) {
                            //se anche l'altro avversario c'è , ma ha finito i pawn -> lui ha perso e io ho vinto
                            nextPlayer.setLoser(true);
                            game.getCurrentPlayer().setWinner(true);
                            notifyListeners(generateYouLostAndSomeOneWon(game.getCurrentPlayer().getName()));
                            notifyListeners(generateYouWon());
                        }
                    else {
                        //the third player can be putted in ActionState
                        game.getCurrentPlayer().getCurrentCard().resetCurrentActionList();
                        notifyListeners(generateYouLost(tempLoser.getName()));
                        notifyListeners(generateTurnEnded());//this will send turn ended to the currentUser before setting a new one
                        game.setCurrentPlayer(nextPlayer);
                        notifyListeners(generateSelectPawnRequest());
                    }
                }
            }
        else {
            //the next player can play normally, normal case
            game.getCurrentPlayer().getCurrentCard().resetCurrentActionList();
            notifyListeners(generateTurnEnded());//this will send turn ended to the currentUser before setting a new one
            game.setCurrentPlayer(nextPlayer);
            notifyListeners(generateSelectPawnRequest());
        }

    }
    /**
     * This function is called when after an execution of a MoveAction the checkWin function (for that specific action) return true.
     * It places the passed player into winner state and the others in loser state.
     * @param winner the player who won
     */
    private void someoneWon(Player winner){
        winner.setWinner(true);
        notifyListeners(generateYouWon());
        for(Player loser : game.getPlayers()){
            if(!loser.getName().equals(winner.getName())) {
                loser.setLoser(true);
            }
        }
        notifyListeners(generateYouLostAndSomeOneWon(winner.getName()));
    }

    private PlayerView modelPlayer_to_viewPlayer(Player player){
        ArrayList<PawnView> pawnViews = new ArrayList<>();
        for(Pawn p : player.getPawnList()){
            pawnViews.add(new PawnView(p.getId(),p.getColor()));
        }
        return new PlayerView(player.getName(),pawnViews);
    }
    private CardView modelCard_to_viewCard(Card card){
        //TODO: change blabla to current description
        return new CardView(card.getId(),card.getName(),"blabla");
    }
    private ArrayList<CardView>loadedCards_to_viewCards(){
        ArrayList<CardView> availableViewCards = new ArrayList<>();
        for(Card card : game.getAvailableCards()){
            availableViewCards.add(modelCard_to_viewCard(card));
        }
        return availableViewCards;
    }

    /* ------------------------------------------------------------------------------------------------------------------------------------ */



    /* ------------------------------------------------------------------------------------------------------------------------------------ */
                                                //FUNCTIONS THAT GENERATE MESSAGES
    /* ------------------------------------------------------------------------------------------------------------------------------------ */
    private ChosenPositionRequestMessage generateChosenPositionRequest(){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        ArrayList<Position> availablePositions = game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        return new ChosenPositionRequestMessage(recipients, availablePositions);
    }
    private ChosenBlockTypeRequestMessage generateChosenBlockTypeRequest(ConstructAction constructAction){
        ArrayList<String> recipients=new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        ArrayList<BlockType> blockTypes = constructAction.availableBlockTypes(constructAction.getChosenPosition(),game.getBoard().getMatrixCopy());
        return new ChosenBlockTypeRequestMessage(recipients,blockTypes);
    }
    private SelectPawnRequestMessage generateSelectPawnRequest(){
        ArrayList<String> recipients=new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        ArrayList<Position> availablePawnsPos=new ArrayList<>();
        for(Pawn p : game.getCurrentPlayer().getPawnList()){
            availablePawnsPos.add(p.getPosition());
        }
        return new SelectPawnRequestMessage(recipients, availablePawnsPos);
    }
    private ChosenCardRequestMessage generateChosenCardRequest(){
        ArrayList<String> recipients=new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        ArrayList<CardView> cards = new ArrayList<>();
        for(Card card : game.getAvailableCards()){
            cards.add(modelCard_to_viewCard(card));
        }
        return new ChosenCardRequestMessage(recipients,cards);
    }
    private FirstPlayerRequestMessage generateFirstPlayerRequest(){
        ArrayList<String> recipients=new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        return new FirstPlayerRequestMessage(recipients);
    }
    private InGameCardsRequestMessage generateInGameCardRequest(){
        ArrayList<String> recipients=new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());

        ArrayList<PlayerView> playerViews = new ArrayList<>();
        for(Player p : game.getPlayers()){
            playerViews.add(modelPlayer_to_viewPlayer(p));
        }

        return new InGameCardsRequestMessage(recipients,playerViews,loadedCards_to_viewCards());
    }
    private InitialPawnPositionRequestMessage generateInitialPawnPositionRequest() {
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        return new InitialPawnPositionRequestMessage(recipients, game.getBoard().availablePositionsForPawnInitialPlacement());
    }
    private SelectedPawnUpdateMessage generateSelectedPawnUpdate(Position selectedPawnPosition){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        int id=-1;
        for(Pawn p : game.getCurrentPlayer().getPawnList()){
            if(p.getPosition().equals(selectedPawnPosition)){
                id=p.getId();
                break;
            }
        }
        return new SelectedPawnUpdateMessage(recipients,id);
    }
    private PawnPositionUpdateMessage generatePawnPositionUpdate(){
        ArrayList<String> recipients = new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        return new PawnPositionUpdateMessage(recipients,game.getCurrentAction().getSelectedPawn().getId(),game.getCurrentAction().getChosenPosition());
    }
    private DoublePawnPositionUpdateMessage generateDoublePawnPositionUpdate(int yourPawnId, int otherPlayerPawnId, Position yourPawnPos, Position otherPlayerNewPawnPosition){
        ArrayList<String> recipients = new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        return new DoublePawnPositionUpdateMessage(recipients,yourPawnId,otherPlayerPawnId,yourPawnPos,otherPlayerNewPawnPosition);
    }
    private CellUpdateMessage generateCellUpdate(BlockType newBlock,Position updatedCellPosition){
        ArrayList<String> recipients = new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        return new CellUpdateMessage(recipients,newBlock,updatedCellPosition);
    }
    private PawnRemoveUpdateMessage generatePawnRemoveUpdate(int pawnId){
        ArrayList<String> recipients = new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        return new PawnRemoveUpdateMessage(recipients,pawnId);
    }
    private ChosenCardUpdateMessage generateChosenCardUpdate(Card card){
        ArrayList<String> recipients = new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        //TODO: change blabla to correct
        return new ChosenCardUpdateMessage(recipients,new CardView(card.getId(),card.getName(),"BlaBla"),game.getCurrentPlayer().getName());
    }

    private GameStartMessage generateGameStart(){
        ArrayList<String> recipients = new ArrayList<>();
        for(Player p : game.getPlayers()){
            if(!p.getName().equals(game.getCurrentPlayer().getName())) {
                recipients.add(p.getName());
            }
        }

        ArrayList<PlayerView> playerViews = new ArrayList<>();
        for(Player p : game.getPlayers()){
            playerViews.add(modelPlayer_to_viewPlayer(p));
        }

        return new GameStartMessage(recipients,playerViews);
    }
    private YouWonMessage generateYouWon(){
        ArrayList<String> recipients=new ArrayList<>();

        for(Player p : game.getPlayers()){
            if(p.getWinner()){
                recipients.add(p.getName());
            }
        }
        return new YouWonMessage(recipients);
    }
    private YouLostAndSomeoneWonMessage generateYouLostAndSomeOneWon(String winnerName){
        ArrayList<String> recipients=new ArrayList<>();
        for(Player p : game.getPlayers()){
            if(!p.getWinner()){
                recipients.add(p.getName());
            }
        }
        return new YouLostAndSomeoneWonMessage(recipients,winnerName);

    }
    private YouLostMessage generateYouLost(String loserName){
        ArrayList<String> recipients=new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        return new YouLostMessage(recipients,loserName);
    }
    private TurnEndedMessage generateTurnEnded(){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        return new TurnEndedMessage(recipients);
    }



    /* ------------------------------------------------------------------------------------------------------------------------------------ */



    /* ------------------------------------------------------------------------------------------------------------------------------------ */
                                            //INTERFACE EXPOSED TO THE CONTROLLER
    /* ------------------------------------------------------------------------------------------------------------------------------------ */
    /**
     * This function is called once the player enters the ActionState; currentAction and selectedPawn are null by default
     * we have to set the selectedPawn/unselectedPawn and load the first action by calling setCurrentAction
     * once setCurrentAction has done it's job the view will receive the notification that an action is ready to be run
     * @param selectedPawnPosition the selectedPawn
     * @param unselectedPawnPosition UnselectedPawn
     * @return the success of the operation
     */
    public Boolean setSelectedPawn(Position selectedPawnPosition, Position unselectedPawnPosition){
        //load the first action to be executed
        game.setCurrentAction();

        //aggiorna i selectedPawn e i pawn dentro la currentAction
        game.updatePawns(game.getBoard().getPawnCopy(selectedPawnPosition),game.getBoard().getPawnCopy(unselectedPawnPosition));

        //notify all the virtualViews of the change in the selected pawn but recipients is composed by only the name of the current player
        notifyListeners(generateSelectedPawnUpdate(selectedPawnPosition));

        //process the action, aka. ask the user for the chosen position for the selected pawn
        game.getCurrentAction().acceptForProcess(); //this will generate the requestMessage for the chosen position

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
        //this call activates the execution of the moveAction but for the constructAction we have to ask the user for the selectedBlockType
        if(game.getCurrentAction().getIsOptional()){
            if(chosenPos==null) {
                game.getCurrentAction().setChosenPosition(null);
                game.currentAction.accept(this); //to be executed(skipped) directly this will call the executeAction
            }
            else{
                game.getCurrentAction().setChosenPosition(chosenPos);
                game.getCurrentAction().acceptForProcess(); //this will either ask for something else or execute the action
            }
        }
        else{
            game.getCurrentAction().setChosenPosition(chosenPos);
            game.getCurrentAction().acceptForProcess();
        }


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
        game.getCurrentAction().blockSelected(blockType);
        game.getCurrentAction().acceptForProcess();
        return true;
    }
    /**
     * Setup method loadCards to load cards in the game. We read cards from a JSON config file
     */
    public Boolean loadCards() {
        String json = UtilityClass.getResource("configFiles/config.json");

        //Sets Action typeAdapter so as to instance the correct subtype of Action
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Action.class, new ActionDeserializer());
        Gson gson = gsonBuilder.create();
        
        //Deserialization
        Type cardListType = new TypeToken<ArrayList<Card>>(){}.getType();
        ArrayList<Card> cardList = gson.fromJson(json, cardListType);

        //Adds the observers to the Actions within cardList
        ArrayList<Card> updatedCardList = new ArrayList<>();
        for(Card card: cardList) {
            ArrayList<Action> updatedActionList = new ArrayList<>();
            for(Action updatedAction: card.getDefaultActionListCopy()) {
                updatedAction.addVisitor(this);
                updatedActionList.add(updatedAction);
            }
            Card updatedCard = new Card(card.getName(), card.getId(), updatedActionList);
            updatedCardList.add(updatedCard);

        }

        game.setLoadedCardsCopy(updatedCardList);

        return true;
    }
    /**
     * Setup method setFirstPlayer to set the first player that will start to play the game
     */
    public Boolean setStartPlayer(String player){
        //this also sets current action to null
        game.setCurrentPlayer(game.getPlayer(player));
        notifyListeners(generateInitialPawnPositionRequest());
        return true;
    }
    /**
     * Setup method setInGameCards to set the cards that will be available to players
     * Players will choose a card in ChooseCardState
     */
    public Boolean setInGameCards(ArrayList<Integer> cards){
        if (cards.size() == game.getPlayers().size()) { //Every player must have one and only one card
            ArrayList<Card> inGameCards = new ArrayList<>();
            for (int cardID : cards) {
                if (game.getLoadedCardCopy(cardID) == null) return false;
                inGameCards.add(game.getLoadedCardCopy(cardID));
            }
            game.setInGameCardsCopy(inGameCards);
            Player nextPlayer = game.getNextPlayer();
            game.setCurrentPlayer(nextPlayer);
            notifyListeners(generateChosenCardRequest());
            return true;
        }
        return false;
    }
    /**
     * Setup method setChosenCard to set the chosen card in the player
     */
    public Boolean setChosenCard(int cardID){
        Card card = game.getInGameCardCopy(cardID);
        if (game.getAvailableCards().contains(card)) {
            game.getCurrentPlayer().setCurrentCard(new Card(card));

            //notify all listeners
            notifyListeners(generateChosenCardUpdate(card));

            //Pass turn
            Player nextPlayer = game.getNextPlayer();
            if (game.getAvailableCards().size() == 0) {
                //All cards are linked to a player
                notifyListeners(generateFirstPlayerRequest());
            } else {
                //next one should select its card
                game.setCurrentPlayer(nextPlayer);
                notifyListeners(generateChosenCardRequest());
            }
            return true;
        }
        return false;
    }
    /**
     * Setup method setPawnsPositions to set the two pawns of the current player
     */
    public Boolean setPawnsPositions(ArrayList<Position> positions){
        if (positions.get(0).getX() >= 0 & positions.get(0).getX() < 5 & positions.get(0).getY() >= 0 & positions.get(0).getY() < 5 & positions.get(1).getX() >= 0 & positions.get(1).getX() < 5 & positions.get(1).getY() >= 0 & positions.get(1).getY() < 5) {
            game.getBoard().setPawnPosition(game.getCurrentPlayer().getPawnList().get(0), positions.get(0));
            game.getBoard().setPawnPosition(game.getCurrentPlayer().getPawnList().get(1), positions.get(1));
            notifyListeners(generateDoublePawnPositionUpdate(game.getCurrentPlayer().getPawnList().get(0).getId(),game.getCurrentPlayer().getPawnList().get(1).getId(),positions.get(0),positions.get(1)));
            Player nextPlayer = game.getNextPlayer();
            if (nextPlayer.getPawnList().get(0).getPosition() != null & nextPlayer.getPawnList().get(1).getPosition() != null) {
                //so all players have set their pawns initial position, gameLogic will ask the user to send its selectedPawn
                notifyListeners(generateSelectPawnRequest());

            } else {
                //otherwise i have to ask the next player to set its initial pawn positions
                //this function will also set CurrentAction to null
                game.setCurrentPlayer(nextPlayer);
                notifyListeners(generateInitialPawnPositionRequest());
            }
            return true;
        }

        return false;
    }

    public Boolean addPlayerToLobby(String name){
        this.lobby.add(name);
        if(this.lobby.size()>=numberOfPlayers && numberOfPlayers>1){
            startGame();
        }
        return true;
    }
    public Boolean setNumberOfPlayers(int numberOfPlayers){
        if(numberOfPlayers<=1 || numberOfPlayers>3){
            return false;
        }
        else {
            this.numberOfPlayers = numberOfPlayers;
            if (this.lobby.size() >= numberOfPlayers) {
                startGame();
            }
            return true;
        }
    }
    public String getCurrentPlayerName(){
        return game.getCurrentPlayer().getName();
    }

    /**
     * startGame method to start the game. After shuffling the players one player is set in SelectGameCardsState,
     * the other players are set in IdleState
     */
    private Boolean startGame() {
        //add the correct amount of player to the game
        for (int i=0; i<numberOfPlayers; i++){
            game.addPlayer(new Player(lobby.get(i)));
        }

        //notify other players that the game is full
        ArrayList<String> notToBeAddedPlayers = new ArrayList<>();
        for(int i=numberOfPlayers; i<lobby.size(); i++){
            notToBeAddedPlayers.add(lobby.get(i));
        }
        notifyListeners(new gameStartedAndYouAreNotSelectedMessage(notToBeAddedPlayers));


        //the most godLike -> random shuffle will chose the Cards in the game
        if (game.getPlayers().size() > 1) {
            loadCards();
            game.shufflePlayers();
            game.getPlayers().get(0).addPawn(new Pawn("990000",0)); //Red
            game.getPlayers().get(0).addPawn(new Pawn("990000",1)); //Red
            game.getPlayers().get(1).addPawn(new Pawn("000099",2)); //Blue
            game.getPlayers().get(1).addPawn(new Pawn("000099",3)); //Blue
            if (game.getPlayers().size() == 3) {
                game.getPlayers().get(2).addPawn(new Pawn("009900",4)); //Green
                game.getPlayers().get(2).addPawn(new Pawn("009900",5)); //Green
            }
            game.setCurrentPlayer(game.getPlayers().get(0));
            //firstly send the message to the other players to start the game
            notifyListeners(generateGameStart()); //contains the list of players
            //and then send the current player to start choosing the cards
            notifyListeners(generateInGameCardRequest()); //contains the list of players
            return true;
        } else {
            return false;
        }
    }

    /* ------------------------------------------------------------------------------------------------------------------------------------ */








}
