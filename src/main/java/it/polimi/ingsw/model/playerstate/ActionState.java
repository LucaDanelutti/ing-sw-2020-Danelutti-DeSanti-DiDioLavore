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
        this.actionList = actionList;
    }

    public ArrayList<Action> getActionList() {
        return actionList;
    }

    public void setActionList(ArrayList<Action> actionList) {
        this.actionList = actionList;
    }

    public Pawn getSelectedPawn() {
        return selectedPawn;
    }

    public void setSelectedPawn(Pawn selectedPawn) {
        this.selectedPawn = selectedPawn;
    }

    public ArrayList<Pawn> availablePawns(Cell[][] matrixCopy, ArrayList<Pawn> playerPawnList) {
        //TODO
        return new ArrayList<Pawn>();
    }
}
