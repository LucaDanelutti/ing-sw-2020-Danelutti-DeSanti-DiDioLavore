package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class ChosenBlockTypeRequestMessage extends Message {
    ArrayList<BlockType> availableBlockTypes=new ArrayList<>();

    public ChosenBlockTypeRequestMessage(ArrayList<String> recipients, ArrayList<BlockType> availableBlockTypes) {
        super(recipients);
        this.availableBlockTypes.addAll(availableBlockTypes);
    }
}
