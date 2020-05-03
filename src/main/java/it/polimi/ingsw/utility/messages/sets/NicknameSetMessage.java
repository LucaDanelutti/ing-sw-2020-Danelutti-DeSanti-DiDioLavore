package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class NicknameSetMessage extends Message {
    String name;
    public NicknameSetMessage(ArrayList<String> recipients,String name) {
        super(recipients);
        this.name=name;
    }
}
