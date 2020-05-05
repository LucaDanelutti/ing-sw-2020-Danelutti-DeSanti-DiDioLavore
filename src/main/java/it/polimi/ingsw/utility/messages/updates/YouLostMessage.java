package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class YouLostMessage extends Message implements Serializable {
    private static final long serialVersionUID = -3553708908732148091L;

    String loserName;

    public YouLostMessage(ArrayList<String> recipients,String loserName) {
        super(recipients);
        this.loserName=loserName;
    }

    public String getLoserName() {
        return loserName;
    }
}
