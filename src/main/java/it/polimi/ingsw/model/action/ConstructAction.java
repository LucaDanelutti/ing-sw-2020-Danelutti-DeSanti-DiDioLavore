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
    public Action duplicate() {
        //TODO: implement
        return null;
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
        for(Observer observer: this.observers){
            observer.update(this);
        }
    }

    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        return new ArrayList<>();
    }

}
