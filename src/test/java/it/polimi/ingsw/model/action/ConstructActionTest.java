package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ConstructActionTest {

    ArrayList<Position> notAvailableCellsTester;
    Pawn selectedPawnTester;
    Pawn notSelectedPawnTester;
    Board boardTester;

    /**
     * This test checks whether the duplicate() function of ConstructAction works properly
     */
    @Test
    void duplicate() {
        ConstructAction originalConstructAction = new ConstructAction(true, null, null, true, null, false, true);
        ConstructAction copiedConstructAction = originalConstructAction.duplicate();

        assertNotSame(originalConstructAction, copiedConstructAction, "Original and Copied should not refer to the same object");
        assertEquals(originalConstructAction.getIsOptional(), copiedConstructAction.getIsOptional(), "Internal values should be the equals");
    }

    @BeforeEach
    void setUpTests() {
        notAvailableCellsTester = new ArrayList<>();
        selectedPawnTester = new Pawn("white");
        notSelectedPawnTester = new Pawn("white");
        boardTester = new Board();
    }


    //TODO: the following test schema has to be removed
    /**
     * This test checks whether the availableCells() function of ConstructAction works properly
     */
    @Test
    void availableCellsTestSchema() {
//        notAvailableCellsTester.add(new Position(0,0));
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, false, true);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

//        boardTester.pawnConstruct(new Position(0,2), BlockType.DOME);

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,0));
            add(new Position(0,1));
            add(new Position(0,2));
            add(new Position(1,0));
            add(new Position(1,2));
            add(new Position(2,0));
            add(new Position(2,1));
            add(new Position(2,2));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() function works properly the pawn is next to a board border
     */
    @Test
    void availableCellsTestCheckBorders() {
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, false, true);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(4,0));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(4,1));
            add(new Position(3,1));
            add(new Position(3,0));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() function checks correctly that if constructOnLastBuilt=true the pawn can build only on the position
     * defined in pawn.lastBuildPosition
     */
    @Test
    void availableCellsTestCheckConstructOnLastBuilt() {
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, true, true);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);
        selectedPawnTester.setLastBuildPosition(new Position(0,1));

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,1));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() function checks correctly that a pawn cannot build on cells contained within the notAvailableCells list
     */
    @Test
    void availableCellsCheckNotAvailableCells() {
        notAvailableCellsTester.add(new Position(0,0));
        notAvailableCellsTester.add(new Position(0,1));
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, false, true);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1, 1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3, 3));

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0, 2));
            add(new Position(1, 0));
            add(new Position(1, 2));
            add(new Position(2, 0));
            add(new Position(2, 1));
            add(new Position(2, 2));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }



        /**
         * This test checks whether the availableCells() function checks correctly that a pawn can build on a cell with an higher level
         * than the one of the cell where the pawn is placed
         */
    @Test
    void availableCellsCheckDeltaHeight() {
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, false, true);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(2,1), BlockType.LEVEL1);
        boardTester.pawnConstruct(new Position(2,1), BlockType.LEVEL2);

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,0));
            add(new Position(0,1));
            add(new Position(0,2));
            add(new Position(1,0));
            add(new Position(1,2));
            add(new Position(2,0));
            add(new Position(2,1));
            add(new Position(2,2));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() function checks correctly that a pawn cannot build on a cell where another pawn is placed
     */
    @Test
    void availableCellsCheckPawnPresence() {
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, false, true);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(1,2));

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,0));
            add(new Position(0,1));
            add(new Position(0,2));
            add(new Position(1,0));
            add(new Position(2,0));
            add(new Position(2,1));
            add(new Position(2,2));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() function checks correctly that a pawn cannot build on a cell where a dome is present
     */
    @Test
    void availableCellsCheckDomePresence() {
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, false, true);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(0,2), BlockType.DOME);

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());
        availableCellsTester.forEach(value -> System.out.println(value.getX() + " " + value.getY()));

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,0));
            add(new Position(0,1));
            add(new Position(1,0));
            add(new Position(1,2));
            add(new Position(2,0));
            add(new Position(2,1));
            add(new Position(2,2));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableBlockTypes() function of ConstructAction works properly
     */
    @Test
    void availableBlockTypes() {
        ArrayList<Position> notAvailableCellsTester = new ArrayList<>();
        notAvailableCellsTester.add(new Position(0,0));
        ArrayList<BlockType> alwaysAvailableBlockType = new ArrayList<>();
//        alwaysAvailableBlockType.add(BlockType.DOME);
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, alwaysAvailableBlockType, false, true);

        Pawn selectedPawnTester = new Pawn("white");
        selectedPawnTester.setPosition(new Position(1,1));

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));

        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL2);
        Position selectedPosition = new Position(1,2);

        ArrayList<BlockType> availableBlockTypes = constructActionTester.availableBlockTypes(selectedPosition, boardTester.getMatrixCopy());
        availableBlockTypes.forEach(value -> System.out.println(value));

        assertEquals(BlockType.LEVEL3, availableBlockTypes.get(0), "The block type should be the same");
    }
}