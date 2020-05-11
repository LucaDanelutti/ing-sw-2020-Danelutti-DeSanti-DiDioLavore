package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectedPawnSetMessage extends SetMessage implements Serializable {
    private static final long serialVersionUID = 2301093241631675811L;

    Position workerPos;

    public SelectedPawnSetMessage(Position workerPos) {
        this.workerPos=workerPos;
    }

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
