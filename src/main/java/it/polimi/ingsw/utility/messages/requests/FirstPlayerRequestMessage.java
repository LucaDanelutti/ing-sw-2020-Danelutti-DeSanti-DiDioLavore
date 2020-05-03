package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class FirstPlayerRequestMessage extends Message {

    public FirstPlayerRequestMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
