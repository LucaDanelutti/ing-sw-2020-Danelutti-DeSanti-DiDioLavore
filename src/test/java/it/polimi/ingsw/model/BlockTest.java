package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <H1>BlockTest</H1>
 * This class is the tester class for Block
 */
class BlockTest {

    /**
     * This test should return true if the getter for Block works correctly
     */
    @Test
    void getType() {
        Block tester= new Block(BlockType.DOME);
        assertEquals(BlockType.DOME,tester.getType(),"should answer true");
    }
}