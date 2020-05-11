package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenBlockTypeSetMessage extends SetMessage implements Serializable {
    private static final long serialVersionUID = -7998376907113329492L;

    BlockType blockType;

    public ChosenBlockTypeSetMessage(BlockType blockType) {
        this.blockType=blockType;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
