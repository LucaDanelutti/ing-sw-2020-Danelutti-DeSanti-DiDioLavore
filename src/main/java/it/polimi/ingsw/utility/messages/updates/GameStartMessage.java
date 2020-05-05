package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStartMessage extends Message implements Serializable {
    private static final long serialVersionUID = 797836970633075160L;

    public GameStartMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
