package it.polimi.ingsw.model.playerstate;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for ActionState
 */
class ChoosePawnsPositionStateTest {
    Board testBoard;
    ChoosePawnsPositionState testState;

    @BeforeEach
    void init() {
        testBoard = new Board();
        testState = new ChoosePawnsPositionState();
    }

    /**
     * This test checks whether the availableCells() function of ChoosePawnsPositionState works properly
     * In this test none of the gameboard cells are occupied
     */
    @Test
    void availableCells_AllEmpty() {
        ArrayList<Position> availableCells = new ArrayList<Position>();
        availableCells = testState.availableCells(testBoard.getMatrixCopy());
        for (int i=0; i<testBoard.getMatrixCopy().length; i++) {
            for (int j=0; j<testBoard.getMatrixCopy()[0].length; j++) {
                assertTrue(availableCells.contains(new Position(i,j)));
            }
        }
        assertTrue(availableCells.size() == 25);
    }

    /**
     * This test checks whether the availableCells() function of ChoosePawnsPositionState works properly
     * In this test all of the gameboard cells are occupied
     */
    @Test
    void availableCells_NoneEmpty() {
        ArrayList<Position> availableCells;
        for (int i=0; i < 5; i++) {
            for (int j=0; j < 5; j++) {
                testBoard.setPawnPosition(new Pawn("000000"), new Position(i,j));
            }
        }
        availableCells = testState.availableCells(testBoard.getMatrixCopy());
        assertTrue(availableCells.size() == 0);
    }

    /**
     * This test checks whether the availableCells() function of ChoosePawnsPositionState works properly
     * In this test only the first row of the gameboard is full
     */
    @Test
    void availableCells_FirstRowFull() {
        ArrayList<Position> availableCells;
        for (int j=0; j < 5; j++) {
            testBoard.setPawnPosition(new Pawn("000000"), new Position(0,j));
        }
        availableCells = testState.availableCells(testBoard.getMatrixCopy());
        for (int i=1; i<testBoard.getMatrixCopy().length; i++) {
            for (int j=0; j<testBoard.getMatrixCopy()[0].length; j++) {
                assertTrue(availableCells.contains(new Position(i,j)));
            }
        }
        assertTrue(availableCells.size() == 20);
    }
}