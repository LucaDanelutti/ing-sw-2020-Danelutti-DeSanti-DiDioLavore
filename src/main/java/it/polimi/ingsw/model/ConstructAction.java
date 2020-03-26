package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ConstructAction extends Action {
    BlockType selectedBlockType;

    ConstructAction(Boolean isOptional, ArrayList<Position> notAvailableCell, ActionType actionType){
        //TODO: expand
        super(isOptional,notAvailableCell,actionType);
        this.selectedBlockType=null;
    }

    @Override
    public Action duplicate() {
        //TODO: implement
        return null;
    }

    public BlockType getSelectedBlockType() {
        return selectedBlockType;
    }

    public void setSelectedBlockType(BlockType selectedBlockType) {
        this.selectedBlockType = selectedBlockType;
    }
}
