package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.playerstate.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for Game
 */
class GameTest {
    Game testGame;
    Player testPlayer1;
    Player testPlayer2;
    Player testPlayer3;

    @BeforeEach
    void init() {
        testGame = new Game();
        testPlayer1 = new Player("testPlayer", new IdleState());
        testGame.addPlayer(testPlayer1);
        testPlayer2 = new Player("testPlayer2", new IdleState());
        testGame.addPlayer(testPlayer2);
        testPlayer3 = new Player("testPlayer3", new ActionState(new ArrayList<Action>()));
        testGame.addPlayer(testPlayer3);
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
        assertTrue(testGame.getPlayers().contains(testPlayer1));
        testGame.removePlayer(testPlayer1);
        assertFalse(testGame.getPlayers().contains(testPlayer1));
    }

    /**
     * The scope of this test function is to test that getNextActionStatePlayer method returns the right player
     */
    @Test
    void getNextActionStatePlayer_3PlayersInGame() {
        assertSame(testPlayer1, testGame.getNextActionStatePlayer());
    }

    /**
     * The scope of this test function is to test that getNextActionStatePlayer method returns the right player
     */
    @Test
    void getNextActionStatePlayer_2PlayersInGame() {
        testGame.removePlayer(testPlayer1);
        assertSame(testPlayer2, testGame.getNextActionStatePlayer());
    }

    /**
     * The scope of this test function is to test that getNextActionStatePlayer method returns the right player
     */
    @Test
    void getNextActionStatePlayer_NoPlayerAvailable() {
        testPlayer1.setState(new WaitingOtherPlayersState());
        testPlayer2.setState(new WaitingOtherPlayersState());
        testPlayer3.setState(new WaitingOtherPlayersState());
        assertThrows(InvalidGameException.class, () -> {testGame.getNextActionStatePlayer();});
    }

    /**
     * The scope of this test function is to test that getNextActionStatePlayer method returns the right player
     */
    @Test
    void getPlayersIn() {
        //Case 1
        assertTrue(testGame.getPlayersIn(PlayerStateType.IdleState).contains(testPlayer1));
        assertTrue(testGame.getPlayersIn(PlayerStateType.IdleState).contains(testPlayer2));
        assertFalse(testGame.getPlayersIn(PlayerStateType.IdleState).contains(testPlayer3));

        //Case 2
        assertFalse(testGame.getPlayersIn(PlayerStateType.ActionState).contains(testPlayer1));
        assertFalse(testGame.getPlayersIn(PlayerStateType.ActionState).contains(testPlayer2));
        assertTrue(testGame.getPlayersIn(PlayerStateType.ActionState).contains(testPlayer3));
    }
}