package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class TurnEndedMessage extends Message implements Serializable {
    private static final long serialVersionUID = 601643023411444716L;

    public TurnEndedMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
