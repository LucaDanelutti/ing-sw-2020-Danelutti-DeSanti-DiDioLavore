package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class YouLostMessage extends Message {
    String loserName;

    public YouLostMessage(ArrayList<String> recipients,String loserName) {
        super(recipients);
        this.loserName=loserName;
    }


    public String getLoserName() {
        return loserName;
    }


}
