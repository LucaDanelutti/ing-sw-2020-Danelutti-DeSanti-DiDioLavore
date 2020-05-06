package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.SetObservable;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class SetMessage extends Message implements Serializable {
    private static final long serialVersionUID = -131552790272887731L;

    public SetMessage(ArrayList<String> recipients) {
        super(recipients);
    }

    public abstract void accept(SetObservable visitor);
}
