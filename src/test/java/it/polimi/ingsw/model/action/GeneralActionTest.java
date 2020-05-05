package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GeneralActionTest {

    ArrayList<Position> notAvailableCellsTester;
    Pawn selectedPawnTester;
    Pawn notSelectedPawnTester;
    Board boardTester;

    @BeforeEach
    void setUpTests() {
        notAvailableCellsTester = new ArrayList<>();
        selectedPawnTester = new Pawn("white",0);
        notSelectedPawnTester = new Pawn("white",1);
        boardTester = new Board();
    }

    /**
     * This test checks whether the duplicate() function of MoveAction works properly
     */
    @Test
    void duplicate() {
        GeneralAction originalGeneralAction = new GeneralAction(true, notAvailableCellsTester, false, false);
        originalGeneralAction.setSelectedPawn(selectedPawnTester);
        GeneralAction copiedMoveAction = originalGeneralAction.duplicate();

        assertNotSame(originalGeneralAction, copiedMoveAction, "Original and Copied should not refer to the same object");
        assertEquals(originalGeneralAction.getSelectedPawn(), copiedMoveAction.getSelectedPawn(), "Internal values should be the equals");
    }

    /**
     * This test checks whether the availableCells() function works properly when destroyPawnAndBuildEnable = true
     */
    @Test
    void availableCellsCheckDestroyPawnAndBuildEnable() {
        GeneralAction generalActionTester = new GeneralAction(true, notAvailableCellsTester, false, true);
        selectedPawnTester.setPosition(new Position(1,2));
        notSelectedPawnTester.setPosition(new Position(1,1));

        generalActionTester.setSelectedPawn(selectedPawnTester);
        generalActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(notSelectedPawnTester, new Position(1,1));

        Pawn enemyPawn = new Pawn("grey",3);
        boardTester.setPawnPosition(enemyPawn, new Position(2,3));

        ArrayList<Position> availableCellsTester = generalActionTester.availableCells(boardTester.getMatrixCopy());
        ArrayList<Position> expectedList = new ArrayList<>();
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

}