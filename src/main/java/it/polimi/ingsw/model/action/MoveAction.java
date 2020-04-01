package it.polimi.ingsw.model.action;

import java.lang.Math;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;

//TODO: capire se e come gestire le NullPointerException nei costruttori
//TODO: implementare le funzioni mancanti
public class MoveAction extends Action {
    private Boolean moveUpEnable;
    private Boolean swapEnable;
    private Boolean moveOnOpponentEnable;
    private Boolean pushEnable;
    private Boolean denyMoveUpEnable;
    private Boolean winDownEnable;
    private ArrayList<Position> addMoveIfOn;

    /**
     Constructor of MoveAction: calls the constructor of the superclass and sets the other parameters
     */
    MoveAction(Boolean isOptional, ArrayList<Position> notAvailableCell, Boolean moveUpEnable, Boolean swapEnable, Boolean moveOnOpponentEnable, Boolean pushEnable, Boolean denyMoveUpEnable, Boolean winDownEnable, ArrayList<Position> addMoveIfOn) {
        super(isOptional, notAvailableCell, ActionType.MOVE);
        this.moveUpEnable = moveUpEnable;
        this.swapEnable = swapEnable;
        this.moveOnOpponentEnable = moveOnOpponentEnable;
        this.pushEnable = pushEnable;
        this.denyMoveUpEnable = denyMoveUpEnable;
        this.winDownEnable = winDownEnable;
        if (addMoveIfOn != null) {
            this.addMoveIfOn = new ArrayList<>(addMoveIfOn);
        } else {
            this.addMoveIfOn = null;
        }
    }

    /**
     * This function is the copy constructor for the class MoveAction
     * By using this method, there is no need to implement Clonable
     */
    MoveAction(MoveAction toBeCopied) {
        this(toBeCopied.isOptional, toBeCopied.notAvailableCell,  toBeCopied.moveUpEnable, toBeCopied.swapEnable, toBeCopied.moveOnOpponentEnable, toBeCopied.pushEnable, toBeCopied.denyMoveUpEnable, toBeCopied.winDownEnable, toBeCopied.addMoveIfOn);
    }

    public void accept(ActionVisitor visitor){
        visitor.executeAction(this);
    }

    /**
     * This function creates a duplicate of this.
     * @return MoveAction
     */
    public MoveAction duplicate() {
        return new MoveAction(this);
    }

    public Boolean getMoveUpEnable() {
        return moveUpEnable;
    }

    public Boolean getSwapEnable() {
        return swapEnable;
    }

    public Boolean getMoveOnOpponentEnable() {
        return moveOnOpponentEnable;
    }

    public Boolean getPushEnable() {
        return pushEnable;
    }

    public Boolean getDenyMoveUpEnable() {
        return denyMoveUpEnable;
    }

    public Boolean getWinDownEnable() {
        return winDownEnable;
    }

    public ArrayList<Position> getAddMoveIfOn() {
        return addMoveIfOn;
    }

    public void setMoveUpEnable(Boolean moveUpEnable) {
        this.moveUpEnable = moveUpEnable;
    }


    /**
     * Computes a boolean value expressing whether the pawn can move on a cell occupied by an enemy pawn
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     * @return the list of available cells to which the pawn selected can move
     */
    private Boolean canMoveOnOpponent(Cell[][] matrixCopy, Position selectedPawnPosition, Position enemyPawnPosition) {
        if (!swapEnable && !pushEnable) return false;
        if (swapEnable) return true;
        //relativePosition expresses the position of enemyPawn wrt the position of selectedPawn: its coordinates can value '0', '1' or '-1'
        Position relativePosition = new Position(enemyPawnPosition.getX() - selectedPawnPosition.getX(), enemyPawnPosition.getY() - selectedPawnPosition.getY());
        //relativePosition times 2 expresses the relative position wrt the position of selectedPawn that has to be checked. The absolute position that has to be checked is obtained by adding the relative position to the selectedPawnPosition
        Position positionToCheck = new Position(selectedPawnPosition.getX() + 2 * relativePosition.getX(), selectedPawnPosition.getY() + 2 * relativePosition.getY());
        Cell cellToCheck = matrixCopy[positionToCheck.getX()][positionToCheck.getY()];
        return cellToCheck.peekBlock() != BlockType.DOME && cellToCheck.getPawn() == null;
    }

    /**
     * Computes the list of cells to which a pawn can move
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available cells to which the pawn selected can move
     */
    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        ArrayList<Position> availableCells = new ArrayList<>();
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j=0; j<matrixCopy[0].length; j++) {
                Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
//              Selects only the cells adjacent to the selectedPawn (excluding the one occupied by the selectedPawn)
                if (!(selectedPawnPosition.getX() == i && selectedPawnPosition.getY() == j) && Math.abs(selectedPawnPosition.getX() - i) <= 1  && Math.abs(selectedPawnPosition.getY() - j) <= 1 ) {
//                  Computes the deltaHeight between the cell in which the selectedPawn is currently located and the prospective one
                    int signedDeltaHeight = matrixCopy[i][j].getSize() - matrixCopy[selectedPawnPosition.getX()][selectedPawnPosition.getY()].getSize();
                    if ((signedDeltaHeight < 1 || (signedDeltaHeight == 1 && moveUpEnable)) && matrixCopy[i][j].peekBlock() != BlockType.DOME ) {
//                      Manages the presence of a pawn on the prospective cell
                        if (matrixCopy[i][j].getPawn() == null || (matrixCopy[i][j].getPawn() != notSelectedPawn && canMoveOnOpponent(matrixCopy, selectedPawnPosition, new Position(i,j)))) {
                            if (!notAvailableCell.contains(new Position(i, j))) {
                                availableCells.add(new Position(i,j));
                            }
                        }
                    }
                }
            }
        }

        return availableCells;
    }


    /**
     * Checks whether the last moveAction executed upon the selectedPawn makes the player win. This can happen in two cases:
     * (1)the pawn has reached a level3 cell or (2) winDownEnable = true and the pawn went down at least 2 levels during the moveAction.
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available cells to which the pawn selected can move
     */
    public Boolean checkWin(Cell[][] matrixCopy) {
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        if (matrixCopy[selectedPawnPosition.getX()][selectedPawnPosition.getY()].peekBlock() == BlockType.LEVEL3) return true;
        return winDownEnable && selectedPawn.getDeltaHeight() <= -2;
    }

}
