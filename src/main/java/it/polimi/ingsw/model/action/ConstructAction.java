package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.Position;

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
}
