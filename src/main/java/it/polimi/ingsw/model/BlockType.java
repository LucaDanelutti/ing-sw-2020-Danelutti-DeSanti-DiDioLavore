package it.polimi.ingsw.model;

import java.awt.font.GlyphVector;

/**
 *<h1>BlockType</h1>
 * This enum is used to represent the level of a block, each block will have its own "type" from one of the following:
 * LEVEL1 -> 1, LEVEL2 -> 2, LEVEL3 -> 3 or DOME -> 4
 */
enum BlockType {
    LEVEL1((byte)1),
    LEVEL2((byte)2),
    LEVEL3((byte)3),
    DOME((byte)4);

    private final byte level;

    /**
     * This is the constructor for the BlockType enum.
     * @param level This is the type of the block  (1°floor->1| 2 | 3 |4 <- DOME).
     */
    BlockType(byte level){
        this.level=level;
    }

    /**
     * This function is used to retrieve the level of the block selected.
     * @return byte This is the level of the block selected.
     */
    byte getLevel(){
        return this.level;
    }
}
