package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.SetObservable;

import java.util.ArrayList;

public abstract class SetMessage extends Message{
    public SetMessage(ArrayList<String> recipients) {
        super(recipients);
    }

    public abstract void accept(SetObservable visitor);
}
