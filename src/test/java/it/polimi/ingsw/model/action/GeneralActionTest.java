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
        selectedPawnTester = new Pawn("white");
        notSelectedPawnTester = new Pawn("white");
        boardTester = new Board();
    }

    /**
     * This test checks whether the duplicate() function of MoveAction works properly
     */
    @Test
    void duplicate() {
        GeneralAction originalGeneralAction = new GeneralAction(true, notAvailableCellsTester, false, true);
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

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(2,3));

        ArrayList<Position> availableCellsTester = generalActionTester.availableCells(boardTester.getMatrixCopy());
        ArrayList<Position> expectedList = new ArrayList<>();
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() function works properly when pushEnable = true and there is a pawn that can be pushed
     * and another pawn that cannot be pushed because it's on a border cell
     */
    @Test
    void availableCellsCheckPushEnable() {
        GeneralAction generalActionTester = new GeneralAction(true, notAvailableCellsTester, true, false);

        Pawn opponentPawn1Tester = new Pawn("grey");
        Pawn opponentPawn2Tester = new Pawn("grey");

        generalActionTester.setSelectedPawn(selectedPawnTester);
        generalActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(opponentPawn1Tester, new Position(1,2));
        boardTester.setPawnPosition(opponentPawn2Tester, new Position(0,0));

        ArrayList<Position> availableCellsTester = generalActionTester.availableCells(boardTester.getMatrixCopy());
        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(1,2));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

}