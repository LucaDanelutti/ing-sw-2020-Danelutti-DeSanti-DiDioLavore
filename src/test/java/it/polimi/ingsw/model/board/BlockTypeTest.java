package it.polimi.ingsw.model.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is used to test the BlockType enumeration
 */
public class BlockTypeTest {

    /**
     * This function creates a BlockType object and checks that getValue returns the correct value
     */
    @Test
    public void getLevel() {
        BlockType tester = BlockType.LEVEL1;
        assertEquals(1, tester.getLevel(),"Should return the value of LEVEL1, which is 1");
    }
}