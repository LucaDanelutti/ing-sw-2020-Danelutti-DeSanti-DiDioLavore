package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.SetObservable;

import java.io.Serializable;

public abstract class SetMessage extends Message implements Serializable {
    private static final long serialVersionUID = -131552790272887731L;
    private String nameOfTheSender;

    public String getNameOfTheSender() {
        return nameOfTheSender;
    }

    public void setNameOfTheSender(String nameOfTheSender) {
        this.nameOfTheSender = nameOfTheSender;
    }

    public abstract void accept(SetObservable visitor);
}
