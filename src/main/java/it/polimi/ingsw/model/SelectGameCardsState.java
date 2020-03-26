package it.polimi.ingsw.model;

/**
 * Setup state SelectGameCardsState: the most "god-like" player is set in this state,
 * he has to choose k God cards, where k is the number of players in the game
 */
class SelectGameCardsState extends PlayerState {
    public SelectGameCardsState() {
        super(PlayerStateType.SelectGameCardsState);
    }
    //TODO
}
