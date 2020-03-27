package it.polimi.ingsw.model.playerstate;

/**
 * Final state LoserState: a player that looses the match is thrown in
 * this state, for example when he has no available pawn to move
 */
public class LoserState extends PlayerState {
    public LoserState() {
        super(PlayerStateType.LoserState);
    }
    //TODO
}
