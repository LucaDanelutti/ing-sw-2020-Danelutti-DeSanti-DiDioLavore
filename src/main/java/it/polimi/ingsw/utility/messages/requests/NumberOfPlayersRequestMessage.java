package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class NumberOfPlayersRequestMessage extends Message {

    public NumberOfPlayersRequestMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
