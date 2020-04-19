package it.polimi.ingsw.model.playerstate;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;

/**
 * Match state ActionState: during a match the player that is currently
 * playing his turn is in this state, the other players are in IdleState
 */
public class ActionState extends PlayerState {
    private ArrayList<Action> actionList = new ArrayList<>();
    private Action currentAction;
    private Pawn selectedPawn;
    private Pawn unselectedPawn;

    public ActionState(ArrayList<Action> actionList) {
        super(PlayerStateType.ActionState);
        this.setActionList(actionList);
    }

    /**
     * Get method of the variable currentAction, returns the reference of the currentAction
     */
    public Action getCurrentAction() {
        return currentAction;
    }

    /**
     * setCurrentAction method of the variable currentAction, sets the next currentAction
     * based on the current currentAction
     */
    public void setCurrentAction() {
        int index = getCurrentActionIndex() + 1;
        if (0 <= index & index < actionList.size()) {
            currentAction = actionList.get(index);
            if (selectedPawn != null) currentAction.setSelectedPawn(selectedPawn.duplicate());
            if (unselectedPawn != null) currentAction.setNotSelectedPawn(unselectedPawn.duplicate());
        } else {
            currentAction = null;
        }
    }

    /**
     * updatePawns method: updates selectedPawn and unselectedPawn inside this and inside currentAction
     */
    public void updatePawns(Pawn selectedPawn, Pawn unselectedPawn) {
        setSelectedPawnCopy(selectedPawn);
        setUnselectedPawnCopy(unselectedPawn);
        if (currentAction != null) {
            if (selectedPawn != null) {
                currentAction.setSelectedPawn(selectedPawn.duplicate());
            } else {
                currentAction.setSelectedPawn(null);
            }
            if (unselectedPawn != null) {
                currentAction.setNotSelectedPawn(unselectedPawn.duplicate());
            } else {
                currentAction.setNotSelectedPawn(null);
            }
        }
    }

    /**
     * addActionAfterCurrentOne method: adds the provided Action after the current one
     */
    public void addActionAfterCurrentOne(Action action) {
        int index = getCurrentActionIndex() + 1;
        actionList.add(index, action.duplicate());
    }

    private int getCurrentActionIndex() {
        int index = -1;
        if (currentAction == null) {
            index = -1;
        } else {
            for (int i=0; i < actionList.size(); i++) {
                if (actionList.get(i) == currentAction) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                throw new InvalidGameException("Invalid actionState!");
            }
        }
        return index;
    }

    /**
     * Get method of the variable actionList, returns a copy of the actionList
     */
    public ArrayList<Action> getActionListCopy() {
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
     * Get method of the variable actionList, returns the reference of the actionList
     */
    public ArrayList<Action> getActionList() {
        return actionList;
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
            throw new InvalidGameException("Invalid actionState!");
        }
    }

    /**
     * Get method of the variable selectedPawn, returns a copy of the selectedPawn
     */
    public Pawn getSelectedPawnCopy() {
        return selectedPawn.duplicate();
    }

    /**
     * Set method of the variable selectedPawn, sets a copy of provided selectedPawn
     */
    public void setSelectedPawnCopy(Pawn selectedPawn) {
        if (selectedPawn != null) {
            this.selectedPawn = selectedPawn.duplicate();
        } else {
            this.selectedPawn = null;
        }
    }

    /**
     * Get method of the variable unselectedPawn, returns a copy of the unselectedPawn
     */
    public Pawn getUnselectedPawnCopy() {
        return unselectedPawn.duplicate();
    }

    /**
     * Set method of the variable unselectedPawn, sets a copy of provided unselectedPawn
     */
    public void setUnselectedPawnCopy(Pawn unselectedPawn) {
        if (unselectedPawn != null) {
            this.unselectedPawn = unselectedPawn.duplicate();
        } else {
            this.unselectedPawn = null;
        }
    }
}
