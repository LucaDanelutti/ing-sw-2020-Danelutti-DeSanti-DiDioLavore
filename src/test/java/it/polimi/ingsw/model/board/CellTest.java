package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for Cell class
 */
class CellTest {

    /**
     * The scope of this test function is to test that the copy constructor actually creates a copy of the cell, with no
     * reference to the original a part of the Pawn variable
     */
    @Test
    void copyConstructorShouldReturnACopy(){
        Cell originalCell=new Cell();
        Pawn originalPawn=new Pawn("ffffff");

        Cell copiedCell=new Cell(originalCell);

        assertNotSame(originalCell,copiedCell,"Original and Copied should not refer to the same object");
        assertSame(originalCell.getPawn(),copiedCell.getPawn(),"pawn variable should be a shallow copy");
    }

}