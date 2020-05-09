package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is the test class for Player
 */
class PlayerTest {

    /**
     * The scope of this test function is to test that addPawn method adds the provided Pawn to the Player
     */
    @Test
    void addPawn() {
        Player testPlayer = new Player("Tester");
        assertEquals(0, testPlayer.getPawnList().size());
        Pawn testPawn = new Pawn("ffffff",0);
        testPlayer.addPawn(testPawn);
        assertEquals(1, testPlayer.getPawnList().size());
        assertTrue(testPlayer.getPawnList().contains(testPawn));
    }

    /**
     * The scope of this test function is to test that removePawn method removes the provided Pawn from the Player
     */
    @Test
    void removePawn() {
        Player testPlayer = new Player("Tester");
        Pawn testPawn = new Pawn("ffffff",0);
        testPlayer.addPawn(testPawn);
        assertEquals(1, testPlayer.getPawnList().size());
        assertTrue(testPlayer.getPawnList().contains(testPawn));
        testPlayer.removePawn(testPawn);
        assertEquals(0, testPlayer.getPawnList().size());
        assertFalse(testPlayer.getPawnList().contains(testPawn));
    }
}