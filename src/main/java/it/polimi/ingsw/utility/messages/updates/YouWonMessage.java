package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class YouWonMessage extends Message implements Serializable {
    private static final long serialVersionUID = -4660471843196967503L;

    public YouWonMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
