package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ConstructAction;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.playerstate.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicExecutorTest {
    GameLogicExecutor gameLogicExecutor;
    Game game;
    MoveAction basicMove;
    ConstructAction basicConstruct;
    ArrayList<Action> basicActionList1;
    ArrayList<Action> basicActionList2;
    ArrayList<Action> basicActionList3;

    @BeforeEach
    void init() {
        game = new Game();
        gameLogicExecutor= new GameLogicExecutor(game);
        Player p;
        Position p1;
        Position p2;
        basicActionList1 = new ArrayList<>();
        basicActionList2 = new ArrayList<>();
        basicActionList3 = new ArrayList<>();
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        basicActionList1.add(basicMove);
        basicActionList1.add(basicConstruct);
        basicActionList2.add(basicMove.duplicate());
        basicActionList2.add(basicConstruct.duplicate());
        basicActionList3.add(basicMove.duplicate());
        basicActionList3.add(basicConstruct.duplicate());
        for(Action a : basicActionList1){
            a.addObserver(gameLogicExecutor);
        }
        for(Action a : basicActionList2){
            a.addObserver(gameLogicExecutor);
        }
        for(Action a : basicActionList3){
            a.addObserver(gameLogicExecutor);
        }

        p = new Player("ian", new ActionState(basicActionList1));
        p.addPawn(new Pawn("black"));
        p.addPawn(new Pawn("black"));
        p.setCurrentCard(new Card("Pippo1", 1, basicActionList1));
        game.addPlayer(p);
        p1 = new Position(1, 1);
        p2 = new Position(4, 4);
        game.board.setPawnPosition(p.getPawnList().get(0), p1);
        game.board.setPawnPosition(p.getPawnList().get(1), p2);

        p = new Player("luca", new IdleState());
        p.addPawn(new Pawn("white"));
        p.addPawn(new Pawn("white"));
        p.setCurrentCard(new Card("Pippo2", 2, basicActionList2));
        game.addPlayer(p);
        p1 = new Position(1, 2);
        p2 = new Position(4, 3);
        game.board.setPawnPosition(p.getPawnList().get(0), p1);
        game.board.setPawnPosition(p.getPawnList().get(1), p2);

        p = new Player("riccardo", new IdleState());
        p.addPawn(new Pawn("green"));
        p.addPawn(new Pawn("green"));
        p.setCurrentCard(new Card("Pippo3", 3, basicActionList3));
        game.addPlayer(p);
        p1 = new Position(1, 3);
        p2 = new Position(4, 0);
        game.board.setPawnPosition(p.getPawnList().get(0), p1);
        game.board.setPawnPosition(p.getPawnList().get(1), p2);

    }

    @Test
    void setSelectedPawnTest(){
        Position selectedPawn= game.getPlayer("ian").getPawnList().get(0).getPosition();
        Position notSelectedPawn= game.getPlayer("ian").getPawnList().get(1).getPosition();

        //once we call this function the first action will be loaded from actionList and pawn values will be updated
        gameLogicExecutor.setSelectedPawn(selectedPawn,notSelectedPawn);

        // we should check that the first action is actually loaded
        ActionState actionState = (ActionState) game.getPlayer("ian").getState();
        assertEquals(basicMove,actionState.getCurrentAction());

        //we should check that the pawns are set correctly
        assertEquals(game.getPlayer("ian").getPawnList().get(0),actionState.getCurrentAction().getSelectedPawn());
        assertEquals(game.getPlayer("ian").getPawnList().get(1),actionState.getCurrentAction().getNotSelectedPawn());
    }

    @Test
    void setChosenPositionForMoveActionTest(){
        Pawn selectedPawn=game.getPlayer("ian").getPawnList().get(0);
        Position selectedPawnPos= selectedPawn.getPosition();
        Pawn notSelected = game.getPlayer("ian").getPawnList().get(1);
        Position notSelectedPawnPos= notSelected.getPosition();
        gameLogicExecutor.setSelectedPawn(selectedPawnPos,notSelectedPawnPos);
        ActionState actionState=(ActionState)game.getPlayersIn(PlayerStateType.ActionState).get(0).getState();

        //let's find out the positions available for the selected pawn
        ArrayList<Position> positions=actionState.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());

        //after this function there should be a call (via visitor pattern) to the gameLogicExecutor.executeAction
        //so after this call the pawn should be placed correctly in the board
        gameLogicExecutor.setChosenPosition(positions.get(0));

        //this is a standardMove so let's just check that the pawn moved to the right position
        assertEquals(positions.get(0),selectedPawn.getPosition());

    }

    @Test
    void setChosenPositionForConstructActionTest(){
        Pawn selectedPawn=game.getPlayer("ian").getPawnList().get(0);
        Position selectedPawnPos= selectedPawn.getPosition();
        Pawn notSelected = game.getPlayer("ian").getPawnList().get(1);
        Position notSelectedPawnPos= notSelected.getPosition();
        gameLogicExecutor.setSelectedPawn(selectedPawnPos,notSelectedPawnPos);
        ActionState actionState=(ActionState)game.getPlayersIn(PlayerStateType.ActionState).get(0).getState();

        //this will execute the first action (Move)
        ArrayList<Position> positions=actionState.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(positions.get(0));

        //this will execute the second action (Construct)
        positions=actionState.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(positions.get(0));
        ArrayList<BlockType> blockTypes = ((ConstructAction)actionState.getCurrentAction()).availableBlockTypes(positions.get(0),game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenBlockType(blockTypes.get(0));

        assertEquals(blockTypes.get(0),game.getBoard().getMatrixCopy()[positions.get(0).getX()][positions.get(0).getY()].peekBlock());

    }

    /**
     * The scope of this test function is to test that addPlayer method adds a player with the provided name in the right state
     */
    @Test
    void addPlayer_FirstPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        assertTrue(testGame.getPlayers().size() == 0);
        assertTrue(testGameLogicExecutor.addPlayer("testPlayer1"));
        assertTrue(testGame.getPlayers().size() == 1);
        assertTrue(testGame.getPlayer("testPlayer1").getState().getType() == PlayerStateType.HostWaitOtherPlayersState);
    }

    /**
     * The scope of this test function is to test that addPlayer method adds a player with the provided name in the correct state
     */
    @Test
    void addPlayer_NotFirstPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        testGameLogicExecutor.addPlayer("testPlayer1");
        assertTrue(testGame.getPlayers().size() == 1);
        assertTrue(testGameLogicExecutor.addPlayer("testPlayer2"));
        assertTrue(testGame.getPlayers().size() == 2);
        assertTrue(testGame.getPlayer("testPlayer2").getState().getType() == PlayerStateType.ClientWaitStartState);
    }

    /**
     * The scope of this test function is to test that startGame method correctly starts the game
     */
    @Test
    void startGame_3Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        testGameLogicExecutor.addPlayer("testPlayer1");
        testGameLogicExecutor.addPlayer("testPlayer2");
        testGameLogicExecutor.addPlayer("testPlayer3");
        ArrayList<String> testNameArrays = new ArrayList<>();
        testNameArrays.add("testPlayer1");
        testNameArrays.add("testPlayer2");
        testNameArrays.add("testPlayer3");
        assertTrue(testGameLogicExecutor.startGame());
        assertTrue(testGame.getPlayersIn(PlayerStateType.SelectGameCardsState).size() == 1);
        assertTrue(testNameArrays.contains(testGame.getPlayersIn(PlayerStateType.SelectGameCardsState).get(0).getName()));
        assertTrue(testGame.getPlayersIn(PlayerStateType.IdleState).size() == 2);
    }

    /**
     * The scope of this test function is to test that startGame method correctly starts the game
     */
    @Test
    void startGame_2Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        testGameLogicExecutor.addPlayer("testPlayer1");
        testGameLogicExecutor.addPlayer("testPlayer2");
        ArrayList<String> testNameArrays = new ArrayList<>();
        testNameArrays.add("testPlayer1");
        testNameArrays.add("testPlayer2");
        assertTrue(testGameLogicExecutor.startGame());
        assertTrue(testGame.getPlayersIn(PlayerStateType.SelectGameCardsState).size() == 1);
        assertTrue(testNameArrays.contains(testGame.getPlayersIn(PlayerStateType.SelectGameCardsState).get(0).getName()));
        assertTrue(testGame.getPlayersIn(PlayerStateType.IdleState).size() == 1);
    }

    /**
     * The scope of this test function is to test that startGame method correctly starts the game
     */
    @Test
    void startGame_1Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        testGameLogicExecutor.addPlayer("testPlayer1");
        assertFalse(testGameLogicExecutor.startGame());
    }

    /**
     * The scope of this test function is to test that setChosenCard method correctly sets the player card
     */
    @Test
    void setChosenCard_FirstPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new ChooseCardState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        Card testCard1 = new Card("testCard1", 1, new ArrayList<Action>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<Action>());
        ArrayList<Card> testCardList = new ArrayList<>();
        testCardList.add(testCard1);
        testCardList.add(testCard2);
        testGame.setInGameCardsCopy(testCardList);
        assertTrue(testGameLogicExecutor.setChosenCard(testCard1));
        assertEquals(testCard1, testPlayer1.getCurrentCard());
        assertEquals(PlayerStateType.IdleState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.ChooseCardState, testPlayer2.getState().getType());
    }

    /**
     * The scope of this test function is to test that setChosenCard method correctly sets the player card
     */
    @Test
    void setChosenCard_LastPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new ChooseCardState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        Card testCard1 = new Card("testCard1", 1, new ArrayList<Action>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<Action>());
        ArrayList<Card> testCardList = new ArrayList<>();
        testCardList.add(testCard1);
        testCardList.add(testCard2);
        testGame.setInGameCardsCopy(testCardList);
        assertTrue(testGameLogicExecutor.setChosenCard(testCard1));
        assertTrue(testGameLogicExecutor.setChosenCard(testCard2));
        assertEquals(testCard2, testPlayer2.getCurrentCard());
        assertEquals(PlayerStateType.IdleState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.SelectFirstPlayerState, testPlayer2.getState().getType());
    }

    /**
     * The scope of this test function is to test that setChosenCard method correctly sets the player card
     */
    @Test
    void setChosenCard_WrongCard() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new ChooseCardState());
        testGame.addPlayer(testPlayer1);
        Card testCard1 = new Card("testCard1", 1, new ArrayList<Action>());
        ArrayList<Card> testCardList = new ArrayList<>();
        testCardList.add(testCard1);
        testGame.setInGameCardsCopy(testCardList);
        Card testCard2 = new Card("testCard2", 2, new ArrayList<Action>());
        assertFalse(testGameLogicExecutor.setChosenCard(testCard2));
        assertEquals(null, testPlayer1.getCurrentCard());
    }
}