package it.polimi.ingsw.model.playerstate;

/**
 * Match state ActionState: during a match the player that is currently
 * playing his turn is in this state, the other players are in IdleState
 */
public class ActionState extends PlayerState {
    public ActionState() {
        super(PlayerStateType.ActionState);
    }
    //TODO
}
