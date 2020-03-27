package it.polimi.ingsw.model.playerstate;

/**
 * Setup state SelectGameCardsState: the most "god-like" player is set in this state,
 * he has to choose k God cards, where k is the number of players in the game
 */
public class SelectGameCardsState extends PlayerState {
    public SelectGameCardsState() {
        super(PlayerStateType.SelectGameCardsState);
    }
    //TODO
}
