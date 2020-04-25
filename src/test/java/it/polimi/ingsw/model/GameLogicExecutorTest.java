package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ConstructAction;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.playerstate.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicExecutorTest {
    GameLogicExecutor gameLogicExecutor;
    Game game;
    MoveAction basicMove;
    Player currentPlayer;
    ConstructAction basicConstruct;
    ArrayList<Action> basicActionList1;
    ArrayList<Action> basicActionList2;
    ArrayList<Action> basicActionList3;

    //TODO: missing setPawnsPositions
    //TODO: missing loadCard
    //TODO: setInGameCards

    /**
     * This function creates a simple setup for a game of three players Ian, Luca, Riccardo.
     * The players play like a game with no gods, with one simple move and one simple create loaded in their actionList.
     * Player Ian is already in actionState
     */
    private void simpleGameSetupWith3PlayersOneInActionStateOthersInIdle() {
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
        currentPlayer=p;
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


    /**
     * The test checks that after the selection of the pawn for the current turn each variable is set correctly in the player state and the action to be executed.
     */
    @Test
    void setSelectedPawnTest(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        Position selectedPawn= game.getPlayer("ian").getPawnList().get(0).getPosition();
        Position notSelectedPawn= game.getPlayer("ian").getPawnList().get(1).getPosition();

        //once we call this function the first action will be loaded from actionList and pawn values will be updated
        gameLogicExecutor.setSelectedPawn(selectedPawn,notSelectedPawn);

        // we should check that the first action is actually loaded
        ActionState actionState = (ActionState) game.getPlayer("ian").getState();
        assertEquals(basicMove,actionState.getCurrentAction());

        //we should check that the pawns are set correctly both in the loaded action and in ActionState
        assertEquals(game.getPlayer("ian").getPawnList().get(0),actionState.getCurrentAction().getSelectedPawn());
        assertEquals(game.getPlayer("ian").getPawnList().get(1),actionState.getCurrentAction().getNotSelectedPawn());
        assertEquals(game.getPlayer("ian").getPawnList().get(0),actionState.getSelectedPawnCopy());
        assertEquals(game.getPlayer("ian").getPawnList().get(1),actionState.getUnselectedPawnCopy());


    }

    /**
     * This test checks that the execution of a moveAction in the simpleGameSetup works properly
     */
    @Test
    void setChosenPositionForMoveActionTest(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
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

    /**
     * This test checks that the execution of a constructAction in the simpleGameSetup works properly and the turn is passed
     * to the next player.
     */
    @Test
    void setChosenPositionForConstructActionTest(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
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
     * This test checks that the execution of an optional construct that has the enableMoveUp turned on actually enables the MoveUp
     */
//    @Test
//    void optionalConstructWithEnableMoveUpTest(){
//        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
//        MoveAction prometheusMove= new MoveAction(false, new ArrayList<>(),false, false, false, false,false, false, new ArrayList<>(), false, false);
//        ConstructAction optionalConstruct= new ConstructAction(true,new ArrayList<>(),false,new ArrayList<>(),false,true,false);
//        ArrayList<Action> prometheusActionList = new ArrayList<>();
//        prometheusActionList.add(optionalConstruct);
//        prometheusActionList.add(prometheusMove);
//        prometheusActionList.add(basicConstruct.duplicate());
//        for(Action a : prometheusActionList){
//            a.addObserver(gameLogicExecutor);
//        }
//
//        //game.getPlayer("ian").setCurrentCard(new Card("prometheus",10,prometheusActionList));
//        currentPlayer.setCurrentCard(new Card("prometheus",10,prometheusActionList));
//        currentPlayer.setState(new ActionState(prometheusActionList));
//
//        gameLogicExecutor.setSelectedPawn(game.getPlayer("ian").getPawnList().get(0).getPosition(), game.getPlayer("ian").getPawnList().get(1).getPosition());
//
//        ActionState actionState = (ActionState) game.getPlayer("ian").getState();
//        assertEquals(false, ((MoveAction)actionState.getActionList().get(1)).getMoveUpEnable());
//
//        //let's skip the position for the optional construct and see if the MoveUp on the move for the current player is updated correctly
//        gameLogicExecutor.setChosenPosition(null);
//        gameLogicExecutor.setChosenBlockType(null); //this is needed, a construct action is executed only if the chosenBlockType is set
//
//        //at this time the next action should be loaded, and it is the moveAction, this moveAction must have MoveUp set to true
//        assertEquals(true, ((MoveAction)actionState.getCurrentAction()).getMoveUpEnable());
//
//    }

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

    /**
     * The scope of this test function is to test that setPawnsPositions method correctly sets the player pawns
     */
    @Test
    void setPawnsPositions_OkPositions() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new ChoosePawnsPositionState());
        testPlayer1.addPawn(new Pawn("990000"));
        testPlayer1.addPawn(new Pawn("990000"));
        testGame.addPlayer(testPlayer1);
        ArrayList<Position> testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(0, 0));
        testPositionArray.add(new Position(1, 1));
        assertTrue(testGameLogicExecutor.setPawnsPositions(testPositionArray));
        assertEquals(new Position(0, 0), testPlayer1.getPawnList().get(0).getPosition());
        assertEquals(new Position(1, 1), testPlayer1.getPawnList().get(1).getPosition());
        assertEquals(new Position(0, 0), testGame.getBoard().getMatrixCopy()[0][0].getPawn().getPosition());
        assertEquals(new Position(1, 1), testGame.getBoard().getMatrixCopy()[1][1].getPawn().getPosition());
    }

    /**
     * The scope of this test function is to test that setPawnsPositions method correctly sets the player pawns
     */
    @Test
    void setPawnsPositions_NotOkPositions() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new ChoosePawnsPositionState());
        testPlayer1.addPawn(new Pawn("990000"));
        testPlayer1.addPawn(new Pawn("990000"));
        testGame.addPlayer(testPlayer1);
        ArrayList<Position> testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(0, 0));
        testPositionArray.add(new Position(5, 5));
        assertFalse(testGameLogicExecutor.setPawnsPositions(testPositionArray));
    }

    /**
     * The scope of this test function is to test that setInGameCards method correctly sets cards inside the game
     */
    @Test
    void setInGameCards_OkCards_2Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new SelectGameCardsState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        ArrayList<Card> testCardArray = new ArrayList<>();
        Card testCard1 = new Card("testCard1", 1, new ArrayList<>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<>());
        Card testCard3 = new Card("testCard3", 3, new ArrayList<>());
        testCardArray.add(testCard1);
        testCardArray.add(testCard2);
        testCardArray.add(testCard3);
        testGame.setLoadedCardsCopy(testCardArray);
        ArrayList<Integer> testCardIDArray = new ArrayList<>();
        testCardIDArray.add(1);
        testCardIDArray.add(2);
        assertTrue(testGameLogicExecutor.setInGameCards(testCardIDArray));
        assertTrue(testGame.getInGameCards().size() == 2);
        assertEquals(testCard1, testGame.getInGameCards().get(0));
        assertEquals(testCard2, testGame.getInGameCards().get(1));
        assertEquals(PlayerStateType.IdleState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.ChooseCardState, testPlayer2.getState().getType());
    }

    /**
     * The scope of this test function is to test that setInGameCards method correctly sets cards inside the game
     */
    @Test
    void setInGameCards_OkCards_3Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new SelectGameCardsState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        Player testPlayer3 = new Player("testPlayer3", new IdleState());
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        testGame.addPlayer(testPlayer3);
        ArrayList<Card> testCardArray = new ArrayList<>();
        Card testCard1 = new Card("testCard1", 1, new ArrayList<>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<>());
        Card testCard3 = new Card("testCard3", 3, new ArrayList<>());
        Card testCard4 = new Card("testCard4", 4, new ArrayList<>());
        testCardArray.add(testCard1);
        testCardArray.add(testCard2);
        testCardArray.add(testCard3);
        testCardArray.add(testCard4);
        testGame.setLoadedCardsCopy(testCardArray);
        ArrayList<Integer> testCardIDArray = new ArrayList<>();
        testCardIDArray.add(2);
        testCardIDArray.add(3);
        testCardIDArray.add(4);
        assertTrue(testGameLogicExecutor.setInGameCards(testCardIDArray));
        assertTrue(testGame.getInGameCards().size() == 3);
        assertEquals(testCard2, testGame.getInGameCards().get(0));
        assertEquals(testCard3, testGame.getInGameCards().get(1));
        assertEquals(testCard4, testGame.getInGameCards().get(2));
        assertEquals(PlayerStateType.IdleState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.ChooseCardState, testPlayer2.getState().getType());
        assertEquals(PlayerStateType.IdleState, testPlayer3.getState().getType());
    }

    /**
     * The scope of this test function is to test that setInGameCards method correctly sets cards inside the game
     */
    @Test
    void setInGameCards_NotOkCards() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new SelectGameCardsState());
        testGame.addPlayer(testPlayer1);
        ArrayList<Card> testCardArray = new ArrayList<>();
        Card testCard1 = new Card("testCard1", 1, new ArrayList<>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<>());
        Card testCard3 = new Card("testCard3", 3, new ArrayList<>());
        testCardArray.add(testCard1);
        testCardArray.add(testCard2);
        testCardArray.add(testCard3);
        testGame.setLoadedCardsCopy(testCardArray);
        ArrayList<Integer> testCardIDArray = new ArrayList<>();
        testCardIDArray.add(5);
        assertFalse(testGameLogicExecutor.setInGameCards(testCardIDArray));
        assertTrue(testGame.getInGameCards().size() == 0);
    }

    /**
     * The scope of this test function is to test that loadCards properly loads the json into Cards
     */
    @Test
    void loadCards() {
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        gameLogicExecutor.loadCards();
    }
}