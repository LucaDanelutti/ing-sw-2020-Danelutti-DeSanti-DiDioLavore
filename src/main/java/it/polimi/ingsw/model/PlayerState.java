package it.polimi.ingsw.model;

/**
 * This class represents the state of the player in the game
 */
abstract class PlayerState {
    final PlayerStateType type;

    public PlayerState(PlayerStateType type) {
        this.type = type;
    }

    public PlayerStateType getType() {
        return type;
    }
}
