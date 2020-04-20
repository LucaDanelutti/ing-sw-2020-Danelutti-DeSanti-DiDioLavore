package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;
import java.util.Objects;

public class GeneralAction extends Action{
    private Boolean enableNoWinIfOnPerimeter;
    private Boolean destroyPawnAndBuildEnable;

    public GeneralAction(Boolean isOptional, ArrayList<Position> notAvailableCell, Boolean enableNoWinIfOnPerimeter, Boolean destroyPawnAndBuildEnable){
        super(isOptional,notAvailableCell,ActionType.GENERAL);
        this.destroyPawnAndBuildEnable=destroyPawnAndBuildEnable;
        this.enableNoWinIfOnPerimeter = enableNoWinIfOnPerimeter;
    }
    GeneralAction(GeneralAction toBeCopied){
        super(toBeCopied.isOptional, toBeCopied.notAvailableCell, ActionType.GENERAL, toBeCopied.selectedPawn, toBeCopied.notSelectedPawn, toBeCopied.actionObservers);
        this.destroyPawnAndBuildEnable = toBeCopied.destroyPawnAndBuildEnable;
        this.enableNoWinIfOnPerimeter = toBeCopied.enableNoWinIfOnPerimeter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralAction that = (GeneralAction) o;
        return Objects.equals(enableNoWinIfOnPerimeter, that.enableNoWinIfOnPerimeter) &&
                Objects.equals(destroyPawnAndBuildEnable, that.destroyPawnAndBuildEnable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableNoWinIfOnPerimeter, destroyPawnAndBuildEnable);
    }

    public GeneralAction duplicate(){
        return new GeneralAction(this);
    }

    public void accept(ActionVisitor visitor){
        visitor.executeAction(this);
    }


    /**
     * Computes the list of cells where a pawn can move
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of cells where a pawn can move
     */
    @Override
    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        ArrayList<Position> availableCells = new ArrayList<>();
        return availableCells;
    }

    public void  setEnableNoWinIfOnPerimeter(Boolean enableNoWinIfOnPerimeter) { this.enableNoWinIfOnPerimeter = enableNoWinIfOnPerimeter; }

    public Boolean getEnableNoWinIfOnPerimeter() {
        return enableNoWinIfOnPerimeter;
    }

    public Boolean getDestroyPawnAndBuildEnable() {
        return destroyPawnAndBuildEnable;
    }
}
