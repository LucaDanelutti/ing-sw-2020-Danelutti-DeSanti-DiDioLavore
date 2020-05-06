package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class NicknameRequestMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -7188969061312168563L;

    public NicknameRequestMessage(ArrayList<String> recipients) {
        super(recipients);
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
