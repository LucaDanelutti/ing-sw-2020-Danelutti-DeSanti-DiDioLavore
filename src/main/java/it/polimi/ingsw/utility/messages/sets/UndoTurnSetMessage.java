package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;

public class UndoTurnSetMessage extends SetMessage implements Serializable {

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
