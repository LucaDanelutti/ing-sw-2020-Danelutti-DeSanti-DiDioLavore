package it.polimi.ingsw.model;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.playerstate.InvalidGameException;
import it.polimi.ingsw.model.playerstate.PlayerStateType;
import it.polimi.ingsw.model.playerstate.WaitingOtherPlayersState;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents the game abstraction. The game contains the player list and the board
 */
class Game {
    ArrayList<Player> inGamePlayers = new ArrayList<Player>();
    ArrayList<Card> inGameCards=new ArrayList<>();

    Board board;

    Game(){
        this.board=new Board();
    }

    Board getBoard() {
        return board;
    }

    void setBoard(Board board) {
        this.board = board;
    }

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
       return new Player("TODO", new WaitingOtherPlayersState()); //TODO: PlayerNotFoundException
   }


    /**
     * addPlayer method to add the provided Player to inGamePlayers
     * @param player is the Player to add
     */
    void addPlayer(Player player) {
        inGamePlayers.add(player);
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
     * getNextCurrentPlayer method to get the Player that is going to be in ActionState.
     * Throws InvalidGameException if no next ActionStatePlayer is available.
     */
    Player getNextActionStatePlayer() {
        if (inGamePlayers.size() == 2) {
            for (int i=0; i<2; i++) {
                if (inGamePlayers.get(i%2).getState().getType() == PlayerStateType.ActionState) {
                    if (inGamePlayers.get((i+1)%2).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+1)%2);
                    } else {
                        throw new InvalidGameException("Next ActionStatePlayer not available");
                    }
                }
            }
        } else if (inGamePlayers.size() == 3) {
            for (int i=0; i<3; i++) {
                if (inGamePlayers.get(i%3).getState().getType() == PlayerStateType.ActionState) {
                    if (inGamePlayers.get((i+1)%3).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+1)%3);
                    } else if (inGamePlayers.get((i+2)%3).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+2)%3);
                    } else {
                        throw new InvalidGameException("Next ActionStatePlayer not available");
                    }
                }
            }
        }
        throw new InvalidGameException("Next ActionStatePlayer not available");
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
                        throw new InvalidGameException("Next player not available");
                    }
                }
            }
        } else if (inGamePlayers.size() == 3) {
            for (int i=0; i<3; i++) {
                if (inGamePlayers.get(i%3).getState().getType() == playerStateType) {
                    if (inGamePlayers.get((i+1)%3).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+1)%3);
                    } else if (inGamePlayers.get((i+2)%3).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+2)%3);
                    } else {
                        throw new InvalidGameException("Next player not available");
                    }
                }
            }
        }
        throw new InvalidGameException("Next player not available");
    }

    /**
     * getNextCurrentPlayer method to get the Players in the provided playerState. If there are no
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

    void setInGameCards(ArrayList<Card> cards){
        this.inGameCards=cards;
    }
    ArrayList<Card> getInGameCards(){
        return this.inGameCards;
    }
    ArrayList<Card> getAvailableCards(){
        ArrayList<Card> availableCards=new ArrayList<>(this.inGameCards);
        for(Player p : this.inGamePlayers){
            if(p.getCurrentCard()!=null){
                availableCards.removeIf(i -> i.getId() == p.getCurrentCard().getId());
            }
        }
        return availableCards;
    }

}
