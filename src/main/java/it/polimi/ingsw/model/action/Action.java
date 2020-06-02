package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import java.util.ArrayList;



public abstract class Action {
    protected Pawn selectedPawn;
    protected Pawn notSelectedPawn;
    protected Position chosenPosition;
    protected Boolean isOptional;
    protected ArrayList<Position> notAvailableCell;


                                                        //CONSTRUCTORS

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
     This constructor of Action is needed for the duplicate function
     */
    Action(Boolean isOptional, ArrayList<Position> notAvailableCell, Pawn selectedPawn, Pawn notSelectedPawn) {
        //this.actionType=actionType;
        this.isOptional = isOptional;
        if (notAvailableCell != null) {
            this.notAvailableCell = new ArrayList<>(notAvailableCell);
        } else {
            this.notAvailableCell = null;
        }
        this.selectedPawn = selectedPawn;
        this.notSelectedPawn = notSelectedPawn;
    }
    /**
     * This function is the copy constructor for the class Action
     * By using this method, there is no need to implement Clonable
     * @param toBeCopied this is the original Action to be copied
     */
    Action(Action toBeCopied){
        this.isOptional = toBeCopied.isOptional;
        this.selectedPawn = new Pawn(toBeCopied.selectedPawn);
        this.notSelectedPawn = new Pawn(toBeCopied.notSelectedPawn);
        this.chosenPosition = new Position(toBeCopied.chosenPosition);
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







                                                            //VISITOR PATTERN FUNCTIONS

    /**
     * This is the function needed to implement the visitor pattern for GameLogicExecutor and execute the current action without knowing the class type
     * @param actionVisitor the visitor
     */
    public abstract  void acceptForExecution(ActionVisitor actionVisitor);
    /**
     * This is the function needed to implement the visitor pattern for GameLogicExecutor and process the current action without knowing the class type
     * @param actionVisitor the visitor
     */
    public abstract  void acceptForProcess(ActionVisitor actionVisitor);





                                                    //COMPUTATIONAL FUNCTIONS FOR THIS ACTION ACTION

    /**
     * This function computes the availableCells for selected pawn
     * @param matrixCopy the current matrix of cells
     * @return the list of available positions
     */
    public abstract ArrayList<Position> availableCells(Cell[][] matrixCopy);
    /**
     * This function returns the available blockTypes for the selected position given the current matrix of cells
     * @param selectedPosition the position
     * @param matrixCopy the matrix of cells
     * @return the available block types
     */
    public abstract ArrayList<BlockType> availableBlockTypes(Position selectedPosition, Cell[][] matrixCopy);








                                                    //SETTERS

    /**
     * This function sets the chosen position
     * @param chosenPosition the position where to MOVE/CONSTRUCT
     */
    public abstract void setChosenPosition(Position chosenPosition);
    /**
     * This is the setter for the selected pawn
     * @param selectedPawn the selected pawn to be set
     */
    public void setSelectedPawn(Pawn selectedPawn) {
        this.selectedPawn = selectedPawn;
    }
    /**
     * This function sets the unselected pawn
     * @param notSelectedPawn the not selected pawn
     */
    public void setNotSelectedPawn(Pawn notSelectedPawn) {
        this.notSelectedPawn = notSelectedPawn;
    }
    /**
     * This function sets the optional parameter
     * @param optional the value of the isOptional parameter
     */
    public void setOptional(Boolean optional) {
        isOptional = optional;
    }
    /**
     * This function disables winning on the perimeter for the current action
     */
    public abstract void disablePerimeterWin();
    /**
     * This function disables climbing up on the blocks
     */
    public abstract void disableClimbing();
    /**
     * This function sets the selected block
     * @param blockType the block to be setted
     */
    public abstract void blockSelected(BlockType blockType);







                                                    //GETTERS

    /**
     * This is the getter for the IsOptional parameter
     * @return the isOptional variable
     */
    public Boolean getIsOptional() {
        return isOptional;
    }
    /**
     * This is the getter for the notAvailableCells
     * @return the not available cells
     */
    public ArrayList<Position> getNotAvailableCell() {
        return notAvailableCell;
    }
    /**
     * This is the getter for the selectedPawn
     * @return the selected pawn
     */
    public Pawn getSelectedPawn() {
        return selectedPawn;
    }
    /**
     * This is the getter for the not selected pawn
     * @return the not selected pawn
     */
    public Pawn getNotSelectedPawn() {
        return notSelectedPawn;
    }
    /**
     * This is the getter for the chosenPosition
     * @return the chosen position
     */
    public Position getChosenPosition() {
        return chosenPosition;
    }



}
