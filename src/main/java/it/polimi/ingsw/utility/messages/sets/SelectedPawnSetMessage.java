package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectedPawnSetMessage extends Message implements Serializable {
    private static final long serialVersionUID = 2301093241631675811L;

    Position workerPos;

    public SelectedPawnSetMessage(ArrayList<String> recipients, Position workerPos) {
        super(recipients);
        this.workerPos=workerPos;
    }
}
