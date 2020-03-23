package it.polimi.ingsw.model;


/**
 * <h1>Pawn</h1>
 * This class represents the element Pawn of the game
 */
public class Pawn {
    private int color;
    private Position position;
    private Position previousPosition;
    private Position lastBuildPosition;
    private int deltaHeight;

    /**
     * Constructor of this class
     * @param color defines the color of a pawn
     */
    public Pawn(int color) {
        this.color = color;
    }

    /**
     * @param position is the new position of the pawn
     */
    public void setPosition(Position position) {
        if (this.position != null) {
            this.previousPosition = this.position;
        }
        this.position = position;
    }

    /**
     * @param lastBuildPosition is the position where the last building has been built by the considered pawn
     */
    public void setLastBuildPosition(Position lastBuildPosition) {
        this.lastBuildPosition = lastBuildPosition;
    }

    /**
     * @param deltaHeight is the difference in height between the last position and current position of the pawn
     */
    public void setDeltaHeight(int deltaHeight) {
        this.deltaHeight = deltaHeight;
    }

    /**
     * @return pawn color
     */
    public int getColor() {
        return color;
    }

    /**
     * @return current position of the pawn
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return previous position of the pawn
     */
    public Position getPreviousPosition() {
        return previousPosition;
    }

    /**
     * @return position of the last block built by the pawn
     */
    public Position getLastBuildPosition() {
        return lastBuildPosition;
    }

    /**
     * @return the difference in height between the last position and current position of the pawn
     */
    public int getDeltaHeight() {
        return deltaHeight;
    }
}
