package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ConstructActionTest {

    /**
     * This test checks whether the duplicate() function of ConstructAction works properly
     */
    @Test
    void duplicate() {
        ConstructAction originalConstructAction = new ConstructAction(true, null, null, true, null, null, true);
        ConstructAction copiedConstructAction = originalConstructAction.duplicate();

        assertNotSame(originalConstructAction, copiedConstructAction, "Original and Copied should not refer to the same object");
        assertEquals(originalConstructAction.getIsOptional(), copiedConstructAction.getIsOptional(), "Internal values should be the equals");
    }

    /**
     * This test checks whether the availableCells() function of ConstructAction works properly
     */
    @Test
    void availableCells() {
        ArrayList<Position> notAvailableCellsTester = new ArrayList<>();
        notAvailableCellsTester.add(new Position(0,0));
        ConstructAction constructActionTester = new ConstructAction(true, notAvailableCellsTester, null, false, null, null, true);

        Pawn selectedPawnTester = new Pawn("white");
        Pawn notSelectedPawnTester = new Pawn("white");
        selectedPawnTester.setPosition(new Position(1,1));
        notSelectedPawnTester.setPosition(new Position(2,2));

        constructActionTester.setSelectedPawn(selectedPawnTester);
        constructActionTester.setNotSelectedPawn(notSelectedPawnTester);

        Board boardTester = new Board();
        boardTester.setPawnPosition(selectedPawnTester, new Position(1,1));
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(2,2));

        boardTester.pawnConstruct(new Position(0,2), BlockType.DOME);

        ArrayList<Position> availableCellsTester = constructActionTester.availableCells(boardTester.getMatrixCopy());
        availableCellsTester.forEach(value -> System.out.println(value.getX() + " " + value.getY()));
    }
}