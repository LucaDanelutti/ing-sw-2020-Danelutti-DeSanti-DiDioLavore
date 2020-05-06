package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class TurnEndedMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = 601643023411444716L;

    public TurnEndedMessage(ArrayList<String> recipients) {
        super(recipients);
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
