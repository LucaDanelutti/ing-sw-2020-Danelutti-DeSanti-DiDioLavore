package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class GameEndedMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -5296998152404109286L;

    String reason;

    public GameEndedMessage(ArrayList<String> recipients, String reason) {
        super(recipients);
        this.reason=reason;
    }

    public String getReason() {
        return reason;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
