package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class FirstPlayerSetMessage extends Message {
    String name;
    public FirstPlayerSetMessage(ArrayList<String> recipients, String name) {
        super(recipients);
        this.name=name;
    }
}
