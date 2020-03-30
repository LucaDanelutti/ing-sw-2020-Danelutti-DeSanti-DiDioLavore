package it.polimi.ingsw.model;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.playerstate.PlayerStateType;
import it.polimi.ingsw.model.playerstate.WaitingOtherPlayersState;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents the game abstraction. The game contains the player list and the board
 */
class Game {
    ArrayList<Player> inGamePlayers = new ArrayList<Player>();
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
     * getNextCurrentPlayer method to get the Player that is going to be in ActionState
     */
    Player getNextActionStatePlayer() {
        Iterator<Player> iterator = inGamePlayers.iterator();
        while (iterator.hasNext()) {
            Player tempPlayer = iterator.next();
            if (tempPlayer.getState().getType() == PlayerStateType.ActionState) {
                if (iterator.hasNext()) {
                    return iterator.next(); //Return the next player in the arrayList
                }
                else {
                    return inGamePlayers.iterator().next(); //Return the first player of the arrayList
                }
            }
        }
        return new Player("TODO", new WaitingOtherPlayersState()); //TODO: GameNotStartedException
    }

    /**
     * getNextCurrentPlayer method to get the Players in the provided playerState
     */
    ArrayList<Player> getPlayersIn(PlayerStateType playerState) {
        ArrayList<Player> tempPlayerList = new ArrayList<Player>();
        for (Player player : inGamePlayers) {
            if (player.getState().getType() == playerState) {
                tempPlayerList.add(player);
            }
        }
        return tempPlayerList;
    }

}
