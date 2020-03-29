package it.polimi.ingsw.model.playerstate;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;

/**
 * Match state IdleState: during a match every player is in
 * this state except the one that is currently playing his turn
 */
public class IdleState extends PlayerState {


    private ArrayList<Action> actionList;
    public IdleState() {
        super(PlayerStateType.IdleState);
    }
    public ArrayList<Action> getActionList() {
        return actionList;
    }

    public void setActionList(ArrayList<Action> actionList) {
        this.actionList = actionList;
    }

    //TODO
}
