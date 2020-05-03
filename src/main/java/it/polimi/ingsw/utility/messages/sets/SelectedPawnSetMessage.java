package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class SelectedPawnSetMessage extends Message {
    Position workerPos;

    public SelectedPawnSetMessage(ArrayList<String> recipients, Position workerPos) {
        super(recipients);
        this.workerPos=workerPos;
    }
}
