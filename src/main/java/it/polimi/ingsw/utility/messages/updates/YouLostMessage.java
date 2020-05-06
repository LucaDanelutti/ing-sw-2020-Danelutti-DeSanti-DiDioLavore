package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class YouLostMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -3553708908732148091L;

    String loserName;

    public YouLostMessage(ArrayList<String> recipients,String loserName) {
        super(recipients);
        this.loserName=loserName;
    }

    public String getLoserName() {
        return loserName;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
