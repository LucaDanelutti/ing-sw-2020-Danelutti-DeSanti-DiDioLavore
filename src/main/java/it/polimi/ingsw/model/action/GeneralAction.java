package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;

public class GeneralAction extends Action{
    private Boolean pushEnable;
    private Boolean destroyPawnAndBuildEnable;

    public GeneralAction(Boolean isOptional, ArrayList<Position> notAvailableCell,Boolean pushEnable, Boolean destroyPawnAndBuildEnable){
        super(isOptional,notAvailableCell,ActionType.GENERAL);
        this.destroyPawnAndBuildEnable=destroyPawnAndBuildEnable;
        this.pushEnable=pushEnable;
    }
    GeneralAction(GeneralAction toBeCopied){
       this(toBeCopied.isOptional,toBeCopied.notAvailableCell,toBeCopied.pushEnable,toBeCopied.destroyPawnAndBuildEnable);
    }
    public GeneralAction duplicate(){
        return new GeneralAction(this);
    }
    public void accept(ActionVisitor visitor){
        visitor.executeAction(this);
    }

    @Override
    public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        //TODO: implement!
        return null;
    }

    public Boolean getPushEnable() {
        return pushEnable;
    }

    public Boolean getDestroyPawnAndBuildEnable() {
        return destroyPawnAndBuildEnable;
    }
}
