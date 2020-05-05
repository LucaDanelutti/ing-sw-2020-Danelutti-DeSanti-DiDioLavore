package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class DoublePawnPositionUpdateMessage extends Message implements Serializable {
    private static final long serialVersionUID = -9037003976306087363L;

    int workerId1, workerId2;
    Position workerPos1,workerPos2;


    public DoublePawnPositionUpdateMessage(ArrayList<String> recipients, int workerId1, int workerId2, Position workerPos1, Position workerPos2) {
        super(recipients);
        this.workerId1 = workerId1;
        this.workerId2 = workerId2;
        this.workerPos1 = workerPos1;
        this.workerPos2 = workerPos2;
    }

    public int getWorkerId1() {
        return workerId1;
    }

    public int getWorkerId2() {
        return workerId2;
    }

    public Position getWorkerPos1() {
        return workerPos1;
    }

    public Position getWorkerPos2() {
        return workerPos2;
    }
}
