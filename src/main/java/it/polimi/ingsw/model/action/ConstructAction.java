package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;
import java.util.Objects;

public class ConstructAction extends Action {
    private BlockType selectedBlockType;
    private Boolean constructOnLastBuilt;
    private Boolean buildBelowEnable;
    private ArrayList<BlockType> alwaysAvailableBlockType;
    private Boolean disableMoveUp;
    private Boolean notBuildOnLastBuilt;



    public void acceptForExecution(ActionVisitor actionVisitor){
        actionVisitor.executeAction(this);
    }
    public void acceptForProcess(ActionVisitor actionVisitor){
        actionVisitor.processAction(this);
    }




    public ConstructAction(Boolean isOptional, ArrayList<Position> notAvailableCell, Boolean buildBelowEnable, ArrayList<BlockType> alwaysAvailableBlockType, Boolean constructOnLastBuilt, Boolean disableMoveUp, Boolean notBuildOnLastBuilt){
        super(isOptional,notAvailableCell);
        this.constructOnLastBuilt=constructOnLastBuilt;
        this.buildBelowEnable=buildBelowEnable;
        this.alwaysAvailableBlockType=alwaysAvailableBlockType;
        this.disableMoveUp=disableMoveUp;
        this.notBuildOnLastBuilt=notBuildOnLastBuilt;
    }

    ConstructAction(ConstructAction toBeCopied) {
        super(toBeCopied.isOptional, toBeCopied.notAvailableCell, toBeCopied.selectedPawn, toBeCopied.notSelectedPawn);
        this.constructOnLastBuilt = toBeCopied.constructOnLastBuilt;
        this.buildBelowEnable = toBeCopied.buildBelowEnable;
        if(toBeCopied.alwaysAvailableBlockType!=null){
            this.alwaysAvailableBlockType=new ArrayList<>();
            this.alwaysAvailableBlockType.addAll(toBeCopied.alwaysAvailableBlockType);
        }
        else{
            this.alwaysAvailableBlockType=null;
        }
        this.alwaysAvailableBlockType = toBeCopied.alwaysAvailableBlockType;
        this.disableMoveUp = toBeCopied.disableMoveUp;
        this.notBuildOnLastBuilt = toBeCopied.notBuildOnLastBuilt;
        this.selectedBlockType = toBeCopied.selectedBlockType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstructAction that = (ConstructAction) o;
        return selectedBlockType == that.selectedBlockType &&
                Objects.equals(constructOnLastBuilt, that.constructOnLastBuilt) &&
                Objects.equals(buildBelowEnable, that.buildBelowEnable) &&
                Objects.equals(alwaysAvailableBlockType, that.alwaysAvailableBlockType) &&
                Objects.equals(disableMoveUp, that.disableMoveUp) &&
                Objects.equals(notBuildOnLastBuilt, that.notBuildOnLastBuilt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectedBlockType, constructOnLastBuilt, buildBelowEnable, alwaysAvailableBlockType, disableMoveUp);
    }



    public Boolean getConstructOnLastBuilt() {
        return constructOnLastBuilt;
    }
    public Boolean getBuildBelowEnable() {
        return buildBelowEnable;
    }
    public ArrayList<BlockType> getAlwaysAvailableBlockType() {
        return alwaysAvailableBlockType;
    }
    public Boolean getDisableMoveUp() {
        return disableMoveUp;
    }


    @Override
    public ConstructAction duplicate() {
        return new ConstructAction(this);
    }

    public void setChosenPosition(Position chosenPosition) {
        this.chosenPosition = chosenPosition;
    }

    public BlockType getSelectedBlockType() {
        return selectedBlockType;
    }


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
     * Removes from availableCells the position where selectedPawn can't construct because it's its lastBuildPosition and notBuildOnLastBuilt=true
     * @param availableCells is the current list of cells where the selectedPawn can construct
     */
    private void checkNotBuildOnLastBuilt(ArrayList<Position> availableCells) {
        if (notBuildOnLastBuilt) availableCells.remove(selectedPawn.getLastBuildPosition());
    }

    /**
     * Computes the list of cells in which a pawn can construct
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available cells in which the pawn selected can construct
     */
    @Override
    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        ArrayList<Position> availableCells = new ArrayList<>();
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        if (constructOnLastBuilt) {
            availableCells.add(selectedPawn.getLastBuildPosition());
            return availableCells;
        }

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
        checkNotBuildOnLastBuilt(availableCells);

        return availableCells;
    }

    /**
     * Computes the list of BlockType that can be selected by the player to build in the selected position
     * @param selectedPosition is a position where the player may decide to build one of the returned block types
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available block types that can be selected by the player to build in the selected position
     */
    public ArrayList<BlockType> availableBlockTypes(Position selectedPosition, Cell[][] matrixCopy) {
        ArrayList<BlockType> availableBlockTypes = new ArrayList<>();
        availableBlockTypes.add(matrixCopy[selectedPosition.getX()][selectedPosition.getY()].peekBlock().getLevelAbove());
        for (BlockType blockType : alwaysAvailableBlockType) {
            if (!availableBlockTypes.contains(blockType)) availableBlockTypes.add(blockType);
        }
        return availableBlockTypes;
    }

    public void blockSelected(BlockType blockType) {
        this.selectedBlockType=blockType;
    }

    public void disablePerimeterWin() {
    }

    public void disableClimbing() {
    }
}
