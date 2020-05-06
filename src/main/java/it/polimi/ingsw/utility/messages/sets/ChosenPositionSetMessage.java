package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenPositionSetMessage extends SetMessage implements Serializable {
    private static final long serialVersionUID = 253005137274615202L;

    Position workerPos;

    public ChosenPositionSetMessage(ArrayList<String> recipients, Position workerPos) {
        super(recipients);
        this.workerPos=workerPos;
    }

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
