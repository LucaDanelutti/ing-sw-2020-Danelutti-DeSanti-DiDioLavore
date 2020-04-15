package it.polimi.ingsw.model.playerstate;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;

/**
 * Match state ActionState: during a match the player that is currently
 * playing his turn is in this state, the other players are in IdleState
 */
public class ActionState extends PlayerState {
    private ArrayList<Action> actionList;
    private Action currentAction;
    private Pawn selectedPawn;

    public ActionState(ArrayList<Action> actionList) {
        super(PlayerStateType.ActionState);
        this.setActionList(actionList);
    }

    /**
     * Get method of the variable currentAction, returns a copy of the currentAction
     */
    public Action getCurrentAction() {
        return currentAction.duplicate();
    }

    /**
     * Set method of the variable currentAction, sets a copy of provided currentAction
     */
    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction.duplicate();
    }

    /**
     * Get method of the variable actionList, returns a copy of the actionList
     */
    public ArrayList<Action> getActionList() {
        if (actionList == null) {
            return null;
        } else {
            ArrayList<Action> copiedActionList = new ArrayList<>();
            for (Action action : actionList) {
                copiedActionList.add(action.duplicate());
            }
            return copiedActionList;
        }
    }

    /**
     * Set method of the variable actionList, sets a copy of provided actionList
     */
    public void setActionList(ArrayList<Action> actionList) {
        if (actionList != null) {
            for (Action action : actionList) {
                this.actionList.add(action.duplicate());
            }
        } else {
            this.actionList = null;
        }
    }

    /**
     * Get method of the variable selectedPawn, returns a copy of the selectedPawn
     */
    public Pawn getSelectedPawn() {
        return selectedPawn.duplicate();
    }

    /**
     * Set method of the variable selectedPawn, sets a copy of provided selectedPawn
     */
    public void setSelectedPawn(Pawn selectedPawn) {
        this.selectedPawn = selectedPawn.duplicate();
    }

    public ArrayList<Pawn> availablePawns(Cell[][] matrixCopy, ArrayList<Pawn> playerPawnList) {
        //TODO
        return new ArrayList<Pawn>();
    }
}
