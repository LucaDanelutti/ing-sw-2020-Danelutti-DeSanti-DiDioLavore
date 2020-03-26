package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is used to test the Pawn class
 */
class PawnTest {

    /**
     * Initialize the variable color of a pawn through the class constructor and then checks if getColor() returns the correct value
     */
    @Test
    void getColor() {
        Pawn pawnTester = new Pawn("ffffff");
        assertEquals("ffffff", pawnTester.getColor());
    }

    /**
     * Initialize a pawn and sets its variable position. Then checks if getPosition() returns the correct value
     */
    @Test
    void getPosition() {
        Pawn pawnTester = new Pawn("ffffff");
        pawnTester.setPosition(new Position(2,3));
        assertEquals(2, pawnTester.getPosition().getX());
        assertEquals(3, pawnTester.getPosition().getY());
    }

    /**
     * Initialize a pawn and sets its variable position. Then changes it and checks if getPreviousPosition() returns the correct value
     */
    @Test
    void getPreviousPosition() {
        Pawn pawnTester = new Pawn("ffffff");
        pawnTester.setPosition(new Position(2,3));
        pawnTester.setPosition(new Position(4,5));
        assertEquals(2, pawnTester.getPreviousPosition().getX());
        assertEquals(3, pawnTester.getPreviousPosition().getY());
    }

    /**
     * Initialize a pawn, sets its variable lastBuildPosition and checks if getLastBuildPosition() returns the correct value
     */
    @Test
    void getLastBuildPosition() {
        Pawn pawnTester = new Pawn("ffffff");
        pawnTester.setLastBuildPosition(new Position(3,4));
        assertEquals(3, pawnTester.getLastBuildPosition().getX());
        assertEquals(4, pawnTester.getLastBuildPosition().getY());
    }

    /**
     * Initialize a pawn, sets its variable deltaHeight and checks if getDeltaHeight() returns the correct value
     */
    @Test
    void getDeltaHeight() {
        Pawn pawnTester = new Pawn("ffffff");
        pawnTester.setDeltaHeight(3);
        assertEquals(3, pawnTester.getDeltaHeight());
    }
}