package it.polimi.ingsw.model;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import java.util.ArrayList;

/**
 * This class represents the game abstraction. The game contains the player list and the board
 */
public class Game {
    ArrayList<Player> inGamePlayers = new ArrayList<>();
    ArrayList<Card> inGameCards = new ArrayList<>();
    ArrayList<Card> loadedCards = new ArrayList<>();
    Board board;
    Player currentPlayer;
    Action currentAction;


                                                //CONSTRUCTORS

    /**
     * This is the construct for the class Game
     */
    public Game(){
        this.board=new Board();
    }
    public Game(Game toBeCopied){
        if(toBeCopied!=null) {
            if (toBeCopied.getCurrentPlayer() != null) {
                this.setLoadedCardsCopy(toBeCopied.getLoadedCards());
                this.setInGameCardsCopy(toBeCopied.getInGameCards());
                this.setInGamePlayersAndBoardCopy(toBeCopied);

                for(Player p : this.getPlayers()){
                    if(p.getName().equals(toBeCopied.getCurrentPlayer().getName())){
                        this.currentPlayer=p;
                    }
                }
                if(toBeCopied.currentAction==null){
                    this.currentAction=null;
                }
                else {
                    int index=toBeCopied.getCurrentActionIndex();
                    this.currentAction = getCurrentPlayer().getCurrentCard().getCurrentActionList().get(index);
                }
            }
        }
        else{
            this.board=new Board();
        }
    }




                                            //SPECIFIC FUNCTIONS

    /**
     * updatePawns method: updates selectedPawn and unselectedPawn inside this and inside currentAction
     */
    void updatePawns(Pawn selectedPawn, Pawn unselectedPawn) {
        setSelectedPawnCopy(selectedPawn);
        setUnselectedPawnCopy(unselectedPawn);
        if (currentAction != null) {
            if (selectedPawn != null) {
                currentAction.setSelectedPawn(selectedPawn.duplicate());
            } else {
                currentAction.setSelectedPawn(null);
            }
            if (unselectedPawn != null) {
                currentAction.setNotSelectedPawn(unselectedPawn.duplicate());
            } else {
                currentAction.setNotSelectedPawn(null);
            }
        }
    }
    /**
     * This function is responsible to load the new current action automatically
     */
    void setCurrentAction() {
        int index = getCurrentActionIndex() + 1;
        ArrayList<Action>actionList = getCurrentPlayerActionList();
        if (0 <= index & index < actionList.size()) {
            currentAction = actionList.get(index);
            if (getCurrentPlayerSelectedPawn() != null) currentAction.setSelectedPawn(getCurrentPlayerSelectedPawn().duplicate());
            if (getCurrentPlayerUnselectedPawn() != null) currentAction.setNotSelectedPawn(getCurrentPlayerUnselectedPawn().duplicate());
        } else {
            currentAction = null;
        }
    }
    /**
     * addActionAfterCurrentOne method: adds the provided Action after the current one
     */
    void addActionAfterCurrentOne(Action action) {
        int index = getCurrentActionIndex() + 1;
        Action newAction=action.duplicate();
        newAction.setOptional(true);
        currentPlayer.getCurrentCard().getCurrentActionList().add(index, newAction);
    }
    /**
     * addPlayer method to add the provided Player to inGamePlayers
     * @param toAddPlayer is the Player to add
     */
    boolean addPlayer(Player toAddPlayer) {
        if (inGamePlayers.size() == 3) {
            throw new InvalidGameException("Cannot add player to a game that has already 3 players!");
        }
        for (Player player : inGamePlayers) {
            if (player.getName().equals(toAddPlayer.getName())) {
                return false;
            }
        }
        inGamePlayers.add(toAddPlayer);
        return true;
    }
    /**
     * removePlayer method to remove the provided Player from inGamePlayers
     * @param player is the Player to remove
     */
    void removePlayer(Player player) {
        inGamePlayers.remove(player);
    }
    /**
     * shufflePlayers method to shuffle the players in the game
     */
    void shufflePlayers() {
        int x = (int) (Math.random() * ((inGamePlayers.size() - 1) + 1));
        ArrayList<Player> newInGamePlayers = new ArrayList<>();
        newInGamePlayers.add(inGamePlayers.get(x));
        inGamePlayers.remove(x);
        x = (int) (Math.random() * ((inGamePlayers.size() - 1) + 1));
        newInGamePlayers.add(inGamePlayers.get(x));
        inGamePlayers.remove(x);
        if (inGamePlayers.size() > 0) {
            newInGamePlayers.add(inGamePlayers.get(0));
        }
        inGamePlayers = newInGamePlayers;
    }
    /**
     * setFirstPlayer method to set the first player in the game
     */
    void setFirstPlayer(Player player) {
        inGamePlayers.remove(player);
        inGamePlayers.add(0, player);
    }
    /**
     * This function returns the next player to be putted in the current player variable
     * @return the next player
     */
    Player getNextPlayer(){
        int currentPlayerIndex=inGamePlayers.indexOf(currentPlayer);
        int numberOfPlayers=inGamePlayers.size();
        if(currentPlayerIndex==numberOfPlayers-1){
            //we have to restart the index
            return inGamePlayers.get(0);
        }else{
            //just the next one on the list
            return inGamePlayers.get(currentPlayerIndex+1);
        }
    }
    /**
     * This function returns the next non loser player in the game
     * @return the next non loser player
     */
    Player getNextNonLoserPlayer() {
        Boolean isSomeoneNonLoser = areThereAnyNonLoserPlayersLeft();
        if (isSomeoneNonLoser) {
            //c'è qualcuno che effettivamente può essere messo a giocare
            int currentPlayerIndex = inGamePlayers.indexOf(currentPlayer);
            int numberOfPlayers = inGamePlayers.size();
            if (numberOfPlayers == 2) {
                if (currentPlayerIndex == 1) {
                    return inGamePlayers.get(0);
                }
                else if(currentPlayerIndex==0){
                    return inGamePlayers.get(1);
                }
            }
            else {
                if (currentPlayerIndex == 0) {
                    if (inGamePlayers.get(1).getLoser()) {
                        //se il prossimo è perdente ne rimane solo uno da poter mettere a giocare
                        return inGamePlayers.get(2);
                    } else {
                        return inGamePlayers.get(1);
                    }
                }
                else if (currentPlayerIndex == 1) {
                    if (inGamePlayers.get(2).getLoser()) {
                        //se il prossimo è perdente ne rimane solo uno da poter mettere a giocare
                        return inGamePlayers.get(0);
                    } else {
                        return inGamePlayers.get(2);
                    }
                }
                else if (currentPlayerIndex == 2) {
                    if (inGamePlayers.get(0).getLoser()) {
                        //se il prossimo è perdente ne rimane solo uno da poter mettere a giocare
                        return inGamePlayers.get(1);
                    } else {
                        return inGamePlayers.get(0);
                    }
                }
            }
        }
        else {
            //sono tutti perdenti!
            return null;
        }

        return null;
    }
    /**
     * This function returns true if there is at minimum one player to be putted in the current player variable
     * @return the result of the query
     */
    Boolean areThereAnyNonLoserPlayersLeft(){
        boolean isSomeoneNonLoser=false;
        for(Player p : inGamePlayers){
            if(!p.getName().equals(currentPlayer.getName())){
                if(!p.getLoser()){
                    isSomeoneNonLoser=true;
                }
            }
        }
        return isSomeoneNonLoser;
    }
    /**
     * This function removes all the pawns for the selected player
     * @param nickname the selected player
     */
    void removePlayerPawns(String nickname){
        for(Player player : this.inGamePlayers){
            if(player.getName().equals(nickname)){
                for(Pawn pawn : player.getPawnList()){
                    this.board.removePawnFromGame(pawn);
                }
                player.removeAllPawns();
            }
        }
    }







                                                    //GETTERS

    Action getCurrentAction(){
        return this.currentAction;
    }
    Player getCurrentPlayer(){
        return this.currentPlayer;
    }
    ArrayList<Action> getCurrentPlayerActionList(){
        return this.currentPlayer.getCurrentCard().getCurrentActionList();
    }
    /**
     * Get method of the variable unselectedPawn, returns a copy of the unselectedPawn
     */
    Pawn getUnselectedPawnCopy() {
        return currentPlayer.getUnselectedPawn().duplicate();
    }
    /**
     * Get method of the variable selectedPawn, returns a copy of the selectedPawn
     */
    Pawn getSelectedPawnCopy() {
        return currentPlayer.getSelectedPawn().duplicate();
    }
    /**
     * This is the getter for the variable board
     * @return the board
     */
    Board getBoard() {
        return board;
    }
    /**
     * This is the getter for the variable inGamePlayer
     * @return the variable inGamePlayers
     */
    ArrayList<Player> getPlayers() {
        return inGamePlayers;
    }
    /**
     * getPlayer method to get the Player identified by the provided name
     * @param name is the name of the player to return
     */
    Player getPlayer(String name) {
        for (Player tempPlayer : inGamePlayers) {
            if (tempPlayer.getName().equals(name)) {
                return tempPlayer;
            }
        }
        return null;
    }
    /**
     * Get method of the variable loadedCards, returns the reference of loadedCards
     */
    ArrayList<Card> getLoadedCards(){
        return this.loadedCards;
    }
    /**
     * Get method of the variable inGameCards, returns the reference of inGameCards
     */
    ArrayList<Card> getInGameCards(){
        return this.inGameCards;
    }
    /**
     * getLoadedCard method: returns a copy of the loaded card with id equals to cardID
     */
    Card getLoadedCardCopy(int cardID){
        for (Card card : loadedCards) {
            if (card.getId() == cardID)
                return new Card(card);
        }
        return null;
    }
    /**
     * getInGameCardCopy method: returns a copy of the in game card with id equals to cardID
     */
    Card getInGameCardCopy(int cardID){
        for (Card card : inGameCards) {
            if (card.getId() == cardID)
                return new Card(card);
        }
        return null;
    }
    /**
     * getAvailableCards method: returns the available cards (not linked to a player)
     */
    ArrayList<Card> getAvailableCards(){
        ArrayList<Card> availableCards = new ArrayList<>(this.inGameCards);
        for(Player p : this.inGamePlayers){
            if(p.getCurrentCard()!=null){
                availableCards.removeIf(i -> i.equals(p.getCurrentCard()));
            }
        }
        return availableCards;
    }







                                                    //SETTERS

    /**
     * This is the setter for the current player
     * @param currentPlayer the player to set as current
     */
    void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.currentAction=null;
    }
    /**
     * Set method of the variable loadedCards, sets a copy of provided cards
     */
    void setLoadedCardsCopy(ArrayList<Card> cards){
        if (cards != null) {
            for (Card card : cards) {
                this.loadedCards.add(new Card(card));
            }
        } else {
            throw new InvalidGameException("Invalid cards array!");
        }
    }
    /**
     * Set method of the variable inGameCards, sets a copy of provided cards
     */
    void setInGameCardsCopy(ArrayList<Card> cards){
        if (cards != null) {
            for (Card card : cards) {
                this.inGameCards.add(new Card(card));
            }
        } else {
            throw new InvalidGameException("Invalid cards array!");
        }
    }






                                                //COMMODITY FUNCTIONS

    /**
     * This function returns the selected pawn for the current player
     * @return the selected pawn
     */
    private Pawn getCurrentPlayerSelectedPawn(){
        if(currentPlayer==null)
            return null;
        return this.currentPlayer.getSelectedPawn();
    }
    /**
     * This function returns the unselected pawn for the current player
     * @return the unselected pawn
     */
    private Pawn getCurrentPlayerUnselectedPawn(){
        if(currentPlayer==null)
            return null;
        return this.currentPlayer.getUnselectedPawn();
    }
    /**
     * Set method of the variable unselectedPawn, sets a copy of provided unselectedPawn
     */
    private void setUnselectedPawnCopy(Pawn unselectedPawn) {
        if (unselectedPawn != null) {
            currentPlayer.setUnselectedPawnPosition(unselectedPawn.duplicate());
        } else {
            currentPlayer.setUnselectedPawnPosition(null);
        }
    }
    /**
     * Set method of the variable selectedPawn, sets a copy of provided selectedPawn
     */
    private void setSelectedPawnCopy(Pawn selectedPawn) {
        if (selectedPawn != null) {
            currentPlayer.setSelectedPawnPosition(selectedPawn.duplicate());
        } else {
            currentPlayer.setSelectedPawnPosition(null);
        }
    }
    /**
     * This function returns the index of the current action inside of the current action list in the current player card
     * @return the index
     */
    private int getCurrentActionIndex() {
        int index = -1;
        ArrayList<Action> actionList = getCurrentPlayerActionList();
        if (currentAction != null) {
            for (int i = 0; i < actionList.size(); i++) {
                if (actionList.get(i) == currentAction) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                throw new InvalidGameException("Invalid actionState!");
            }
        }
        return index;
    }
    /**
     * This function sets the in game players and the board to the ones of the game to be copied
     * @param toBeCopied the game to be copied
     */
    private void setInGamePlayersAndBoardCopy(Game toBeCopied){
        this.inGamePlayers=new ArrayList<>();
        for(Player p: toBeCopied.getPlayers()){
            this.inGamePlayers.add(new Player(p));
        }
        this.setBoardCopy(toBeCopied);
    }
    /**
     * This function sets the board to the one inside of the game passed as argument
     * @param toBeCopied the game from which to take the board
     */
    private void setBoardCopy(Game toBeCopied){
        this.board=toBeCopied.getBoard().getBoardCopyWithoutPawns();
        for(Player p : this.getPlayers()){
            for(Pawn pawn : p.getPawnList()){
                this.board.placePawn(pawn,pawn.getPosition());
            }
        }
    }

}
