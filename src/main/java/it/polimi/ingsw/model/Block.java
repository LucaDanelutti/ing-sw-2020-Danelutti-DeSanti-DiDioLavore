package it.polimi.ingsw.model;

/**
 * This class is one of the basic elements of the game, it represent one of the concrete blocks in the game to be
 * stacked together
 */
public class Block {
    private final BlockType type;

    /**
     * This function is the constructor for this class.
     * Example of usage: Block n = new Block(BlockType.LEVEL1);
     * @param type This is one of the possible enumeration of the BlockType enum
     */
    Block(BlockType type) {
        this.type = type;
    }

    /**
     * This is the getter for the variable type
     * @return Blocktype
     */
    BlockType getType() {
        return type;
    }
}
