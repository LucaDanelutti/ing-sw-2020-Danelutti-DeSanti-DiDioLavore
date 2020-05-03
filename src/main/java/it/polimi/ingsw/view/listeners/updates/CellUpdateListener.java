package it.polimi.ingsw.view.listeners.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.modelview.CellView;

public interface CellUpdateListener {
    public void onCellUpdate(Position cellPosition, CellView changedCell);
}
