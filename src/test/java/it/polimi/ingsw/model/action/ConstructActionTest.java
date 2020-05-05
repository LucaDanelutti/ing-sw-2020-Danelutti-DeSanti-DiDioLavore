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

    @BeforeEach
    void setUpTests() {
        notAvailableCellsTester = new ArrayList<>();
        selectedPawnTester = new Pawn("white",0);
        notSelectedPawnTester = new Pawn("white",1);
        boardTester = new Board();
    }

    /**
     * This test checks whether the duplicate() function of ConstructAction works properly
     */
    @Test
    void duplicate() {
        ConstructAction originalConstructAction = new ConstructAction(true, null, true, null, false, false, false);
        originalConstructAction.setSelectedPawn(selectedPawnTester);
        ConstructAction copiedConstructAction = originalConstructAction.duplicate();

        assertNotSame(originalConstructAction, copiedConstructAction, "Original and Copied should not refer to the same object");
        assertEquals(originalConstructAction.getSelectedPawn(), copiedConstructAction.getSelectedPawn(), "Internal values should be the equals");
    }

    /**
     * This test checks whether the availableCells() function doesn't let the pawn build on its setLastBuildPosition if notBuildOnLastBuilt = true
     */
    @Test
    void availableCellsTestCheckNotBuildOnLastBuilt() {
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, null, false, false, true);
        selectedPawnTester.setLastBuildPosition(new Position(1,2));

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

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
     * This test checks whether the availableCells() function works properly when the pawn is next to a board border
     */
    @Test
    void availableCellsTestCheckBorders() {
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, null, false, false, false);

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
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, null, true, false, false);

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
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, null, false, false, false);

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
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, null, false, false, false);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(selectedPawnTester.getPosition(),new Position(2,1), BlockType.LEVEL1);
        boardTester.pawnConstruct(selectedPawnTester.getPosition(),new Position(2,1), BlockType.LEVEL2);

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
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, null, false, false, false);

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
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, null, false, false, false);

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(null, new Position(0,2), BlockType.DOME);

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
     * This test checks whether the availableBlockTypes() function works properly when alwaysAvailableBlockType is empty
     * and the player would like to build above a level 3 building
     */
    @Test
    void availableBlockTypesLevel2() {
        ArrayList<Position> notAvailableCellsTester = new ArrayList<>();
        notAvailableCellsTester.add(new Position(0,0));
        ArrayList<BlockType> alwaysAvailableBlockType = new ArrayList<>();
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, alwaysAvailableBlockType, false, false, false);

        Pawn selectedPawnTester = new Pawn("white",0);
        selectedPawnTester.setPosition(new Position(1,1));

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));

        boardTester.pawnConstruct(selectedPawnTester.getPosition(),new Position(1,2), BlockType.LEVEL3);
        Position selectedPosition = new Position(1,2);

        ArrayList<BlockType> availableBlockTypes = constructActionTester.availableBlockTypes(selectedPosition, boardTester.getMatrixCopy());
        ArrayList<BlockType> expectedList = new ArrayList<BlockType>() {{
            add(BlockType.DOME);
        }};

        assertTrue(availableBlockTypes.containsAll(expectedList) && expectedList.containsAll(availableBlockTypes));
    }

    /**
     * This test checks whether the availableBlockTypes() function works properly when alwaysAvailableBlockType is empty
     * and the player would like to build above a level 2 and alwaysAvailableBlockType contains DOME.
     */
    @Test
    void availableBlockTypesLevel3AndCheckAlwaysAvailableBlockType() {
        ArrayList<Position> notAvailableCellsTester = new ArrayList<>();
        notAvailableCellsTester.add(new Position(0,0));
        ArrayList<BlockType> alwaysAvailableBlockType = new ArrayList<>();
        alwaysAvailableBlockType.add(BlockType.DOME);
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, false, alwaysAvailableBlockType, false, false, false);

        Pawn selectedPawnTester = new Pawn("white",0);
        selectedPawnTester.setPosition(new Position(1,1));

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));

        boardTester.pawnConstruct(selectedPawnTester.getPosition(),new Position(1,2), BlockType.LEVEL2);
        Position selectedPosition = new Position(1,2);

        ArrayList<BlockType> availableBlockTypes = constructActionTester.availableBlockTypes(selectedPosition, boardTester.getMatrixCopy());
        ArrayList<BlockType> expectedList = new ArrayList<BlockType>() {{
            add(BlockType.LEVEL3);
            add(BlockType.DOME);
        }};

        assertTrue(availableBlockTypes.containsAll(expectedList) && expectedList.containsAll(availableBlockTypes));
    }
}