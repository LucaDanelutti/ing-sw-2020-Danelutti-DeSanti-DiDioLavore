package it.polimi.ingsw.model;

/**
 * <h1>Position</h1>
 * The Position class is used as a Type to
 * represent the position of a pawn in the board
 */
public class Position {
    private final byte x;
    private final byte y;

    /**
     * Default constructor
     * @param x This is the abscissa
     * @param y  This is the ordinate
     */
    public Position(byte x, byte y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return Abscissa
     */
    public byte getX() {
        return x;
    }

    /**
     * @return Ordinate
     */
    public byte getY() {
        return y;
    }
}
