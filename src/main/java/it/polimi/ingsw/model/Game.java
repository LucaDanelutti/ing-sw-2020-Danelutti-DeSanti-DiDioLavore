package it.polimi.ingsw.model;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.playerstate.InvalidGameException;
import it.polimi.ingsw.model.playerstate.PlayerStateType;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents the game abstraction. The game contains the player list and the board
 */
class Game {
    ArrayList<Player> inGamePlayers = new ArrayList<>();
    ArrayList<Card> inGameCards = new ArrayList<>();
    ArrayList<Card> loadedCards = new ArrayList<>();
    Board board;

    //=================================================================================
    //NEW THINGS
    Player currentPlayer;
    Action currentAction;

    public Action getCurrentAction(){
        return this.currentAction;
    }

    public ArrayList<Action> getCurrentPlayerActionList(){
        return this.currentPlayer.getCurrentCard().getCurrentActionList();
    }
    private Pawn getCurrentPlayerSelectedPawn(){
        return this.currentPlayer.getSelectedPawn();
    }

/*
    public void setCurrentAction() {
        int index = getCurrentActionIndex() + 1;
        ArrayList<Action>actionList = getCurrentPlayerActionList();
        if (0 <= index & index < actionList.size()) {
            currentAction = actionList.get(index);
            if (selectedPawn != null) currentAction.setSelectedPawn(selectedPawn.duplicate());
            if (unselectedPawn != null) currentAction.setNotSelectedPawn(unselectedPawn.duplicate());
        } else {
            currentAction = null;
        }
    }
*/

    private int getCurrentActionIndex() {
        int index = -1;
        ArrayList<Action>actionList = getCurrentPlayerActionList();
        if (currentAction == null) {
            index = -1;
        } else {
            for (int i=0; i < actionList.size(); i++) {
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
    //===================================================================================


    /**
     * This is the construct for the class Game
     */
    Game(){
        this.board=new Board();
    }

    /**
     * This is the getter for the variable board
     * @return the board
     */
    Board getBoard() {
        return board;
    }

    /**
     * This is the setter for the variable board
     * @param board the board to be set
     */
    void setBoard(Board board) {
        this.board = board;
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
       Iterator<Player> iterator = inGamePlayers.iterator();
       while (iterator.hasNext()) {
           Player tempPlayer = iterator.next();
           if (tempPlayer.getName().equals(name)) {
               return tempPlayer;
           }
       }
       return null;
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
            if (player.getName() == toAddPlayer.getName()) {
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
        int x = (int)(Math.random()*((inGamePlayers.size()-1-0)+1))+0;
        ArrayList<Player> newInGamePlayers = new ArrayList<>();
        newInGamePlayers.add(inGamePlayers.get(x));
        inGamePlayers.remove(x);
        x = (int)(Math.random()*((inGamePlayers.size()-1-0)+1))+0;
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
        int index = inGamePlayers.indexOf(player);
        inGamePlayers.remove(index);
        inGamePlayers.add(0, player);
    }

    /**
     * getNextPlayer method to get the Player that is going to be in an active state.
     * Throws InvalidGameException if no next player is available.
     */
    Player getNextPlayer(PlayerStateType playerStateType) {
        if (inGamePlayers.size() == 2) {
            for (int i=0; i<2; i++) {
                if (inGamePlayers.get(i%2).getState().getType() == playerStateType) {
                    if (inGamePlayers.get((i+1)%2).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+1)%2);
                    } else {
                        return null;
                    }
                }
            }
        }
        else if (inGamePlayers.size() == 3) {
            for (int i=0; i<3; i++) {
                if (inGamePlayers.get(i%3).getState().getType() == playerStateType) {
                    if (inGamePlayers.get((i+1)%3).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+1)%3);
                    } else if (inGamePlayers.get((i+2)%3).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+2)%3);
                    } else {
                        return null;
                    }
                }
            }
        }
        throw new InvalidGameException("A started game must have 2 or 3 players!");
    }

    /**
     * getPlayersIn method to get the Players in the provided playerState. If there are no
     * players in the provided playerStateType returns an empty arrayList. If more than one player
     * is found in actionState throws an exception
     */
    ArrayList<Player> getPlayersIn(PlayerStateType playerState) {
        ArrayList<Player> tempPlayerList = new ArrayList<>();
        for (Player player : inGamePlayers) {
            if (player.getState().getType() == playerState) {
                tempPlayerList.add(player);
            }
        }
        if (playerState == PlayerStateType.ActionState && tempPlayerList.size() != 1) {
            throw new InvalidGameException("No player/more than one player in ActionState found");
        }
        return tempPlayerList;
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
     * Get method of the variable loadedCards, returns the reference of loadedCards
     */
    ArrayList<Card> getLoadedCards(){
        return this.loadedCards;
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

}
