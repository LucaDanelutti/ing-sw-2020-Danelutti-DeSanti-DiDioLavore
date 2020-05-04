package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class ChosenBlockTypeSetMessage extends Message {
    BlockType blockType;

    public ChosenBlockTypeSetMessage(ArrayList<String> recipients, BlockType blockType) {
        super(recipients);
        this.blockType=blockType;
    }
}
