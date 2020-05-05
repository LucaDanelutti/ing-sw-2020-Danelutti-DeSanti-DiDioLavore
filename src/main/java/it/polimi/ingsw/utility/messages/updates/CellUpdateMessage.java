package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CellView;

import java.io.Serializable;
import java.util.ArrayList;

public class CellUpdateMessage extends Message implements Serializable {
    private static final long serialVersionUID = -7436329154575487370L;

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
