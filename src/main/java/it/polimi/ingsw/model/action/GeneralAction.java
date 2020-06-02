package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;
import java.util.Objects;

public class GeneralAction extends Action{
    private final Boolean enableNoWinIfOnPerimeter;
    private final Boolean destroyPawnAndBuildEnable;


                                                            //CONSTRUCTORS
    /**
     * This is the main constructor for the general Action class
     */
    public GeneralAction(Boolean isOptional, ArrayList<Position> notAvailableCell, Boolean enableNoWinIfOnPerimeter, Boolean destroyPawnAndBuildEnable){
        super(isOptional,notAvailableCell);
        this.destroyPawnAndBuildEnable=destroyPawnAndBuildEnable;
        this.enableNoWinIfOnPerimeter = enableNoWinIfOnPerimeter;
    }
    /**
     * This is the copy constructor for the general Action class
     * @param toBeCopied the general action to be copied
     */
    GeneralAction(GeneralAction toBeCopied){
        super(toBeCopied.isOptional, toBeCopied.notAvailableCell, toBeCopied.selectedPawn, toBeCopied.notSelectedPawn);
        this.destroyPawnAndBuildEnable = toBeCopied.destroyPawnAndBuildEnable;
        this.enableNoWinIfOnPerimeter = toBeCopied.enableNoWinIfOnPerimeter;
    }
    /**
     * This function is used to invoke the copy constructor
     * @return the general action
     */
    public GeneralAction duplicate(){
        return new GeneralAction(this);
    }



                                                    //VISITOR PATTERN FUNCTIONS

    /**
     * This is the function needed to implement the visitor pattern for GameLogicExecutor and execute the current action without knowing the class type
     * @param visitor the visitor
     */
    public void acceptForExecution(ActionVisitor visitor){
        visitor.executeAction(this);
    }
    /**
     * This is the function needed to implement the visitor pattern for GameLogicExecutor and process the current action without knowing the class type
     * @param actionVisitor the visitor
     */
    public void acceptForProcess(ActionVisitor actionVisitor){
        actionVisitor.processAction(this);
    }






                                                        //SPECIFIC FUNCTIONS

    /**
     * Computes the list of cells where a pawn can move
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of cells where a pawn can move
     */
    @Override public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        return new ArrayList<>();
    }
    public void blockSelected(BlockType blockType) {
    }
    public ArrayList<BlockType> availableBlockTypes(Position selectedPosition, Cell[][] matrixCopy){
        return new ArrayList<>();
    }





                                                            //SETTERS

    public void setChosenPosition(Position chosenPosition) {
        this.chosenPosition = chosenPosition;
    }
    public void disablePerimeterWin() {
    }
    public void disableClimbing() {
    }




                                                            //GETTERS

    public Boolean getEnableNoWinIfOnPerimeter() {
        return enableNoWinIfOnPerimeter;
    }
    public Boolean getDestroyPawnAndBuildEnable() {
        return destroyPawnAndBuildEnable;
    }







    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralAction that = (GeneralAction) o;
        return Objects.equals(enableNoWinIfOnPerimeter, that.enableNoWinIfOnPerimeter) &&
                Objects.equals(destroyPawnAndBuildEnable, that.destroyPawnAndBuildEnable);
    }
    @Override public int hashCode() {
        return Objects.hash(enableNoWinIfOnPerimeter, destroyPawnAndBuildEnable);
    }
}
