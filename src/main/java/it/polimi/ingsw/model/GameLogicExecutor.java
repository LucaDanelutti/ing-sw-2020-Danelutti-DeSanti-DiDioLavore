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
import it.polimi.ingsw.view.modelview.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

public class GameLogicExecutor extends RequestAndUpdateObservable implements ActionVisitor {
    private Game game;
    private final ArrayList<String> lobby;
    private int numberOfPlayers; //set to -1 by constructor
    private static final int maxX=4;
    private static final int maxY=4;
    private Game beginningOfCurrentAction;
    private Game beginningOfCurrentPlayerTurn;

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
            beginningOfCurrentAction=createGameHardCopy(game);
            notifyListeners(generateChosenPositionForMoveRequest());
        } else {
            executeAction(moveAction);
        }

    }
    public void processAction(ConstructAction constructAction){
        if(constructAction.getChosenPosition()==null){
            beginningOfCurrentAction=createGameHardCopy(game);
            notifyListeners(generateChosenPositionForConstructRequest());
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
            if(constructAction.getNotSelectedPawn()!=null) {
                game.updatePawns(game.getBoard().getPawnCopy(constructAction.getSelectedPawn().getPosition()), game.getBoard().getPawnCopy(constructAction.getNotSelectedPawn().getPosition()));
            }
            else{
                game.updatePawns(game.getBoard().getPawnCopy(constructAction.getSelectedPawn().getPosition()), null);
            }

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
                if(moveAction.getNotSelectedPawn()!=null) {
                    game.updatePawns(game.getBoard().getPawnCopy(newPos), game.getBoard().getPawnCopy(moveAction.getNotSelectedPawn().getPosition()));
                }
                else{
                    game.updatePawns(game.getBoard().getPawnCopy(newPos), null);
                }

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
                if(moveAction.getNotSelectedPawn()!=null) {
                    game.updatePawns(game.getBoard().getPawnCopy(newPos), game.getBoard().getPawnCopy(moveAction.getNotSelectedPawn().getPosition()));
                }
                else{
                    game.updatePawns(game.getBoard().getPawnCopy(newPos), null);
                }

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
                if(moveAction.getNotSelectedPawn()!=null) {
                    game.updatePawns(game.getBoard().getPawnCopy(newPos), game.getBoard().getPawnCopy(moveAction.getNotSelectedPawn().getPosition()));
                }
                else{
                    game.updatePawns(game.getBoard().getPawnCopy(newPos), null);
                }

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
        PlayerView playerView =new PlayerView(player.getName(),pawnViews);
        playerView.setLoser(player.getLoser());
        playerView.setCard(modelCard_to_viewCard(player.getCurrentCard()));
        playerView.setWinner(player.getWinner());
        return playerView;
    }
    private CardView modelCard_to_viewCard(Card card){
        if(card==null){
            return null;
        }
        return new CardView(card.getId(),card.getName(),card.getDescription());
    }

    private ArrayList<CardView>loadedCards_to_viewCards(){
        ArrayList<CardView> availableViewCards = new ArrayList<>();
        for(Card card : game.getLoadedCards()){
            availableViewCards.add(modelCard_to_viewCard(card));
        }
        return availableViewCards;
    }
    private CellView[][] cellMatrix_to_cellViewMatrix(Cell[][] matrix){
        CellView[][] copiedMatrix=new CellView[5][5];
        for(int row=0; row<matrix.length; row++){
            for(int col=0; col<matrix[0].length;col++){
                copiedMatrix[row][col]=matrix[row][col].cell_to_cellView();
            }
        }
        return  copiedMatrix;
    }

    private Boolean isThisPositionInTheAvailableCells(Position toBeChecked){
        return game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy()).contains(toBeChecked);
    }
    private Boolean isThisBlockTypeInTheAvailableBlockTypes(BlockType blockType){
        return game.getCurrentAction().availableBlockTypes(game.getCurrentAction().getChosenPosition(), game.getBoard().getMatrixCopy()).contains(blockType);
    }
    private Boolean isThisSelectedPawnValid(Position selectedPawnPos){
        for(Pawn p : game.getCurrentPlayer().getPawnList()){
            if(p.getPosition().equals(selectedPawnPos)){
                return true;
            }
        }
        return false;
    }
    private Boolean isThisAPlayer(String name){
        for(Player p : game.getPlayers()){
            if(p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    private Boolean areThoseCardIdsDifferent(ArrayList<Integer> cardIds){
        Set<Integer> set = new HashSet<>(cardIds);
        return cardIds.size() <= set.size();
    }
    private Boolean areThoseCardsIdsLegal(ArrayList<Integer> cardIds){
        for(Integer cardId : cardIds){
            if(game.getLoadedCardCopy(cardId)==null){
                return false;
            }
        }
        return true;
    }
    private Boolean isThisALegalPosition(Position pos){
        int x=pos.getX();
        int y=pos.getY();
        return x <= maxX && x >= 0 && y <= maxY && y >= 0;
    }
    private Boolean isThisPositionInTheAvailableOnes(Position chosenPos){
        return game.getBoard().availablePositionsForPawnInitialPlacement().contains(chosenPos);
    }

    private Game createGameHardCopy(Game toBeCopied){
        /*Game hardCopy=new Game();
        //since we call this function when the game is already started,let's be sure about that
        if(toBeCopied.getCurrentPlayer()!=null) {
            //TODO: we have to implement the hard copy of player, board and card
            hardCopy.setLoadedCardsCopy(toBeCopied.getLoadedCards());
            hardCopy.setInGameCardsCopy(toBeCopied.getInGameCards());
            hardCopy.setInGamePlayersAndBoardCopy(toBeCopied);
        }
        return hardCopy;*/

        return new Game(toBeCopied);
    }
    public ModelView createModelViewFromThisGame(Game game){
        ArrayList<PlayerView> playersList=new ArrayList<>();
        for(Player p : game.getPlayers()){
            PlayerView playerView=modelPlayer_to_viewPlayer(p);
            for(PawnView pawnView : playerView.getPawnList()){
                for(Pawn pawn : p.getPawnList()){
                    if(pawnView.getId()==pawn.getId()){
                        pawnView.setPawnPosition(new Position(pawn.getPosition()));
                    }
                }
            }
            playersList.add(playerView);
        }

        CellView[][] matrix=cellMatrix_to_cellViewMatrix(game.getBoard().getMatrixCopy());

        return new ModelView(matrix,playersList);
    }

    /* ------------------------------------------------------------------------------------------------------------------------------------ */



    /* ------------------------------------------------------------------------------------------------------------------------------------ */
                                                //FUNCTIONS THAT GENERATE MESSAGES
    /* ------------------------------------------------------------------------------------------------------------------------------------ */
    private ChosenPositionForMoveRequestMessage generateChosenPositionForMoveRequest(){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        ArrayList<Position> availablePositions = game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        if(game.getCurrentAction().getIsOptional()){
            availablePositions.add(null);
        }
        return new ChosenPositionForMoveRequestMessage(recipients, availablePositions);
    }
    private ChosenPositionForConstructRequestMessage generateChosenPositionForConstructRequest(){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(game.getCurrentPlayer().getName());
        ArrayList<Position> availablePositions = game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        if(game.getCurrentAction().getIsOptional()){
            availablePositions.add(null);
        }
        return new ChosenPositionForConstructRequestMessage(recipients, availablePositions);
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
    private NumberOfPlayersRequestMessage generateNumberOfPlayersRequest(){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(this.lobby.get(0));
        return new NumberOfPlayersRequestMessage(recipients);
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
        return new ChosenCardUpdateMessage(recipients,new CardView(card.getId(),card.getName(),card.getDescription()),game.getCurrentPlayer().getName());
    }
    private UndoUpdateMessage generateUndoUpdate(Game toBeRestored){
        ArrayList<String> recipients = new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        return new UndoUpdateMessage(recipients,createModelViewFromThisGame(toBeRestored));
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
    private GameEndedMessage generateGameEnded(String reason){
        ArrayList<String> recipients=new ArrayList<>();
        for(Player p : game.getPlayers()){
            recipients.add(p.getName());
        }
        return new GameEndedMessage(recipients,reason);
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
     * @return the success of the operation
     */
    public Boolean setSelectedPawn(Position selectedPawnPosition){
        if(isThisSelectedPawnValid(selectedPawnPosition)) {
            //load the first action to be executed
            game.setCurrentAction();
            beginningOfCurrentPlayerTurn=createGameHardCopy(game);

            //aggiorna i selectedPawn e i pawn dentro la currentAction
            Position unselected = null;
            for (Pawn p : game.getCurrentPlayer().getPawnList()) {
                if (!p.getPosition().equals(selectedPawnPosition)) {
                    unselected = p.getPosition();
                }
            }
            if (unselected == null) {
                game.updatePawns(game.getBoard().getPawnCopy(selectedPawnPosition), null);
            } else {
                game.updatePawns(game.getBoard().getPawnCopy(selectedPawnPosition), game.getBoard().getPawnCopy(unselected));
            }
            //notify all the virtualViews of the change in the selected pawn but recipients is composed by only the name of the current player
            notifyListeners(generateSelectedPawnUpdate(selectedPawnPosition));

            //process the action, aka. ask the user for the chosen position for the selected pawn
            game.getCurrentAction().acceptForProcess(); //this will generate the requestMessage for the chosen position

            return true;
        }
        else{
            notifyListeners(generateSelectPawnRequest());
            return false;
        }
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
            else if(isThisPositionInTheAvailableCells(chosenPos)){
                game.getCurrentAction().setChosenPosition(chosenPos);
                game.getCurrentAction().acceptForProcess(); //this will either ask for something else or execute the action
            }
            else{
                game.getCurrentAction().acceptForProcess();
            }
        }
        else{
            if(isThisPositionInTheAvailableCells(chosenPos)){
                game.getCurrentAction().setChosenPosition(chosenPos);
                game.getCurrentAction().acceptForProcess(); //this will either ask for something else or execute the action
            }
            else{
                game.getCurrentAction().acceptForProcess();
            }

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
        if(isThisBlockTypeInTheAvailableBlockTypes(blockType)){
            game.getCurrentAction().blockSelected(blockType);
            game.getCurrentAction().acceptForProcess();
        }
        else{
            game.getCurrentAction().acceptForProcess();
            return false;
        }
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
            updatedCard.setDescription(card.getDescription());
            updatedCardList.add(updatedCard);

        }

        game.setLoadedCardsCopy(updatedCardList);

        return true;
    }
    /**
     * Setup method setFirstPlayer to set the first player that will start to play the game
     */
    public Boolean setStartPlayer(String player){
        if(isThisAPlayer(player)) {
            //this also sets current action to null
            game.setCurrentPlayer(game.getPlayer(player));
            notifyListeners(generateInitialPawnPositionRequest());
            return true;
        }
        else{
           notifyListeners(generateFirstPlayerRequest());
           return false;
        }
    }
    /**
     * Setup method setInGameCards to set the cards that will be available to players
     * Players will choose a card in ChooseCardState
     */
    public Boolean setInGameCards(ArrayList<Integer> cards){
        if(areThoseCardIdsDifferent(cards) && areThoseCardsIdsLegal(cards) && cards.size()==game.getPlayers().size()) {
            ArrayList<Card> inGameCards = new ArrayList<>();
            for (int cardID : cards) {
                inGameCards.add(game.getLoadedCardCopy(cardID));
            }
            game.setInGameCardsCopy(inGameCards);
            Player nextPlayer = game.getNextPlayer();
            game.setCurrentPlayer(nextPlayer);
            notifyListeners(generateChosenCardRequest());
            return true;
        }
        else{
            notifyListeners(generateInGameCardRequest());
            return false;
        }
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
            if (game.getAvailableCards().size() == 0) {
                //All cards are linked to a player
                notifyListeners(generateFirstPlayerRequest());
            } else {
                //next one should select its card
                Player nextPlayer = game.getNextPlayer();
                game.setCurrentPlayer(nextPlayer);
                notifyListeners(generateChosenCardRequest());
            }
            return true;
        }
        else {
            notifyListeners(generateChosenCardRequest());
            return false;
        }
    }
    public Boolean setPawnsPositions(int idWorker1, Position workerPos1, int idWorker2, Position workerPos2){
        if(isThisALegalPosition(workerPos1) && isThisALegalPosition(workerPos2)&&isThisPositionInTheAvailableOnes(workerPos1)&&isThisPositionInTheAvailableOnes(workerPos2)) {
            int indexWorker1 = 0;
            int indexWorker2 = 0;
            for (int i = 0; i < game.getCurrentPlayer().getPawnList().size(); i++) {
                Pawn p = game.getCurrentPlayer().getPawnList().get(i);
                if (p.getId() == idWorker1) {
                    indexWorker1 = i;
                    game.getBoard().setPawnPosition(game.getCurrentPlayer().getPawnList().get(i), workerPos1);
                }
                else if (p.getId() == idWorker2) {
                    indexWorker2 = i;
                    game.getBoard().setPawnPosition(game.getCurrentPlayer().getPawnList().get(i), workerPos2);
                }
                else {
                    return false;
                }
            }
            notifyListeners(generateDoublePawnPositionUpdate(game.getCurrentPlayer().getPawnList().get(indexWorker1).getId(), game.getCurrentPlayer().getPawnList().get(indexWorker2).getId(), workerPos1, workerPos2));
            Player nextPlayer = game.getNextPlayer();
            if (nextPlayer.getPawnList().get(0).getPosition() != null && nextPlayer.getPawnList().get(1).getPosition() != null) {
                //so all players have set their pawns initial position, gameLogic will ask the user to send its selectedPawn
                game.setCurrentPlayer(nextPlayer);
                notifyListeners(generateSelectPawnRequest());
            } else {
                //otherwise i have to ask the next player to set its initial pawn positions
                //this function will also set CurrentAction to null
                game.setCurrentPlayer(nextPlayer);
                notifyListeners(generateInitialPawnPositionRequest());
            }
            return true;
        }
        else{
            notifyListeners(generateInitialPawnPositionRequest());
            return false;
        }
    }
    public Boolean addPlayerToLobby(String name){
        this.lobby.add(name);
        if(this.lobby.size()==1){
            notifyListeners(generateNumberOfPlayersRequest());
        }
        if(this.lobby.size()>=numberOfPlayers && numberOfPlayers>1){
            startGame();
        }
        return true;
    }
    public Boolean setNumberOfPlayers(int numberOfPlayers){
        if(numberOfPlayers<=1 || numberOfPlayers>3){
            notifyListeners(generateNumberOfPlayersRequest());
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
        if(game.getCurrentPlayer()==null)
            return null;
        return game.getCurrentPlayer().getName();
    }
    public Boolean removePlayer(String name){
        //the game is not yet started
        if(game.getPlayers().size()==0){
            //let's find the index of the player to be removed
            int indexToBeRemoved=-1;
            for(int i=0; i<this.lobby.size(); i++){
                if(lobby.get(i).equals(name)){
                    indexToBeRemoved=i;
                }
            }
            switch (indexToBeRemoved){
                case -1:
                    return false;
                case 0:{
                    this.lobby.remove(name);
                    if(lobby.size()>0){
                        //let's send a new number of player request to the new first player
                        this.numberOfPlayers=-1;
                        notifyListeners(generateNumberOfPlayersRequest());
                    }
                }
                default:{
                    //no other action is needed, just remove the player from the lobby
                    this.lobby.remove(name);
                }


            }
        }
        //the game is started, so we have to crash the connection for everyone
        else{
            for(Player p : game.getPlayers()){
                if(p.getName().equals(name)){
                    game.removePlayer(p);
                    break;
                }
            }
            notifyListeners(generateGameEnded("Player disconnection"));
        }
        return true;
    }
    /**
     * startGame method to start the game. After shuffling the players one player is set in SelectGameCardsState,
     * the other players are set in IdleState
     */
    private void startGame() {
        //add the correct amount of player to the game
        for (int i=0; i<numberOfPlayers; i++){
            game.addPlayer(new Player(lobby.get(i)));
        }
        loadCards();
        //the most godLike -> random shuffle will chose the Cards in the game
        game.shufflePlayers();
        game.getPlayers().get(0).addPawn(new Pawn("blue",0));
        game.getPlayers().get(0).addPawn(new Pawn("blue",1));
        game.getPlayers().get(1).addPawn(new Pawn("green",2));
        game.getPlayers().get(1).addPawn(new Pawn("green",3));
        if (game.getPlayers().size() == 3) {
            game.getPlayers().get(2).addPawn(new Pawn("orange",4));
            game.getPlayers().get(2).addPawn(new Pawn("orange",5));
        }
        game.setCurrentPlayer(game.getPlayers().get(0));
        //firstly send the message to the other players to start the game
        notifyListeners(generateGameStart()); //contains the list of players
        //and then send the current player to start choosing the cards
        notifyListeners(generateInGameCardRequest()); //contains the list of players


        //notify other players that the game is full
        ArrayList<String> notToBeAddedPlayers = new ArrayList<>();
        for(int i=numberOfPlayers; i<lobby.size(); i++){
            notToBeAddedPlayers.add(lobby.get(i));
        }
        notifyListeners(new gameStartedAndYouAreNotSelectedMessage(notToBeAddedPlayers));
    }
    /* ------------------------------------------------------------------------------------------------------------------------------------ */

    public Boolean undoCurrentAction(){
        this.game=beginningOfCurrentAction;

        //let's reset the modelView of the clients
        notifyListeners(generateUndoUpdate(this.game));

        //let's ask again the currentPlayer for the ChosenPosition
        notifyListeners(generateChosenPositionForConstructRequest());

        return true;
    }
    public Boolean undoTurn(){
        this.game=beginningOfCurrentPlayerTurn;

        //let's reset the modelView of the clients
        notifyListeners(generateUndoUpdate(this.game));

        //let's ask again the currentPlayer for the selectedPawn
        notifyListeners(generateSelectPawnRequest());

        return true;
    }



    //DEPRECATED SHOULD BE AVOIDED!
    /**
     * Setup method setPawnsPositions to set the two pawns of the current player
     */
    public Boolean setPawnsPositions(ArrayList<Position> positions){
        if (isThisALegalPosition(positions.get(0))&&isThisALegalPosition(positions.get(1))&&isThisPositionInTheAvailableOnes(positions.get(0))&&isThisPositionInTheAvailableOnes(positions.get(1))) {
            game.getBoard().setPawnPosition(game.getCurrentPlayer().getPawnList().get(0), positions.get(0));
            game.getBoard().setPawnPosition(game.getCurrentPlayer().getPawnList().get(1), positions.get(1));
            notifyListeners(generateDoublePawnPositionUpdate(game.getCurrentPlayer().getPawnList().get(0).getId(),game.getCurrentPlayer().getPawnList().get(1).getId(),positions.get(0),positions.get(1)));
            Player nextPlayer = game.getNextPlayer();
            if (nextPlayer.getPawnList().get(0).getPosition() != null && nextPlayer.getPawnList().get(1).getPosition() != null) {
                //so all players have set their pawns initial position, gameLogic will ask the user to send its selectedPawn
                game.setCurrentPlayer(nextPlayer);
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




}
