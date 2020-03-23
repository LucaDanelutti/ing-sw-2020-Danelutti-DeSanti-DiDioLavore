package it.polimi.ingsw.model;

/**
 * <h1>Position</h1>
 * The Position class is used as a Type to
 * represent the position of a pawn in the board
 */
public class Position {
    private byte x;
    private byte y;

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

    /**
     * @param x This is the abscissa
     */
    public void setX(byte x) {
        this.x = x;
    }

    /**
     * @param y This is the ordinate
     */
    public void setY(byte y) {
        this.y = y;
    }
}
