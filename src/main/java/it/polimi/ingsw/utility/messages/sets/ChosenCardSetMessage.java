package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenCardSetMessage extends Message implements Serializable {
    int cardId;
    public ChosenCardSetMessage(ArrayList<String> recipients, int cardId) {
        super(recipients);
        this.cardId=cardId;
    }
}
