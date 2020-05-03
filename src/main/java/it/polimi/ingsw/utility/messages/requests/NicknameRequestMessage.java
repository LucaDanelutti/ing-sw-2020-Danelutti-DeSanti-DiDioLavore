package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class NicknameRequestMessage extends Message {

    public NicknameRequestMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
