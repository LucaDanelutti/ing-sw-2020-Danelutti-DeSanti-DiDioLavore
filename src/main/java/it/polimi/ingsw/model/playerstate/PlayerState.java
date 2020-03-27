package it.polimi.ingsw.model.playerstate;

/**
 * This class represents the state of the player in the game
 */
public abstract class PlayerState {
    final PlayerStateType type;

    public PlayerState(PlayerStateType type) {
        this.type = type;
    }

    public PlayerStateType getType() {
        return type;
    }
}
