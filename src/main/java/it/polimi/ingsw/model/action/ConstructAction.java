package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;

public class ConstructAction extends Action {
    private BlockType selectedBlockType;
    private Position onlyAvailableCell;
    private Boolean buildBelowEnable;
    private ArrayList<BlockType> alwaysAvailableBlockType;
    private Boolean enableMoveUp;

    ConstructAction(Boolean isOptional, ArrayList<Position> notAvailableCell, BlockType selectedBlockType, Boolean buildBelowEnable, ArrayList<BlockType> alwaysAvailableBlockType, Position onlyAvailableCell, Boolean enableMoveUp){
        super(isOptional,notAvailableCell,ActionType.CONSTRUCT);
        this.selectedBlockType=selectedBlockType;
        this.onlyAvailableCell=onlyAvailableCell;
        this.buildBelowEnable=buildBelowEnable;
        this.alwaysAvailableBlockType=alwaysAvailableBlockType;
        this.enableMoveUp=enableMoveUp;
    }

    ConstructAction(ConstructAction toBeCopied) {
        this(toBeCopied.isOptional, toBeCopied.notAvailableCell, toBeCopied.selectedBlockType, toBeCopied.buildBelowEnable, toBeCopied.alwaysAvailableBlockType, toBeCopied.onlyAvailableCell, toBeCopied.enableMoveUp);
    }

    public void accept(ActionVisitor actionVisitor){
        actionVisitor.executeAction(this);
    }

    public Position getOnlyAvailableCell() {
        return onlyAvailableCell;
    }
    public Boolean getBuildBelowEnable() {
        return buildBelowEnable;
    }
    public ArrayList<BlockType> getAlwaysAvailableBlockType() {
        return alwaysAvailableBlockType;
    }
    public Boolean getEnableMoveUp() {
        return enableMoveUp;
    }


    @Override
    public ConstructAction duplicate() {
        return new ConstructAction(this);
    }

    public BlockType getSelectedBlockType() {
        return selectedBlockType;
    }

    /**
     * we are in a Construct Action, so once chosenPosition and blockType are set, an update should be thrown
     * to the GameLogic
     * @param selectedBlockType the selected blocktype returned by avaiableBlockTypes
     */
    public void setSelectedBlockType(BlockType selectedBlockType) {
        this.selectedBlockType = selectedBlockType;
        for(ActionObserver actionObserver: this.actionObservers){
            actionObserver.update(this);
        }
    }

    /**
     * Removes from availableCells the positions present within notAvailableCell
     * @param availableCells is the current list of cells where the selectedPawn can construct
     */
    private void checkNotAvailableCells(ArrayList<Position> availableCells) {
        for (Position position : notAvailableCell) {
            if (availableCells.contains(position)) availableCells.remove(position);
        }
    }

    /**
     * Removes from availableCells the positions where a dome is placed
     * @param availableCells is the current list of cells where the selectedPawn can construct
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     */
    private void checkDomePresence(ArrayList<Position> availableCells, Cell[][] matrixCopy) {
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j = 0; j < matrixCopy[0].length; j++) {
                if (availableCells.contains(new Position(i, j))) {
                    if (matrixCopy[i][j].peekBlock() == BlockType.DOME) availableCells.remove(new Position(i, j));
                }
            }
        }
    }

    /**
     * Removes from availableCells the position where selectedPawn can't construct because of the presence of a pawn
     * @param availableCells is the current list of cells where the selectedPawn can construct
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     */
    private void checkPawnPresence(ArrayList<Position> availableCells, Cell[][] matrixCopy) {
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j = 0; j < matrixCopy[0].length; j++) {
                if (availableCells.contains(new Position(i, j))) {
                    if (matrixCopy[i][j].getPawn() != null) {
                        if (!matrixCopy[i][j].getPawn().getPosition().equals(selectedPawn.getPosition()) || !buildBelowEnable) availableCells.remove(new Position(i, j));
                    }
                }
            }
        }
    }

    /**
     * Computes the list of cells in which a pawn can construct
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available cells in which the pawn selected can construct
     */
    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        ArrayList<Position> availableCells = new ArrayList<>();
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j=0; j<matrixCopy[0].length; j++) {
                //Adds to availableCells the cells adjacent to the selectedPawn including the one occupied by the selectedPawn
                if (Math.abs(selectedPawnPosition.getX() - i) <= 1  && Math.abs(selectedPawnPosition.getY() - j) <= 1 ) {
                    availableCells.add(new Position(i, j));
                }
            }
        }

        checkNotAvailableCells(availableCells);
        checkDomePresence(availableCells, matrixCopy);
        checkPawnPresence(availableCells, matrixCopy);

        return availableCells;
    }

}
