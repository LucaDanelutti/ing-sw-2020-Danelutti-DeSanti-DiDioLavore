package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class FirstPlayerSetMessage extends Message implements Serializable {
    private static final long serialVersionUID = 845291205162850025L;

    String name;

    public FirstPlayerSetMessage(ArrayList<String> recipients, String name) {
        super(recipients);
        this.name=name;
    }
}
