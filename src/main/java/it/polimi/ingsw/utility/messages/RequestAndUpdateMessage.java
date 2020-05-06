package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.RequestAndUpdateObservable;

import java.util.ArrayList;

public abstract class RequestAndUpdateMessage extends Message{
    public RequestAndUpdateMessage(ArrayList<String> recipients) {
        super(recipients);
    }

    public abstract void accept(RequestAndUpdateObservable visitor);
}
