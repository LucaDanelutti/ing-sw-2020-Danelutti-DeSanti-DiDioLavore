package it.polimi.ingsw.view.modelview;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.UpdatesListener;

import java.util.ArrayList;

public class ModelView implements UpdatesListener {
    private ArrayList<PlayerView> playerList;
    private CellView[][] matrix;

    @Override
    public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage){
        int pawnId=pawnPositionUpdateMessage.getWorkerId();
        Position pawnPos=pawnPositionUpdateMessage.getWorkerPos();
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == pawnId) pawn.setPawnPosition(pawnPos);
            }
        }
    }

    @Override
    public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage){
        for (PlayerView player : playerList) {
            player.getPawnList().removeIf(pawn -> pawn.getId() == pawnRemoveUpdateMessage.getWorkerId());
        }
    }

    @Override
    public void update(CellUpdateMessage cellUpdateMessage){
        Position cellPosition=cellUpdateMessage.getPosition();
        matrix[cellPosition.getX()][cellPosition.getY()] = cellUpdateMessage.getCell();
    }

    @Override
    public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {

    }

    @Override
    public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage){
        int pawnId1=doublePawnPositionUpdateMessage.getWorkerId1();
        Position pawnPos1=doublePawnPositionUpdateMessage.getWorkerPos1();
        int pawnId2=doublePawnPositionUpdateMessage.getWorkerId2();
        Position pawnPos2=doublePawnPositionUpdateMessage.getWorkerPos2();

        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == pawnId1) pawn.setPawnPosition(pawnPos1);
            }
        }
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == pawnId2) pawn.setPawnPosition(pawnPos2);
            }
        }

    }

    @Override
    public void update(PlayerUpdateMessage playerUpdateMessage){
        //TODO: continue
    }

    @Override
    public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage){
        int id=selectedPawnUpdateMessage.getWorkerId();
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == id) pawn.setSelected(true);
            }
        }
    }

    @Override
    public void update(GameStartMessage gameStartMessage) {

    }

    @Override
    public void update(TurnEndedMessage turnEndedMessage) {

    }

    @Override
    public void update(Object o) {

    }
}
