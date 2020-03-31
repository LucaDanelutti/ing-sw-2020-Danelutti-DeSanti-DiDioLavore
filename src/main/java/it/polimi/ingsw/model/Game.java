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
        if (inGamePlayers.size() == 2) {
            for (int i=0; i<2; i++) {
                if (inGamePlayers.get(i%2).getState().getType() == PlayerStateType.ActionState) {
                    if (inGamePlayers.get((i+1)%2).getState().getType() == PlayerStateType.IdleState) {
                        return inGamePlayers.get((i+1)%2);
                    } else {
                        return new Player("TODO", new WaitingOtherPlayersState()); //TODO: Exception
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
                        return new Player("TODO", new WaitingOtherPlayersState()); //TODO: Exception
                    }
                }
            }
        }
        return new Player("TODO", new WaitingOtherPlayersState()); //TODO: Exception
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
