package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Board;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MoveActionTest {

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


    /**
     * This test checks whether the availableCells() function of MoveAction works properly
     */
    @Test
    void availableCellsTest() {
        ArrayList<Position> notAvailableCellsTester = new ArrayList<>();
        notAvailableCellsTester.add(new Position(1,1));
        notAvailableCellsTester.add(new Position(1,2));
        MoveAction moveActionTester = new MoveAction(true, notAvailableCellsTester, false, false, true, true, true, true, null);

        Pawn selectedPawnTester = new Pawn("white");
        Pawn notSelectedPawnTester = new Pawn("white");
        selectedPawnTester.setPosition(new Position(1,2));
        notSelectedPawnTester.setPosition(new Position(2,2));

        moveActionTester.setSelectedPawn(selectedPawnTester);
        moveActionTester.setNotSelectedPawn(notSelectedPawnTester);


        Board boardTester = new Board();

//        boardTester.pawnConstruct(new Position(1,1), BlockType.DOME);
//        boardTester.setPawnPosition(notSelectedPawnTester, new Position(1,4));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(2,3));


//        boardTester.pawnConstruct(new Position(3,4), BlockType.DOME);
//        boardTester.pawnConstruct(new Position(1,4), BlockType.LEVEL2);
//        boardTester.pawnConstruct(new Position(1,4), BlockType.LEVEL3);



        ArrayList<Position> availableCellsTester = moveActionTester.availableCells(boardTester.getMatrixCopy());
        availableCellsTester.forEach(value -> System.out.println(value.getX() + " " + value.getY()));
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