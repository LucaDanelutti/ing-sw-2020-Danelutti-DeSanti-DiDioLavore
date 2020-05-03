package it.polimi.ingsw.view.listeners.updates;

import it.polimi.ingsw.model.Position;

public interface DoublePawnPositionUpdateListener {
    public void onDoublePawnPositionUpdate(Integer workerId1, Position workerPos1, Integer workerId2, Position workerPos2);
}
