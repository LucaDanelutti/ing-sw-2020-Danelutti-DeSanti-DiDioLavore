package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class FirstPlayerSetMessage extends SetMessage implements Serializable {
    private static final long serialVersionUID = 845291205162850025L;

    String name;

    public FirstPlayerSetMessage(String name) {
        this.name=name;
    }

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
