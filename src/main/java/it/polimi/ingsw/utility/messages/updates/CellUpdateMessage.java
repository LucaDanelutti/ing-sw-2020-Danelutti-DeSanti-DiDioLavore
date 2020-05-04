package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CellView;

import java.util.ArrayList;

public class CellUpdateMessage extends Message {
    CellView cell;
    Position position;

    public CellUpdateMessage(ArrayList<String> recipients, CellView cell, Position position) {
        super(recipients);
        this.cell = cell;
        this.position = position;
    }

    public CellView getCell() {
        return cell;
    }

    public Position getPosition() {
        return position;
    }
}
