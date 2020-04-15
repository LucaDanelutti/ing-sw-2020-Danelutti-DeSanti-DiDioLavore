package it.polimi.ingsw.model.playerstate;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;

/**
 * Setup state ChoosePawnsPositionState: every player
 * has to place his pawns in the game board
 */
public class ChoosePawnsPositionState extends PlayerState {
    public ChoosePawnsPositionState() {
        super(PlayerStateType.ChoosePawnsPositionState);
    }

    /**
     * Computes the list of cells to which a pawn can be placed
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available cells to which the pawn can be placed
     */
    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        ArrayList<Position> availableCells = new ArrayList<>();
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j=0; j<matrixCopy[0].length; j++) {
                //Adds to availableCells the free cells
                if (matrixCopy[i][j].getPawn() == null) {
                    availableCells.add(new Position(i, j));
                }
            }
        }
        return availableCells;
    }
}
