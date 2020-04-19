package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;
import java.util.Objects;

public class GeneralAction extends Action{
    private Boolean pushEnable;
    private Boolean destroyPawnAndBuildEnable;

    public GeneralAction(Boolean isOptional, ArrayList<Position> notAvailableCell, Boolean pushEnable, Boolean destroyPawnAndBuildEnable){
        super(isOptional,notAvailableCell,ActionType.GENERAL);
        this.destroyPawnAndBuildEnable=destroyPawnAndBuildEnable;
        this.pushEnable=pushEnable;
    }
    GeneralAction(GeneralAction toBeCopied){
       this(toBeCopied.isOptional,toBeCopied.notAvailableCell,toBeCopied.pushEnable,toBeCopied.destroyPawnAndBuildEnable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralAction that = (GeneralAction) o;
        return Objects.equals(pushEnable, that.pushEnable) &&
                Objects.equals(destroyPawnAndBuildEnable, that.destroyPawnAndBuildEnable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pushEnable, destroyPawnAndBuildEnable);
    }

    public GeneralAction duplicate(){
        return new GeneralAction(this);
    }

    public void accept(ActionVisitor visitor){
        visitor.executeAction(this);
    }


    /**
     * Defines whether a pawn can be pushed or not
     * @param otherPawnPosition is the position where the pawn that may be pushed is placed
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     */
    private Boolean canPushOpponent(Cell[][] matrixCopy, Position otherPawnPosition) {
        if (otherPawnPosition.equals(notSelectedPawn.getPosition())) return false;
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        //relativePosition expresses the position of enemyPawn wrt the position of selectedPawn: its coordinates can value '0', '1' or '-1'
        Position relativePosition = new Position(otherPawnPosition.getX() - selectedPawnPosition.getX(), otherPawnPosition.getY() - selectedPawnPosition.getY());
        //relativePosition times 2 expresses the relative position wrt the position of selectedPawn that has to be checked. The absolute position that has to be checked is obtained by adding the relative position to the selectedPawnPosition
        Position positionToCheck = new Position(selectedPawnPosition.getX() + 2 * relativePosition.getX(), selectedPawnPosition.getY() + 2 * relativePosition.getY());
        if (positionToCheck.getX() < 0 || positionToCheck.getX() > 4 || positionToCheck.getY() < 0 || positionToCheck.getY() > 4) return false;
        Cell cellToCheck = matrixCopy[positionToCheck.getX()][positionToCheck.getY()];
        return cellToCheck.peekBlock() != BlockType.DOME && cellToCheck.getPawn() == null;
    }

    /**
     * Computes the list of cells that a pawn can select in order to perform the special action of Medusa
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of cells that a pawn can select in order to perform the special action of Medusa
     */
    @Override
    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        ArrayList<Position> availableCells = new ArrayList<>();
        if (destroyPawnAndBuildEnable) return availableCells;
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j=0; j<matrixCopy[0].length; j++) {
//              Adds to availableCells the cells adjacent to the selectedPawn which contain an opponent pawn that can be pushed
                if (!(selectedPawnPosition.getX() == i && selectedPawnPosition.getY() == j) && Math.abs(selectedPawnPosition.getX() - i) <= 1  && Math.abs(selectedPawnPosition.getY() - j) <= 1 ) {
                    if (matrixCopy[i][j].getPawn() != null && canPushOpponent(matrixCopy, new Position(i, j)))
                        availableCells.add(new Position(i, j));
                }
            }
        }


        return availableCells;
    }

    public Boolean getPushEnable() {
        return pushEnable;
    }

    public Boolean getDestroyPawnAndBuildEnable() {
        return destroyPawnAndBuildEnable;
    }
}
