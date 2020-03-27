package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Position;

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

}
