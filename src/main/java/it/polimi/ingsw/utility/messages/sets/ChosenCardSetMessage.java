package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class ChosenCardSetMessage extends Message {
    int cardId;
    public ChosenCardSetMessage(ArrayList<String> recipients, int cardId) {
        super(recipients);
        this.cardId=cardId;
    }
}
