package it.polimi.ingsw.utility.messages;

import java.util.ArrayList;

public class GameStartMessage extends Message {
    public GameStartMessage(ArrayList<String> recipients) {
        super(recipients);
    }
}
