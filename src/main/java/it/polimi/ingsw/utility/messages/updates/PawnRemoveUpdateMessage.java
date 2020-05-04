package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class PawnRemoveUpdateMessage extends Message {
    int workerId;

    public PawnRemoveUpdateMessage(ArrayList<String> recipients, int workerId) {
        super(recipients);
        this.workerId= workerId;
    }

    public int getWorkerId() {
        return workerId;
    }
}
