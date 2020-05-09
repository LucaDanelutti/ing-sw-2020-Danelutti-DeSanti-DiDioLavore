package it.polimi.ingsw.view.modelview;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;

import java.util.ArrayList;

public class ModelView {
    private static final int MATRIX_SIZE = 5;
    private ArrayList<PlayerView> playerList;
    private CellView[][] matrix;

    public ModelView(){
        this.matrix = new CellView[MATRIX_SIZE][MATRIX_SIZE];
        for(int i=0; i<this.matrix.length; i++){
            for(int j=0; j<this.matrix[0].length; j++){
                this.matrix[i][j] = new CellView();
            }
        }
        playerList = new ArrayList<>();
    }

    public void onPawnPositionUpdate(int pawnId, Position pawnPos) {
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == pawnId) pawn.setPawnPosition(pawnPos);
            }
        }
    }

    public void onPawnRemoved(int pawnId) {
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == pawnId) player.getPawnList().remove(pawn);
            }
        }
    }

    public void onCellUpdate(Position cellPosition, BlockType newBlock) {
        matrix[cellPosition.getX()][cellPosition.getY()].addBlock(newBlock);
    }

    public void onDoublePawnPositionUpdate(int pawnId1, Position pawnPos1, int pawnId2, Position pawnPos2) {
        onPawnPositionUpdate(pawnId1, pawnPos1);
        onPawnPositionUpdate(pawnId2, pawnPos2);
    }

    public void onPlayerUpdate(String name, String color, int pawnId1, int pawnId2) {
        //if the player is already contained within the playerList it has already set its attributes (pawns and relative color)
        Boolean playerExists = false;
        for (PlayerView player : playerList) {
            if (player.getName().equals(name)) {
                playerExists = true;
            }
        }
        //if the player isn't contained within the playerList it is added with the passed attributes
        if (!playerExists) {
            ArrayList<PawnView> newPawnList = new ArrayList<>();
            newPawnList.add(new PawnView(pawnId1, color));
            newPawnList.add(new PawnView(pawnId2, color));
            PlayerView newPlayer = new PlayerView(name, newPawnList);
            playerList.add(newPlayer);
        }
    }

    public void onChosenCardUpdate(CardView chosenCard, String name) {
        for(PlayerView player : playerList) {
            if (player.getName().equals(name)) {
                player.setCard(chosenCard);
            }
        }
    }

    public void onSelectPawnUpdate(Integer id) {
        for (PlayerView player : playerList) {
            for (PawnView pawn: player.getPawnList()) {
                if (pawn.getId() == id) pawn.setSelected(true);
            }
        }
    }
}
