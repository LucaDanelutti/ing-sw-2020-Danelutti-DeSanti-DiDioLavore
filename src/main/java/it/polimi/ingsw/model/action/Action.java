package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;



public abstract class Action {
    public void setOptional(Boolean optional) {
        isOptional = optional;
    }

    protected Boolean isOptional;
    protected ArrayList<Position> notAvailableCell;
    protected Pawn selectedPawn;
    protected Pawn notSelectedPawn;
    protected Position chosenPosition;
    //protected final ActionType actionType;
    protected List<ActionVisitor> actionVisitors = new ArrayList<>();


    //-------------------------------------------------------------------------------------------------------------------


    /**
     This constructor of Action sets isOptional and creates an internal copy of notAvailableCell before setting it to the private variable
     */
    Action(Boolean isOptional, ArrayList<Position> notAvailableCell,ActionType actionType) {
        //this.actionType=actionType;
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
    Action(Boolean isOptional, ArrayList<Position> notAvailableCell,ActionType actionType, Pawn selectedPawn, Pawn notSelectedPawn, List<ActionVisitor> actionVisitors) {
        //this.actionType=actionType;
        this.isOptional = isOptional;
        if (notAvailableCell != null) {
            this.notAvailableCell = new ArrayList<>(notAvailableCell);
        } else {
            this.notAvailableCell = null;
        }
        this.selectedPawn = selectedPawn;
        this.notSelectedPawn = notSelectedPawn;
        this.actionVisitors = new ArrayList<>();
        this.actionVisitors.addAll(actionVisitors);
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
        this.actionVisitors=toBeCopied.actionVisitors;
        //this.actionType=toBeCopied.actionType;
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

    /**
     * This is the function needed to implement the visitor pattern for GameLogicExecutor
     * @param actionVisitor the visitor
     */
    public abstract  void accept(ActionVisitor actionVisitor);
    public abstract  void acceptForProcess();

    public void addVisitor(ActionVisitor actionVisitor){
        this.actionVisitors.add(actionVisitor);
    }
    public void removeVisitor(ActionVisitor actionVisitor){
        this.actionVisitors.remove(actionVisitor);
    }

    /**
     * Notify GameLogic only if this is an MoveAction, for ConstructAction you have to notify the observer once
     * chosenBlockType is set!
     * @param chosenPosition the position where to MOVE/CONSTRUCT
     */
    public abstract void setChosenPosition(Position chosenPosition);

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

    /*public ActionType getActionType() {
        return actionType;
    }*/


    public abstract ArrayList<Position> availableCells(Cell[][] matrixCopy);

    public abstract void disablePerimeterWin();

    public abstract void disableClimbing();

    public abstract void blockSelected(BlockType blockType);

    public abstract ArrayList<BlockType> availableBlockTypes(Position selectedPosition, Cell[][] matrixCopy);

}
