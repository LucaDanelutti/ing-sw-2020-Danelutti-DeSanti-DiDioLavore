package it.polimi.ingsw.model;

/**
 * <h1>Position</h1>
 * The Position class is used as a Type to
 * represent the position of a pawn in the board
 */
public class Position {
    private int x;
    private int y;

    /**
     * Default constructor
     * @param x This is the abscissa
     * @param y  This is the ordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return Abscissa
     */
    public int getX() {
        return x;
    }

    /**
     * @return Ordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @param x This is the abscissa
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y This is the ordinate
     */
    public void setY(int y) {
        this.y = y;
    }
}
