package it.polimi.ingsw.model;


/**
 * This class represents the element Pawn of the game
 */
class Pawn {
    private String color;
    private Position position;
    private Position previousPosition;
    private Position lastBuildPosition;
    private int deltaHeight;

    /**
     * Constructor of this class
     * @param color defines the color of a pawn
     */
    Pawn(String color) {
        this.color = color;
    }

    /**
     * Set method of the variable position, it also updates the value of previousPosition if possible.
     * @param position is the new position of the pawn
     */
    void setPosition(Position position) {
        this.previousPosition = this.position;
        this.position = position;
    }

    /**
     * Set method of the variable lastBuildPosition
     * @param lastBuildPosition is the position where the last building has been built by the considered pawn
     */
    void setLastBuildPosition(Position lastBuildPosition) {
        this.lastBuildPosition = lastBuildPosition;
    }

    /**
     * Set method of the variable deltaHeight
     * @param deltaHeight is the difference in height between the last position and current position of the pawn
     */
    void setDeltaHeight(int deltaHeight) {
        this.deltaHeight = deltaHeight;
    }

    /**
     * Get method of the variable color
     * @return pawn color
     */
    String getColor() {
        return color;
    }

    /**
     * Get method of the variable position
     * @return current position of the pawn
     */
    Position getPosition() {
        return position;
    }

    /**
     * Get method of the variable previousPosition
     * @return previous position of the pawn
     */
    Position getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Get method of the variable lastBuildPosition
     * @return position of the last block built by the pawn
     */
    Position getLastBuildPosition() {
        return lastBuildPosition;
    }

    /**
     * Get method of the variable deltaHeight
     * @return the difference in height between the last position and current position of the pawn
     */
    int getDeltaHeight() {
        return deltaHeight;
    }
}
