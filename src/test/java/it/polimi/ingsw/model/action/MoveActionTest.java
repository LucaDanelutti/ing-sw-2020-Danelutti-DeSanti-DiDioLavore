package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MoveActionTest {

    ArrayList<Position> notAvailableCellsTester;
    Pawn selectedPawnTester;
    Pawn notSelectedPawnTester;
    Board boardTester;


    /**
     * This test checks whether the duplicate() function of MoveAction works properly
     */
    @Test
    void duplicate() {
        MoveAction originalMoveAction = new MoveAction(true, null, true, true, true, true, true, true, null);
        MoveAction copiedMoveAction = originalMoveAction.duplicate();

        assertNotSame(originalMoveAction, copiedMoveAction, "Original and Copied should not refer to the same object");
        assertEquals(originalMoveAction.getIsOptional(), copiedMoveAction.getIsOptional(), "Internal values should be the equals");
    }


    @BeforeEach
    void setBoardAndPawn() {
        notAvailableCellsTester = new ArrayList<>();
        selectedPawnTester = new Pawn("white");
        notSelectedPawnTester = new Pawn("white");
        boardTester = new Board();
    }

    /**
     * This test checks whether the availableCells() works properly when the pawn cannot move to a cell occupied by another pawn (moveOnOpponentEnable = true, swapEnable = true)
     * because the latter is a pawn of the same player
     */
    @Test
    void availableCellsTestCannotSwapPawn() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, true, true, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(1,2));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can move to a cell occupied by another pawn (moveOnOpponentEnable = true, swapEnable = true)
     */
    @Test
    void availableCellsTestCanSwapPawn() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, true, true, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(1,2));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can't actuallly move to a cell occupied by another pawn (moveOnOpponentEnable = true, pushEnable = true) because
     * the other pawn can't actually be pushed, because of a dome behind it, even though it is an opponent pawn
     */
    @Test
    void availableCellsTestCannotPushPawn3() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, true, true, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(1,2));

        boardTester.pawnConstruct(new Position(1,3), BlockType.DOME);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can't actuallly move to a cell occupied by another pawn (moveOnOpponentEnable = true, pushEnable = true) because
     * the other pawn is a pawn of the same player
     */
    @Test
    void availableCellsTestCannotPushPawn2() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, true, true, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(1,2));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can't actuallly move to a cell occupied by another pawn (moveOnOpponentEnable = true, pushEnable = true) because
     * the other pawn can't actually be pushed, because of a border, even though it is an opponent pawn
     */
    @Test
    void availableCellsTestCannotPushPawn1() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, true, true, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(0,2));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can move to a cell occupied by another pawn (moveOnOpponentEnable = true, pushEnable = true) and
     * the other pawn can actually be pushed (it is an opponent's pawn and there is space behind it) even though there is a 2-levels building behind it.
     */
    @Test
    void availableCellsTestCanPushPawn2() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, true, true, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(1,2));

        boardTester.pawnConstruct(new Position(1,3), BlockType.LEVEL1);
        boardTester.pawnConstruct(new Position(1,3), BlockType.LEVEL2);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can move to a cell occupied by another pawn (moveOnOpponentEnable = true, pushEnable = true) and
     * the other pawn can actually be pushed (it is an opponent's pawn and there is space behind it)
     */
    @Test
    void availableCellsTestCanPushPawn1() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, true, true, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(2,1));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can't move to a cell occupied by another pawn and moveOnOpponentEnable = false
     */
    @Test
    void availableCellsTestCannotMoveOnPawn0() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(2,0));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,0));
            add(new Position(0,1));
            add(new Position(0,2));
            add(new Position(1,0));
            add(new Position(1,2));
            add(new Position(2,1));
            add(new Position(2,2));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() works properly when the pawn can't move upon a dome
     */
    @Test
    void availableCellsTestCannotMoveOnDome() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, false, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(1,2), BlockType.DOME);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can't move up 2 levels (even though moveUpEnable = true)
     */
    @Test
    void availableCellsTestCannotMoveUp2Level() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL1);
        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL2);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn can't move up 1 level (moveUpEnable = false)
     */
    @Test
    void availableCellsTestCannotMoveUp1Level() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, false, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL1);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn may move up 1 level (moveUpEnable = true)
     */
    @Test
    void availableCellsTestMoveUp() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL1);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn may move down of 2 levels
     */
    @Test
    void availableCellsTestMoveDown2levels() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(1,1), BlockType.LEVEL1);
        boardTester.pawnConstruct(new Position(1,1), BlockType.LEVEL2);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when the pawn may move down of 1 level
     */
    @Test
    void availableCellsTestMoveDown1level() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        boardTester.pawnConstruct(new Position(1,1), BlockType.LEVEL1);

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

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
     * This test checks whether the availableCells() works properly when some notAvailableCells are set
     */
    @Test
    void availableCellsTestNotAvailableCells() {
        notAvailableCellsTester.add(new Position(0,0));
        notAvailableCellsTester.add(new Position(0,1));
        notAvailableCellsTester.add(new Position(2,2));
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,2));
            add(new Position(1,0));
            add(new Position(1,2));
            add(new Position(2,0));
            add(new Position(2,1));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }

    /**
     * This test checks whether the availableCells() works properly next to the borders of the board
     */
    @Test
    void availableCellsTestBorders() {
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, true, false, false, false, false, false, null);

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);

        boardTester.setPawnPosition(selectedPawnTester, new Position(0,0));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(3,3));

        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,1));
            add(new Position(1,1));
            add(new Position(1,0));
        }};
        assertTrue(availableCellsTester.containsAll(expectedList) && expectedList.containsAll(availableCellsTester));
    }


    /**
     * This test checks whether the availableCells() function of MoveAction works properly
     */
    @Test
    void checkwin() {
        MoveAction moveActionTester = new MoveAction(true, null, false, false, true, true, true, true, null);
        Board boardTester = new Board();
        Pawn selectedPawnTester = new Pawn("white");
        selectedPawnTester.setPosition(new Position(1,2));
//      Checks whether the player wins if it is on a Level3 cell
//        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL1);
//        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL2);
//        boardTester.pawnConstruct(new Position(1,2), BlockType.LEVEL3);
        moveActionTester.setSelectedPawn(selectedPawnTester);

//      Checks whether the player wins if its DeltaHeight is <=-2
        selectedPawnTester.setDeltaHeight(-2);

        assertEquals(true, moveActionTester.checkWin(boardTester.getMatrixCopy()), "Internal values should be the equals");
    }
}