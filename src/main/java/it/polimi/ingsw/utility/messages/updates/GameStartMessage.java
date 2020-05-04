package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class GameStartMessage extends Message {
    public GameStartMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
