package it.polimi.ingsw.utility.messages;

import java.util.ArrayList;

public class TurnEndedMessage extends Message{

    public TurnEndedMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
