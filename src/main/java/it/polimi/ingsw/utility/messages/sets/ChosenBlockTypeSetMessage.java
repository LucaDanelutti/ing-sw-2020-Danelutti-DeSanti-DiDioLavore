package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenBlockTypeSetMessage extends Message implements Serializable {
    private static final long serialVersionUID = -7998376907113329492L;

    BlockType blockType;

    public ChosenBlockTypeSetMessage(ArrayList<String> recipients, BlockType blockType) {
        super(recipients);
        this.blockType=blockType;
    }
}
