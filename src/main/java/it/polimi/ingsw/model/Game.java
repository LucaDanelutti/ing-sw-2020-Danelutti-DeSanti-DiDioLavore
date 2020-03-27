package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;

public class Game {
    Board board;
    //TODO: add playerList variable (with getPlayerList, addPlayer, removePlayer) and modify construct accordingly

    Game(){
        this.board=new Board();
    }
    public Board getBoard() {
        return board;
    }

}
