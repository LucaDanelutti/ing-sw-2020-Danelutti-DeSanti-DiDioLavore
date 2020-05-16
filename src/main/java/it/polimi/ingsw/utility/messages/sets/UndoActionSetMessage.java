package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;

public class UndoActionSetMessage extends SetMessage implements Serializable {
    private static final long serialVersionUID = -850191341052996992L;

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
