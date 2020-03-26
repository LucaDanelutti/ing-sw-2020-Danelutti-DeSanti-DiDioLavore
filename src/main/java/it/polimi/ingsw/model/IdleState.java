package it.polimi.ingsw.model;

/**
 * Match state IdleState: during a match every player is in
 * this state except the one that is currently playing his turn
 */
class IdleState extends PlayerState {
    public IdleState() {
        super(PlayerStateType.IdleState);
    }
    //TODO
}
