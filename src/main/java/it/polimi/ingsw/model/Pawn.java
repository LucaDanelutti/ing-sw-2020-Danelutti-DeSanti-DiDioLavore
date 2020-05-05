package it.polimi.ingsw.model;


import it.polimi.ingsw.model.action.ConstructAction;

import java.util.Objects;

/**
 * This class represents the element Pawn of the game
 */
public class Pawn {
    private String color;
    private Position position;
    private Position previousPosition;
    private Position lastBuildPosition;
    private int deltaHeight;
    private int id;

    public int getId() {
        return id;
    }


    /**
     * Constructor of this class
     * @param color defines the color of a pawn
     */
    public Pawn(String color,int id) {
        this.id=id;
        this.color = color;
    }

    /**
     * this is the copy constructor for Pawn class
     * @param toBeCopied the pawn to be copied
     */
    public Pawn(Pawn toBeCopied){
        this.color=toBeCopied.color;
        if(toBeCopied.position!=null)
            this.position=new Position(toBeCopied.position.getX(),toBeCopied.position.getY());
        if(toBeCopied.previousPosition!=null)
            this.previousPosition=new Position(toBeCopied.previousPosition.getX(),toBeCopied.previousPosition.getY());
        if(toBeCopied.lastBuildPosition!=null)
            this.lastBuildPosition=new Position(toBeCopied.lastBuildPosition.getX(),toBeCopied.lastBuildPosition.getY());
        this.deltaHeight=toBeCopied.deltaHeight;
        this.id=toBeCopied.id;
    }

    public Pawn duplicate() {
        return new Pawn(this);
    }

    /**
     * Set method of the variable position, it also updates the value of previousPosition if possible.
     * @param position is the new position of the pawn
     */
    public void setPosition(Position position) {
        this.previousPosition = this.position;
        this.position = position;
    }

    /**
     * Set method of the variable lastBuildPosition
     * @param lastBuildPosition is the position where the last building has been built by the considered pawn
     */
    public void setLastBuildPosition(Position lastBuildPosition) {
        this.lastBuildPosition = lastBuildPosition;
    }

    /**
     * Set method of the variable deltaHeight
     * @param deltaHeight is the difference in height between the last position and current position of the pawn
     */
    public void setDeltaHeight(int deltaHeight) {
        this.deltaHeight = deltaHeight;
    }

    /**
     * Get method of the variable color
     * @return pawn color
     */
    public String getColor() {
        return color;
    }

    /**
     * Get method of the variable position
     * @return current position of the pawn
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get method of the variable previousPosition
     * @return previous position of the pawn
     */
    public Position getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Get method of the variable lastBuildPosition
     * @return position of the last block built by the pawn
     */
    public Position getLastBuildPosition() {
        return lastBuildPosition;
    }

    /**
     * Get method of the variable deltaHeight
     * @return the difference in height between the last position and current position of the pawn
     */
    public int getDeltaHeight() {
        return deltaHeight;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pawn pawn = (Pawn) o;
        return deltaHeight == pawn.deltaHeight &&
                color.equals(pawn.color) &&
                Objects.equals(position, pawn.position) &&
                Objects.equals(previousPosition, pawn.previousPosition) &&
                Objects.equals(lastBuildPosition, pawn.lastBuildPosition)&&
                Objects.equals(id,pawn.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, position, previousPosition, lastBuildPosition, deltaHeight);
    }
}
