package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <H1>BlockTypeTest</H1>
 * This class is used to test the BlockType enumeration
 */
public class BlockTypeTest {

    /**
     * This function creates a BlockType object and checks that getValue returns the correct value
     */
    @Test
    public void getLevelTest() {
        BlockType tester = BlockType.LEVEL1;
        assertEquals(1, tester.getLevel(),"Should return the value of LEVEL1, which is 1");
    }
}