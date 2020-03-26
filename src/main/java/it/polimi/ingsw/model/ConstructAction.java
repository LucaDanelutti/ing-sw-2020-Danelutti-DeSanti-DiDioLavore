package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ConstructAction extends Action {
    BlockType selectedBlockType;

    ConstructAction(Boolean isOptional, ArrayList<Position> notAvailableCell){
        //TODO: expand
        super(isOptional,notAvailableCell,ActionType.CONSTRUCT);
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
