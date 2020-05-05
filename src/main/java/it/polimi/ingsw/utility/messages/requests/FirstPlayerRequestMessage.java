package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class FirstPlayerRequestMessage extends Message implements Serializable {
    private static final long serialVersionUID = 8152230286677416998L;

    public FirstPlayerRequestMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
