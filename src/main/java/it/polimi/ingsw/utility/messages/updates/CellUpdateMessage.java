package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class CellUpdateMessage extends Message {
    Cell cell;
    Position position;

    public CellUpdateMessage(ArrayList<String> recipients, Cell cell, Position position) {
        super(recipients);
        this.cell = cell;
        this.position = position;
    }
}
