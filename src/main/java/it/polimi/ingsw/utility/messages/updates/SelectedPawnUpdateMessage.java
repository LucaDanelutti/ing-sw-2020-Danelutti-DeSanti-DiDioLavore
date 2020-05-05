package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectedPawnUpdateMessage extends Message implements Serializable {
    private static final long serialVersionUID = 1027863088961334542L;

    int workerId;

    public SelectedPawnUpdateMessage(ArrayList<String> recipients, int workerId) {
        super(recipients);
        this.workerId= workerId;
    }

    public int getWorkerId() {
        return workerId;
    }
}
