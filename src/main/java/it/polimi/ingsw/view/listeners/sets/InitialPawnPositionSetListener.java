package it.polimi.ingsw.view.listeners.sets;

import it.polimi.ingsw.model.Position;

public interface InitialPawnPositionSetListener {
    public void onInitialPawnPositionSet(Integer workerId1, Position workerPos1, Integer workerId2, Position workerPos2);
}
