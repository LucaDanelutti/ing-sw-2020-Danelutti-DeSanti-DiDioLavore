package it.polimi.ingsw.view.listeners.updates;

import it.polimi.ingsw.model.Position;

public interface PawnPositionUpdateListener {
    public void onPawnPositionUpdate(Integer workerId, Position workerPos);
}
