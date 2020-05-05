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
        testPlayer1 = new Player("testPlayer1");
        testGame.addPlayer(testPlayer1);
        testPlayer2 = new Player("testPlayer2");
        testGame.addPlayer(testPlayer2);
        testPlayer3 = new Player("testPlayer3");
        testGame.addPlayer(testPlayer3);
        testGame.setCurrentPlayer(testPlayer3);
    }

    /**
     * The scope of this test function is to test that addPlayer method adds the provided Player to the Game
     */
    @Test
    void addPlayer() {
        testGame.removePlayer(testPlayer1);
        testGame.removePlayer(testPlayer2);
        testGame.removePlayer(testPlayer3);
        Player localTestPlayer = new Player("localTestPlayer");
        assertFalse(testGame.getPlayers().contains(localTestPlayer));
        assertTrue(testGame.addPlayer(localTestPlayer));
        assertTrue(testGame.getPlayers().contains(localTestPlayer));
    }

    /**
     * The scope of this test function is to test that addPlayer method does not add a player with same name as an inGamePlayer
     */
    @Test
    void addPlayer_SameName() {
        testGame.removePlayer(testPlayer3);
        Player localTestPlayer = new Player("testPlayer1");
        assertFalse(testGame.getPlayers().contains(localTestPlayer));
        assertFalse(testGame.addPlayer(localTestPlayer));
        assertFalse(testGame.getPlayers().contains(localTestPlayer));
    }

    /**
     * The scope of this test function is to test that addPlayer method throws an exception if the game already has 3 players
     */
    @Test
    void addPlayer_FullGame() {
        Player localTestPlayer = new Player("localTestPlayer");
        assertFalse(testGame.getPlayers().contains(localTestPlayer));
        assertThrows(InvalidGameException.class, () -> {testGame.addPlayer(localTestPlayer);});
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
     * The scope of this test function is to test that getNextPlayer method returns the right player
     */
    @Test
    void getNextPlayer_NoNextPlayer_ActionState() {
        testPlayer1.setLoser(true);
        testPlayer2.setLoser(true);
        assertFalse(testGame.areThereAnyNonLoserPlayersLeft());
    }

    /**
     * The scope of this test function is to test that getNextPlayer method returns the right player
     */
    @Test
    void getNextPlayer_3PlayersInGame_PlayerState() {
        assertSame(testPlayer1, testGame.getNextPlayer());
    }

    /**
     * The scope of this test function is to test that getNextPlayer method returns the right player
     */
    @Test
    void getNextPlayer_2PlayersInGame_PlayerState() {
        testGame.removePlayer(testPlayer1);
        assertSame(testPlayer2, testGame.getNextPlayer());
    }


    /**
     * The scope of this test function is to test that getNextPlayer method returns the right player
     */
    /*@Test
    void getNextPlayer_3PlayersInGame_ChooseCardState() {
        testPlayer3.setState(new ChooseCardState());
        assertSame(testPlayer1, testGame.getNextPlayer(PlayerStateType.ChooseCardState));
    }*/

    /**
     * The scope of this test function is to test that getNextPlayer method returns the right player
     */
   /* @Test
    void getNextPlayer_2PlayersInGame_ChooseCardState() {
        testGame.removePlayer(testPlayer1);
        testPlayer3.setState(new ChooseCardState());
        assertSame(testPlayer2, testGame.getNextPlayer(PlayerStateType.ChooseCardState));
    }*/

    /**
     * The scope of this test function is to test that getNextPlayer method returns the right player
     */
    /*@Test
    void getNextPlayer_NoCurrentPlayer_PlayerState() {
        testPlayer1.setState(new HostWaitOtherPlayersState());
        testPlayer2.setState(new ClientWaitStartState());
        testPlayer3.setState(new ClientWaitStartState());
        assertThrows(InvalidGameException.class, () -> {testGame.getNextPlayer();});
    }*/

    /**
     * The scope of this test function is to test that getNextActionStatePlayer method returns the right player
     */
   /* @Test
    void getPlayersIn_MultipleIdleState() {
        assertTrue(testGame.getPlayersIn(PlayerStateType.IdleState).contains(testPlayer1));
        assertTrue(testGame.getPlayersIn(PlayerStateType.IdleState).contains(testPlayer2));
        assertFalse(testGame.getPlayersIn(PlayerStateType.IdleState).contains(testPlayer3));
    }*/

    /**
     * The scope of this test function is to test that getNextActionStatePlayer method returns the right player
     */
   /* @Test
    void getPlayersIn_ActionState() {
        assertFalse(testGame.getPlayersIn(PlayerStateType.ActionState).contains(testPlayer1));
        assertFalse(testGame.getPlayersIn(PlayerStateType.ActionState).contains(testPlayer2));
        assertTrue(testGame.getPlayersIn(PlayerStateType.ActionState).contains(testPlayer3));
    }*/

    /**
     * The scope of this test function is to test that getNextActionStatePlayer method returns the right player
     */
    /*@Test
    void getPlayersIn_MultipleActionState() {
        testPlayer1.setState(new ActionState(new ArrayList<Action>()));
        assertThrows(InvalidGameException.class, () -> {testGame.getPlayersIn(PlayerStateType.ActionState);});
    }*/

    /**
     * Usefull only for debugging shufflePlayers
     */
    @Test
    void shufflePlayers() {
        testGame.shufflePlayers();
    }

    /**
     * The scope of this test function is to test that setFirstPlayer method sets correctly the first player of the game
     */
    @Test
    void setFirstPlayer_First() {
        testGame.setFirstPlayer(testPlayer1);
        assertSame(testPlayer1, testGame.getPlayers().get(0));
    }

    /**
     * The scope of this test function is to test that setFirstPlayer method sets correctly the first player of the game
     */
    @Test
    void setFirstPlayer_NotFirst() {
        testGame.setFirstPlayer(testPlayer2);
        assertSame(testPlayer2, testGame.getPlayers().get(0));
    }

    /**
     * The scope of this test function is to test that getLoadedCardCopy method returns the right card
     */
    @Test
    void getLoadedCardCopy() {
        ArrayList<Card> testCardArray = new ArrayList<>();
        Card testCard1 = new Card("Card1", 1, new ArrayList<Action>());
        Card testCard2 = new Card("Card2", 2, new ArrayList<Action>());
        testCardArray.add(testCard1);
        testCardArray.add(testCard2);
        testGame.setLoadedCardsCopy(testCardArray);
        assertEquals(testCard1, testGame.getLoadedCardCopy(1));
        assertEquals(testCard2, testGame.getLoadedCardCopy(2));
        assertEquals(null, testGame.getLoadedCardCopy(3));
    }
}