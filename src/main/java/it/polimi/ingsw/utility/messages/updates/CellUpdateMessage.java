package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.view.modelview.CellView;

import java.io.Serializable;
import java.util.ArrayList;

public class CellUpdateMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -7436329154575487370L;

    BlockType blockType;
    Position position;

    public CellUpdateMessage(ArrayList<String> recipients, BlockType blockType, Position position) {
        super(recipients);
        this.blockType = blockType;
        this.position = position;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public Position getPosition() {
        return position;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
