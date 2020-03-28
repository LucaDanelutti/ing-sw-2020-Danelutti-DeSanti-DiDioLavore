package it.polimi.ingsw.model;

import it.polimi.ingsw.model.playerstate.PlayerStateType;
import it.polimi.ingsw.model.playerstate.WaitingOtherPlayersState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for Game
 */
class GameTest {
    Game testGame;
    Player testPlayer;

    @BeforeEach
    void init() {
        testGame = new Game();
        testPlayer = new Player("testPlayer", new WaitingOtherPlayersState());
        testGame.addPlayer(testPlayer);
    }

    /**
     * The scope of this test function is to test that addPlayer method adds the provided Player to the Game
     */
    @Test
    void addPlayer() {
        Player localTestPlayer = new Player("localTestPlayer", new WaitingOtherPlayersState());
        assertFalse(testGame.getPlayers().contains(localTestPlayer));
        testGame.addPlayer(localTestPlayer);
        assertTrue(testGame.getPlayers().contains(localTestPlayer));
    }

    /**
     * The scope of this test function is to test that removePlayer method removes the provided Player from the Game
     */
    @Test
    void removePlayer() {
        assertTrue(testGame.getPlayers().contains(testPlayer));
        testGame.removePlayer(testPlayer);
        assertFalse(testGame.getPlayers().contains(testPlayer));
    }
}