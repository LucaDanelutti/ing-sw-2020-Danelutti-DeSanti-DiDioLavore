package it.polimi.ingsw.view.listeners.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Cell;

public interface CellUpdateListener {
    public void onCellUpdate(Position cellPosition, Cell changedCell);
}
