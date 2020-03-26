package it.polimi.ingsw.model;

/**
 * Final state WinnerState: the player that wins the match is thrown
 * in this state, for example when he moves up from a level 2 to a level 3
 */
class WinnerState extends PlayerState {
    public WinnerState() {
        super(PlayerStateType.WinnerState);
    }
    //TODO
}
