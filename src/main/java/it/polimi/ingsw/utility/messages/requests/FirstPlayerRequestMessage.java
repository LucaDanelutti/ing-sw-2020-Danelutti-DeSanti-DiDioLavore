package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class FirstPlayerRequestMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = 8152230286677416998L;

    public FirstPlayerRequestMessage(ArrayList<String> recipients) {
        super(recipients);
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
