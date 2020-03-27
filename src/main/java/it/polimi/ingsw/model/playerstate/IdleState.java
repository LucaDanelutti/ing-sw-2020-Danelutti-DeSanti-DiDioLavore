package it.polimi.ingsw.model.playerstate;

/**
 * Match state IdleState: during a match every player is in
 * this state except the one that is currently playing his turn
 */
public class IdleState extends PlayerState {
    public IdleState() {
        super(PlayerStateType.IdleState);
    }
    //TODO
}
