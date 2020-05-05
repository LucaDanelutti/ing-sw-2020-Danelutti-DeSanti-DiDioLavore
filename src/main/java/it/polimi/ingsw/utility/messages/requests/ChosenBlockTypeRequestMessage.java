package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenBlockTypeRequestMessage extends Message implements Serializable {
    private static final long serialVersionUID = -363983287119219876L;

    ArrayList<BlockType> availableBlockTypes=new ArrayList<>();

    public ChosenBlockTypeRequestMessage(ArrayList<String> recipients, ArrayList<BlockType> availableBlockTypes) {
        super(recipients);
        this.availableBlockTypes.addAll(availableBlockTypes);
    }
}
