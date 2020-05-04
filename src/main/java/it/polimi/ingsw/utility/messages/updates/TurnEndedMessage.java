package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class TurnEndedMessage extends Message {

    public TurnEndedMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
