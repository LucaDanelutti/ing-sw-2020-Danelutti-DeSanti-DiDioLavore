package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

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
}