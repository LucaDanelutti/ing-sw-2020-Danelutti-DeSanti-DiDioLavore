package it.polimi.ingsw.view.modelview;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.listeners.updates.*;

import java.util.ArrayList;

public class ModelView implements PawnPositionUpdateListener, PawnRemoveUpdateListener, CellUpdateListener, DoublePawnPositionUpdateListener, PlayerUpdateListener, SelectPawnUpdateListener {
    private ArrayList<PlayerView> playerList;
    private CellView[][] matrix;

    @Override
    public void onPawnPositionUpdate(Integer pawnId, Position pawnPos) {
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == pawnId) pawn.setPawnPosition(pawnPos);
            }
        }
    }

    @Override
    public void onPawnRemoved(Integer pawnId) {
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == pawnId) player.getPawnList().remove(pawn);
            }
        }
    }

    @Override
    public void onCellUpdate(Position cellPosition, CellView changedCell) {
        matrix[cellPosition.getX()][cellPosition.getY()] = changedCell;
    }

    @Override
    public void onDoublePawnPositionUpdate(Integer pawnId1, Position pawnPos1, Integer pawnId2, Position pawnPos2) {
        onPawnPositionUpdate(pawnId1, pawnPos1);
        onPawnPositionUpdate(pawnId2, pawnPos2);
    }

    @Override
    public void onPlayerUpdate(String name, int id1, String color1, int id2, String color2) {
        for (PlayerView player : playerList) {
            if (player.getName().equals(name)) {
                //TODO: continue
            }
        }
    }

    @Override
    public void onSelectPawnUpdate(Integer id) {
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == id) pawn.setSelected(true);
            }
        }
    }
}
