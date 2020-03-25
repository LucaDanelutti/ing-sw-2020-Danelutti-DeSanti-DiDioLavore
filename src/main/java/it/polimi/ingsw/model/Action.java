package it.polimi.ingsw.model;

import java.util.ArrayList;


//TODO: capire se e come gestire le NullPointerException nei costruttori
//TODO: implementare le funzioni mancanti
public abstract class Action {
    protected Boolean isOptional;
    protected ArrayList<Position> notAvailableCell;
    protected Pawn selectedPawn;
    protected Pawn notSelectedPawn;
    protected Position chosenPosition;

    /**
     This constructor of Action sets isOptional and creates an internal copy of notAvailableCell before setting it to the private variable
     */
    Action(Boolean isOptional, ArrayList<Position> notAvailableCell) {
        this.isOptional = isOptional;
        if (notAvailableCell != null) {
            this.notAvailableCell = new ArrayList<>(notAvailableCell);
        } else {
            this.notAvailableCell = null;
        }
    }

    /**
     * This function is the copy constructor for the class Action
     * By using this method, there is no need to implement Clonable
     * @param toBeCopied this is the original Action to be copied
     */
    Action(Action toBeCopied){
        this.isOptional = toBeCopied.isOptional;
        this.selectedPawn = toBeCopied.selectedPawn;
        this.notSelectedPawn = toBeCopied.notSelectedPawn;
        this.chosenPosition = toBeCopied.chosenPosition;
        if (toBeCopied.notAvailableCell != null) {
            this.notAvailableCell = new ArrayList<>(toBeCopied.notAvailableCell);
        } else {
            this.notAvailableCell = null;
        }
    }
    /**
     * Abstract method which returns a duplicate of this. Implemented in the concrete classes.
     * @return Action
     */
    public abstract Action duplicate();

    public void setChosenPosition(Position chosenPosition) {
        this.chosenPosition = chosenPosition;
    }

    public void setSelectedPawn(Pawn selectedPawn) {
        this.selectedPawn = selectedPawn;
    }

    public void setNotSelectedPawn(Pawn notSelectedPawn) {
        this.notSelectedPawn = notSelectedPawn;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public ArrayList<Position> getNotAvailableCell() {
        return notAvailableCell;
    }

    public Pawn getSelectedPawn() {
        return selectedPawn;
    }

    public Pawn getNotSelectedPawn() {
        return notSelectedPawn;
    }

    public Position getChosenPosition() {
        return chosenPosition;
    }

}
