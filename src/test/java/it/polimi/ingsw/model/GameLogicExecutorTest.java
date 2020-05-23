package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.modelview.ModelView;
import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
    ArrayList<MockView> mockViews;


    @BeforeEach
    void init_of_3_players(){
        //create the mock views
        mockViews=new ArrayList<>();
        mockViews.add(new MockView("p1"));
        mockViews.add(new MockView("p2"));
        mockViews.add(new MockView("p3"));

        //create the game accordingly adding the mock views
        game= new Game();
        gameLogicExecutor=new GameLogicExecutor(game);

    }





                                                            //MoveAction tests

    /**
     * This test checks that the execution of a moveAction in the simpleGameSetup works properly
     */
    @Test void setChosenPositionForMoveAction(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        Pawn selectedPawn=game.getPlayer("ian").getPawnList().get(0);
        Position selectedPawnPos= selectedPawn.getPosition();
        Pawn notSelected = game.getPlayer("ian").getPawnList().get(1);
        Position notSelectedPawnPos= notSelected.getPosition();
        gameLogicExecutor.setSelectedPawn(selectedPawnPos);

        //let's find out the positions available for the selected pawn
        ArrayList<Position> positions=game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());

        //after this function there should be a call (via visitor pattern) to the gameLogicExecutor.executeAction
        //so after this call the pawn should be placed correctly in the board
        gameLogicExecutor.setChosenPosition(positions.get(0));

        //this is a standardMove so let's just check that the pawn moved to the right position
        assertEquals(positions.get(0),selectedPawn.getPosition());

    }
    /**
     * This test checks that if an action with denyMoveUp enabled and with a selectedPawn that has moved up, disables moveUp for other players
     */
    @Test void moveActionDenyMoveUpForOtherPlayers() {
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();

        MoveAction athenaMove = new MoveAction(false, new ArrayList<>(), true, false, false, false, true, false, new ArrayList<>(), false, false);
        basicMove = new MoveAction(false, new ArrayList<>(), true, false, false, false, false, false, new ArrayList<>(), false, false);
        basicConstruct = new ConstructAction(false, new ArrayList<>(), false, new ArrayList<>(), false, false, false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(athenaMove);
        actions.add(basicConstruct.duplicate());
        for (Action a : actions) {
            a.addVisitor(gameLogicExecutor);
        }

        Card card=new Card("test",55,actions);
        currentPlayer.setCurrentCard(card);


        //before the action since our opponets have basicMove and basicConstruct, they should have moveUpEnable
        for (Player opponent : getNonCurrentPlayers()) {
            MoveAction moveAction = (MoveAction) opponent.getCurrentCard().getCurrentActionList().get(0);
            assertEquals(true, moveAction.getMoveUpEnable());
        }

        //to activate this effect we have to moveUp of one block our pawn, we create a level1 block near our pawn(0)
        game.getBoard().pawnConstruct(null, new Position(0, 0), BlockType.LEVEL1);

        //then we move the pawn to the selected position
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(0, 0));

        //we check that all other players have moveUp disable (in currentActionList) since our pawn moved up of one position
        assertEquals(1, currentPlayer.getPawnList().get(0).getDeltaHeight());
        for (Player opponent : getNonCurrentPlayers()) {
            MoveAction moveAction = (MoveAction) opponent.getCurrentCard().getCurrentActionList().get(0);
            assertEquals(false, moveAction.getMoveUpEnable());
        }
    }
    /**
     * This test checks that if a MoveAction with addMoveIfOn actually move to one of those cells, another Move is correctly added to its ActionList
     * This is done two times to check that the execution of the added move works fine
     */
    @Test void moveActionAddMoveIfOnMultipleTimes(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        ArrayList<Position> addIfOnThis = new ArrayList<>();
        addIfOnThis.add(new Position(0,0));
        addIfOnThis.add(new Position(0,1));
        MoveAction tritonMove = new MoveAction(false, new ArrayList<>(),true,false,false,false,true,false, addIfOnThis ,false,false);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(tritonMove);
        actions.add(basicConstruct.duplicate());
        for(Action a : actions){
            a.addVisitor(gameLogicExecutor);
        }

        Card card=new Card("test",55,actions);
        currentPlayer.setCurrentCard(card);


        //before the move, the action list should have a size of 2
        assertEquals(2,currentPlayer.getCurrentCard().getCurrentActionList().size());

        //then we move the pawn to the selected position
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(0,0));

        //after the move the actionList should have a size of 3 and the added action should be a moveAction with the same addMoveIfOn array (a copy)
        assertEquals(3,currentPlayer.getCurrentCard().getCurrentActionList().size());
        assertEquals(tritonMove,currentPlayer.getCurrentCard().getCurrentActionList().get(1));

        gameLogicExecutor.setChosenPosition(new Position(0,1));
        assertEquals(4,currentPlayer.getCurrentCard().getCurrentActionList().size());
        assertEquals(tritonMove,currentPlayer.getCurrentCard().getCurrentActionList().get(2));


    }
    /**
     * This test checks that if a MoveAction with swapEnable actually moves into an opponent cell the pawns are swapped
     */
    @Test void moveActionWithSwapEnable(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        ArrayList<Position> addIfOnThis = new ArrayList<>();
        MoveAction swapMove = new MoveAction(false, new ArrayList<>(),true,true,false,false,true,false, addIfOnThis ,false,false);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(swapMove);
        actions.add(basicConstruct.duplicate());
        for(Action a : actions){
            a.addVisitor(gameLogicExecutor);
        }

        Card card=new Card("test",55,actions);
        currentPlayer.setCurrentCard(card);


        //in 1,1 we have the selected pawn, in 1,2 we have an opponent pawn that we can swap
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(1,2));

        assertEquals(new Position(1,1),game.getPlayer("luca").getPawnList().get(0).getPosition());
        assertEquals(new Position(1,2),currentPlayer.getPawnList().get(0).getPosition());


    }
    /**
     * This test checks that if a MoveAction with pushEnable actually moves into an opponent cell the pawn is pushed away correctly
     */
    @Test void moveActionWithPushEnable(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        ArrayList<Position> addIfOnThis = new ArrayList<>();
        MoveAction swapMove = new MoveAction(false, new ArrayList<>(),true,false,false,true,true,false, addIfOnThis ,false,false);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(swapMove);
        actions.add(basicConstruct.duplicate());
        for(Action a : actions){
            a.addVisitor(gameLogicExecutor);
        }

        Card card=new Card("test",55,actions);
        currentPlayer.setCurrentCard(card);

        //in 1,1 we have the selected pawn, in 1,2 we have an opponent pawn that we can push in 1,3
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        game.board.updatePawnPosition(new Position(1,3), new Position(1,4));
        //simpleCompleteBoardPrint();
        //ArrayList<Position> availablePos=game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(new Position(1,2));

        assertEquals(new Position(1,3),game.getPlayer("luca").getPawnList().get(0).getPosition());
        assertEquals(new Position(1,2),currentPlayer.getPawnList().get(0).getPosition());

    }
    /**
     * This test checks that if a MoveAction gets a pawn to the top level, that player wins, and the correct states are set
     */
    @Test void moveActionWithAWinner(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();

        //the correct setup to test a win, the pawn is positioned in 1,1
        game.getBoard().pawnConstruct(null, new Position(1,1),BlockType.LEVEL1);
        game.getBoard().pawnConstruct(null, new Position(1,1),BlockType.LEVEL2);
        game.getBoard().pawnConstruct(null, new Position(0,0),BlockType.LEVEL1);
        game.getBoard().pawnConstruct(null, new Position(0,0),BlockType.LEVEL2);
        game.getBoard().pawnConstruct(null, new Position(0,0),BlockType.LEVEL3);

        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(0,0));

        assertEquals(0,getNonLoserOrWinnerPlayers().size());
        assertEquals(true,currentPlayer.getWinner());
        assertEquals(true,game.getPlayer("luca").getLoser());
        assertEquals(true,game.getPlayer("riccardo").getLoser());
    }






                                                            //ConstructAction tests

    /**
     * This test checks that the execution of a constructAction in the simpleGameSetup works properly and the turn is passed
     * to the next player.
     */
    @Test void setChosenPositionForConstructActionTest(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();

        //set selectedPawn for current user
        Pawn selectedPawn=game.getPlayer("ian").getPawnList().get(0);
        Position selectedPawnPos= selectedPawn.getPosition();
        Pawn notSelected = game.getPlayer("ian").getPawnList().get(1);
        Position notSelectedPawnPos= notSelected.getPosition();

        gameLogicExecutor.setSelectedPawn(selectedPawnPos);


        //this will execute the first action (Move)
        ArrayList<Position> positions=game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(positions.get(0));

        //this will execute the second action (Construct)
        positions=game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(positions.get(0));
        ArrayList<BlockType> blockTypes = game.getCurrentAction().availableBlockTypes(positions.get(0),game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenBlockType(blockTypes.get(0));

        assertEquals(blockTypes.get(0),game.getBoard().getMatrixCopy()[positions.get(0).getX()][positions.get(0).getY()].peekBlock());

    }
    /**
     * This test checks that if an OptionalConstruct with disableMoveUp is actually run, the value of moveUP is turned off in the next actions
     */
    @Test void optionalConstructWithDisableMoveUpTest(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        MoveAction prometheusMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        ConstructAction optionalConstruct= new ConstructAction(true,new ArrayList<>(),false,new ArrayList<>(),false,true,false);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> prometheusActionList = new ArrayList<>();
        prometheusActionList.add(optionalConstruct);
        prometheusActionList.add(prometheusMove);
        prometheusActionList.add(basicConstruct.duplicate());
        for(Action a : prometheusActionList){
            a.addVisitor(gameLogicExecutor);
        }

        game.getCurrentPlayer().setCurrentCard(new Card("prometheus",10,prometheusActionList));

        gameLogicExecutor.setSelectedPawn(game.getPlayer("ian").getPawnList().get(0).getPosition());

        assertEquals(true, ((MoveAction)game.getCurrentPlayer().getCurrentCard().getCurrentActionList().get(1)).getMoveUpEnable());

        ArrayList<Position> chosenPos = game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());

        gameLogicExecutor.setChosenPosition(chosenPos.get(0));
        ArrayList<BlockType> blockTypes = ((ConstructAction)game.getCurrentAction()).availableBlockTypes(chosenPos.get(0),game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenBlockType(blockTypes.get(0)); //this is needed, a construct action is executed only if the chosenBlockType is set

        //at this time the next action should be loaded, and it is the moveAction, this moveAction must have MoveUp set to true
        assertEquals(false, ((MoveAction)game.getCurrentAction()).getMoveUpEnable());
    }
    /**
     * This test checks that if an optional Action (in this case a ConstructAction) is actually skipped, no changes are made to the pawn and the next
     * action is correctly loaded.
     */
    @Test void genericOptionalActionIsSkippedTest(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        ConstructAction optionalConstruct = new ConstructAction(true,new ArrayList<>(),false, new ArrayList<>(),false,false, false);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(optionalConstruct);
        actions.add(basicMove.duplicate());
        for(Action a : actions){
            a.addVisitor(gameLogicExecutor);
        }

        currentPlayer.setCurrentCard(new Card("optionalConstruct",33, actions));


        Pawn prevPawn = currentPlayer.getPawnList().get(0);
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(null);
        gameLogicExecutor.setChosenBlockType(null);

        assertEquals(prevPawn, currentPlayer.getPawnList().get(0));
        assertEquals(basicMove,game.getCurrentAction());
    }





                                                            //generalAction tests

    /**
     * This function tests that at the end of a turn if a general action is present that has destroyPawnAndBuildEnable activated
     * and an opponent pawn is near one of the pawns of the current user the pawn is removed from the game and a block is constructed
     */
    @Test void generalActionWithDestroyPawnAndBuildEnable(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        ArrayList<Position> addIfOnThis = new ArrayList<>();
        GeneralAction medusaAction = new GeneralAction(false,new ArrayList<>(),false,true);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(basicMove.duplicate());
        actions.add(basicConstruct.duplicate());
        actions.add(medusaAction);
        for(Action a : actions){
            a.addVisitor(gameLogicExecutor);
        }

        Card card=new Card("test",55,actions);
        currentPlayer.setCurrentCard(card);

        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        game.getBoard().pawnConstruct(null, new Position(0,1),BlockType.LEVEL1);

        //MoveAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,1));

        //ConstructAction executed
        gameLogicExecutor.setChosenPosition(new Position(1,1));
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);


        assertEquals(1,game.getPlayer("luca").getPawnList().size());
        assertEquals(BlockType.LEVEL1,game.getBoard().getMatrixCopy()[1][2].peekBlock());
        assertNull(game.getBoard().getMatrixCopy()[1][2].getPawn());

    }
    /**
     * This test checks that if a player lose all of his pawn, the player is set in loser state
     */
    @Test void generalActionThatRemovesAllPawnOfAnOpponent(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        ArrayList<Position> addIfOnThis = new ArrayList<>();
        GeneralAction medusaAction = new GeneralAction(false,new ArrayList<>(),false,true);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(basicMove.duplicate());
        actions.add(basicConstruct.duplicate());
        actions.add(medusaAction);
        for(Action a : actions){
            a.addVisitor(gameLogicExecutor);
        }

        Card card=new Card("test",55,actions);
        currentPlayer.setCurrentCard(card);

        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        game.getBoard().pawnConstruct(null, new Position(0,1),BlockType.LEVEL1);
        game.getBoard().pawnConstruct(null, new Position(4,4),BlockType.LEVEL1);


        //MoveAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,1));

        //ConstructAction executed
        gameLogicExecutor.setChosenPosition(new Position(1,1));
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);


        assertEquals(0,game.getPlayer("luca").getPawnList().size());
        assertEquals(BlockType.LEVEL1,game.getBoard().getMatrixCopy()[1][2].peekBlock());
        assertNull(game.getBoard().getMatrixCopy()[1][2].getPawn());
        assertEquals(true,game.getPlayer("luca").getLoser());

    }
    /**
     * This test checks  that if a general Action is executed with enableNoWinOnPerimeter the actual variable is set accordingly
     */
    @Test void generalActionEnableNoWinIfOnPerimeterForOtherPlayers(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        ArrayList<Position> addIfOnThis = new ArrayList<>();
        GeneralAction heraAction = new GeneralAction(false,new ArrayList<>(),true,false);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(heraAction);
        actions.add(basicMove.duplicate());
        actions.add(basicConstruct.duplicate());
        for(Action a : actions){
            a.addVisitor(gameLogicExecutor);
        }

        Card card=new Card("test",55,actions);
        currentPlayer.setCurrentCard(card);

        //we execute the general action
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(null);

        for(Player opponent : getNonCurrentPlayers()){
            for(Action a : opponent.getCurrentCard().getCurrentActionList()){
                MoveAction moveAction = (MoveAction) opponent.getCurrentCard().getCurrentActionList().get(0);
                assertEquals(true, moveAction.getNoWinIfOnPerimeter());
            }
        }


    }






                                                                //Setup tests

    /**
     * The test checks that after the selection of the pawn for the current turn each variable is set correctly in the player state and the action to be executed.
     */
    @Test void setSelectedPawnTest(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        Position selectedPawn= game.getPlayer("ian").getPawnList().get(0).getPosition();
        Position notSelectedPawn= game.getPlayer("ian").getPawnList().get(1).getPosition();

        //once we call this function the first action will be loaded from actionList and pawn values will be updated
        gameLogicExecutor.setSelectedPawn(selectedPawn);

        // we should check that the first action is actually loaded
        assertEquals(basicMove,game.getCurrentAction());

        //we should check that the pawns are set correctly both in the loaded action and in ActionState
        assertEquals(game.getPlayer("ian").getPawnList().get(0),game.getCurrentAction().getSelectedPawn());
        assertEquals(game.getPlayer("ian").getPawnList().get(1),game.getCurrentAction().getNotSelectedPawn());
        assertEquals(game.getPlayer("ian").getPawnList().get(0),game.getSelectedPawnCopy());
        assertEquals(game.getPlayer("ian").getPawnList().get(1),game.getUnselectedPawnCopy());


    }
    /**
     * The test checks that after a player has finished all of its actions, it is placed in idle and the next player is loaded
     */
    @Test void loadNextPlayer(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition());

        //moveAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,0));

        //constructAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,1));
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);

        //so here we should have that luca is in actionState and ian is in idle state, riccardo too.
        assertEquals("luca",game.getCurrentPlayer().getName());
    }
    /**
     * The scope of this test function is to test that startGame method correctly starts the game
     */
    @Test void startGame_3Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        testGameLogicExecutor.setNumberOfPlayers(3);
        testGameLogicExecutor.addPlayerToLobby("testPlayer1");
        testGameLogicExecutor.addPlayerToLobby("testPlayer2");
        testGameLogicExecutor.addPlayerToLobby("testPlayer3");

        ArrayList<String> testNameArrays = new ArrayList<>();
        testNameArrays.add("testPlayer1");
        testNameArrays.add("testPlayer2");
        testNameArrays.add("testPlayer3");
        assertNotNull(testGame.getCurrentPlayer());
        assertTrue(testNameArrays.contains(testGame.getCurrentPlayer().getName()));
    }
    /**
     * The scope of this test function is to test that startGame method correctly starts the game
     */
    @Test void startGame_2Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        testGameLogicExecutor.setNumberOfPlayers(2);

        testGameLogicExecutor.addPlayerToLobby("testPlayer1");
        testGameLogicExecutor.addPlayerToLobby("testPlayer2");

        assertNotNull(testGame.getCurrentPlayer());
    }
    /**
     * The scope of this test function is to test that setChosenCard method correctly sets the player card
     */
    @Test void setChosenCard_FirstPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);

        Card testCard1 = new Card("testCard1", 1, new ArrayList<>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<>());
        ArrayList<Card> testCardList = new ArrayList<>();
        testCardList.add(testCard1);
        testCardList.add(testCard2);

        testGame.setCurrentPlayer(testPlayer1);
        testGame.setInGameCardsCopy(testCardList);
        testGameLogicExecutor.setChosenCard(1);

        assertEquals(testCard1, testPlayer1.getCurrentCard());
        assertEquals(testPlayer2.getName(), testGame.getCurrentPlayer().getName());
    }
    /**
     * The scope of this test function is to test that setChosenCard method correctly sets the player card
     */
    @Test void setChosenCard_LastPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);

        Card testCard1 = new Card("testCard1", 1, new ArrayList<>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<>());
        ArrayList<Card> testCardList = new ArrayList<>();
        testCardList.add(testCard1);
        testCardList.add(testCard2);

        testGame.setCurrentPlayer(testPlayer1);
        testGame.setInGameCardsCopy(testCardList);

        testGameLogicExecutor.setChosenCard(1);
        testGameLogicExecutor.setChosenCard(2);

        assertEquals(testCard2, testPlayer2.getCurrentCard());
        assertEquals(testPlayer2.getName(), testGame.getCurrentPlayer().getName());
    }
    /**
     * The scope of this test function is to test that setChosenCard method correctly sets the player card
     */
    @Test void setChosenCard_WrongCard() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1");
        testGame.addPlayer(testPlayer1);

        Card testCard1 = new Card("testCard1", 1, new ArrayList<Action>());
        ArrayList<Card> testCardList = new ArrayList<>();
        testCardList.add(testCard1);

        testGame.setInGameCardsCopy(testCardList);
        Card testCard2 = new Card("testCard2", 2, new ArrayList<>());
        testGame.setCurrentPlayer(testPlayer1);
        assertFalse(testGameLogicExecutor.setChosenCard(2));
        assertNull(testPlayer1.getCurrentCard());
    }
    /**
     * The scope of this test function is to test that setPawnsPositions method correctly sets the player pawns
     */
    @Test void setPawnsPositions_OkPositions() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);

        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");
        testPlayer1.addPawn(new Pawn("990000",0));
        testPlayer1.addPawn(new Pawn("990000",1));
        testPlayer2.addPawn(new Pawn("009900",2));
        testPlayer2.addPawn(new Pawn("009900",3));
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);

        testGame.setCurrentPlayer(testPlayer1);

        testGameLogicExecutor.setPawnsPositions(0,new Position(0,0),1,new Position(1,1));

        assertEquals(new Position(0, 0), testPlayer1.getPawnList().get(0).getPosition());
        assertEquals(new Position(1, 1), testPlayer1.getPawnList().get(1).getPosition());
        assertEquals(new Position(0, 0), testGame.getBoard().getMatrixCopy()[0][0].getPawn().getPosition());
        assertEquals(new Position(1, 1), testGame.getBoard().getMatrixCopy()[1][1].getPawn().getPosition());
        assertEquals(testPlayer2.getName(),testGame.getCurrentPlayer().getName());
    }
    /**
     * The scope of this test function is to test that setPawnsPositions method correctly sets the player pawns
     */
    @Test void setPawnsPositions_NotOkPositions() {
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        boolean ret=gameLogicExecutor.setPawnsPositions(0,new Position(0,0),1,new Position(5,5));
        assertFalse(ret);
    }
    /**
     * The scope of this test function is to test that setInGameCards method correctly sets cards inside the game
     */
    @Test void setInGameCards_OkCards_2Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);

        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");
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

        testGame.setCurrentPlayer(testPlayer1);

        testGameLogicExecutor.setInGameCards(testCardIDArray);

        assertEquals(2, testGame.getInGameCards().size());
        assertEquals(testCard1, testGame.getInGameCards().get(0));
        assertEquals(testCard2, testGame.getInGameCards().get(1));
        assertEquals(testPlayer2.getName(), testGame.getCurrentPlayer().getName());
    }
    /**
     * The scope of this test function is to test that setInGameCards method correctly sets cards inside the game
     */
    @Test void setInGameCards_OkCards_3Players() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);

        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");
        Player testPlayer3 = new Player("testPlayer3");
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

        testGame.setCurrentPlayer(testPlayer1);
        testGameLogicExecutor.setInGameCards(testCardIDArray);

        assertEquals(3, testGame.getInGameCards().size());
        assertEquals(testCard2, testGame.getInGameCards().get(0));
        assertEquals(testCard3, testGame.getInGameCards().get(1));
        assertEquals(testCard4, testGame.getInGameCards().get(2));
        assertEquals(testPlayer2, testGame.getCurrentPlayer());
    }
    /**
     * The scope of this test function is to test that setInGameCards method correctly sets cards inside the game
     */
    @Test void setInGameCards_NotOkCards() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);

        Player testPlayer1 = new Player("testPlayer1");
        testGame.addPlayer(testPlayer1);
        testGame.setCurrentPlayer(testPlayer1);

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
        assertEquals(0, testGame.getInGameCards().size());
    }
    /**
     * The scope of this test function is to test that setStartPlayer method correctly sets the first player
     */
    @Test void setStartPlayer_OtherPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");
        Player testPlayer3 = new Player("testPlayer3");
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        testGame.addPlayer(testPlayer3);

        testGame.setCurrentPlayer(testPlayer1);
        testGameLogicExecutor.setStartPlayer("testPlayer2");

        assertEquals(testPlayer2, testGame.getCurrentPlayer());
    }
    /**
     * The scope of this test function is to test that setStartPlayer method correctly sets the first player
     */
    @Test void setStartPlayer_SamePlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");
        Player testPlayer3 = new Player("testPlayer3");
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        testGame.addPlayer(testPlayer3);

        testGame.setCurrentPlayer(testPlayer1);
        testGameLogicExecutor.setStartPlayer("testPlayer1");
        assertEquals(testPlayer1,testGame.getCurrentPlayer());
    }
    /**
     * The scope of this test function is to test that setStartPlayer method correctly sets the first player
     */
    @Test void setStartPlayer_NotOkPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1");
        Player testPlayer2 = new Player("testPlayer2");

        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        testGame.setCurrentPlayer(testPlayer1);

        assertFalse(testGameLogicExecutor.setStartPlayer("testPlayer3"));

    }
    /**
     * The scope of this test function is to test that the set-up phase works as expected
     */
    @Test void setUpGame_3Players() {
        Game testGame = new Game();

        //Load cards
        ArrayList<Card> testCardArray = new ArrayList<>();
        Card testCard1 = new Card("testCard1", 1, new ArrayList<>());
        Card testCard2 = new Card("testCard2", 2, new ArrayList<>());
        Card testCard3 = new Card("testCard3", 3, new ArrayList<>());
        testCardArray.add(testCard1);
        testCardArray.add(testCard2);
        testCardArray.add(testCard3);

        testGame.setLoadedCardsCopy(testCardArray);
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);

        testGameLogicExecutor.setNumberOfPlayers(3);
        testGameLogicExecutor.addPlayerToLobby("testPlayer1");
        testGameLogicExecutor.addPlayerToLobby("testPlayer2");
        testGameLogicExecutor.addPlayerToLobby("testPlayer3");


        Player godLikePlayer = testGame.getPlayers().get(0);
        Player testPlayer = testGame.getPlayers().get(1);
        Player testPlayerBis = testGame.getPlayers().get(2);

        ArrayList<Integer> testCardIDArray = new ArrayList<>();
        testCardIDArray.add(1);
        testCardIDArray.add(2);
        testCardIDArray.add(3);
        assertTrue(testGameLogicExecutor.setInGameCards(testCardIDArray));
        assertTrue(testGameLogicExecutor.setChosenCard(1));
        assertTrue(testGameLogicExecutor.setChosenCard(2));
        assertTrue(testGameLogicExecutor.setChosenCard(3));
        assertTrue(testGameLogicExecutor.setStartPlayer("testPlayer1"));

        ArrayList<Position> testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(0, 0));
        testPositionArray.add(new Position(0, 1));
        testGameLogicExecutor.setPawnsPositions(testPositionArray);

        testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(1, 0));
        testPositionArray.add(new Position(1, 1));
        testGameLogicExecutor.setPawnsPositions(testPositionArray);

        testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(2, 0));
        testPositionArray.add(new Position(2, 1));
        testGameLogicExecutor.setPawnsPositions(testPositionArray);


        assertEquals(testCard1, testPlayer.getCurrentCard());
        assertEquals(testCard2, testPlayerBis.getCurrentCard());
        assertEquals(testCard3, godLikePlayer.getCurrentCard());

        assertEquals(new Position(0, 0), testGame.getPlayer("testPlayer1").getPawnList().get(0).getPosition());
    }
    /**
     * The scope of this test function is to test that loadCards properly loads the json into Cards
     */
    @Test void loadCards() {
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();

        ArrayList<Position> expectedList = new ArrayList<Position>() {{
            add(new Position(0,0));
            add(new Position(0,1));
            add(new Position(0,2));
            add(new Position(0,3));
            add(new Position(0,4));
            add(new Position(1,0));
            add(new Position(1,4));
            add(new Position(2,0));
            add(new Position(2,4));
            add(new Position(3,0));
            add(new Position(3,4));
            add(new Position(4,0));
            add(new Position(4,1));
            add(new Position(4,2));
            add(new Position(4,3));
            add(new Position(4,4));
        }};
        assertEquals("Apollo", game.getLoadedCards().get(0).getName());
        assertTrue(game.getLoadedCards().get(1).getDefaultActionListCopy().get(1) instanceof MoveAction);
        //checks whether the list of notAvailableCells loaded from the json is properly set to the action attribute
        assertTrue(game.getLoadedCards().get(10).getDefaultActionListCopy().get(2).getNotAvailableCell().containsAll(expectedList) && expectedList.containsAll(game.getLoadedCards().get(10).getDefaultActionListCopy().get(2).getNotAvailableCell()));
    }



                                                            //Complete game tests

    /**
     * full simple gamePlay with god 1,2,3 no effects activated
     */
    @Test void simpleCompleteGameplay(){
        game = new Game();
        gameLogicExecutor = new GameLogicExecutor(game);

        ArrayList<Position> positions1 = new ArrayList<>();
        positions1.add(new Position(0,0));
        positions1.add(new Position(4,4));

        ArrayList<Position> positions2 = new ArrayList<>();
        positions2.add(new Position(0,1));
        positions2.add(new Position(4,3));

        ArrayList<Position> positions3 = new ArrayList<>();
        positions3.add(new Position(0,2));
        positions3.add(new Position(4,2));


        game.addPlayer(new Player("Player1"));
        game.addPlayer(new Player("Player2"));
        game.addPlayer(new Player("Player3"));

        //set the pawn as we want
        game.getPlayers().get(0).addPawn(new Pawn("Blue",0));
        game.getPlayers().get(0).addPawn(new Pawn("Blue",1));
        game.getPlayers().get(1).addPawn(new Pawn("White",2));
        game.getPlayers().get(1).addPawn(new Pawn("White",3));
        game.getPlayers().get(2).addPawn(new Pawn("Brown",4));
        game.getPlayers().get(2).addPawn(new Pawn("Brown",5));

        gameLogicExecutor.loadCards();
        game.getPlayers().get(0).setCurrentCard(game.getLoadedCardCopy(1)); //APOLLO
        game.getPlayers().get(1).setCurrentCard(game.getLoadedCardCopy(2)); //ARTEMIS
        game.getPlayers().get(2).setCurrentCard(game.getLoadedCardCopy(3)); //ATHENA

        game.setCurrentPlayer(game.getPlayers().get(0));

        gameLogicExecutor.setPawnsPositions(positions1);
        gameLogicExecutor.setPawnsPositions(positions2);
        gameLogicExecutor.setPawnsPositions(positions3);

        gameLogicExecutor.setStartPlayer("Player1");

        //From now on the turns will be Player1(Apollo) -> Player2(Artemis) -> Player3(Athena)

        //Player1 Turn1 Apollo
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player1").getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(1,0)); //move
        gameLogicExecutor.setChosenPosition(new Position(1,1)); //construct
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);
        //Player2 Turn1 Arthemis
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player2").getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(1,1)); //move
        gameLogicExecutor.setChosenPosition(null); //optionalMove
        gameLogicExecutor.setChosenPosition(new Position(1,2));//construct
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);
        //Player3 Turn1 Athena
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player3").getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(1,3)); //move
        gameLogicExecutor.setChosenPosition(new Position(1,2)); //construct
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL2);


        //Player1 Turn2 Apollo
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player1").getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(2,1)); //move
        gameLogicExecutor.setChosenPosition(new Position(2,2)); //construct
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);
        //Player2 Turn2 Arthemis
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player2").getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(1,2)); //move
        gameLogicExecutor.setChosenPosition(null); //optionalMove
        gameLogicExecutor.setChosenPosition(new Position(2,2));//construct
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL2);
        //Player3 Turn2 Athena
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player3").getPawnList().get(0).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(2,3)); //move
        gameLogicExecutor.setChosenPosition(new Position(2,2)); //construct
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL3);

        //Player1 Turn3 Apollo
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player1").getPawnList().get(0).getPosition());
        ArrayList<Position> a =game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(new Position(3,0)); //move
        a =game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(new Position(4,0)); //construct
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);
        //Player2 Turn3 Arthemis
        gameLogicExecutor.setSelectedPawn(game.getPlayer("Player2").getPawnList().get(0).getPosition());
        a =game.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenPosition(new Position(2,2)); //move


        assertEquals(true,game.getPlayer("Player2").getWinner());
        assertEquals(true,game.getPlayer("Player1").getLoser());
        assertEquals(true,game.getPlayer("Player3").getLoser());


    }




                                                    // TESTS USING MOCKVIEWS TO EMULATE CLIENTS

    /**
     * This function tests adding 4 players and then setting the number of player to 3, the last player to join should receive a "game started and you are not selected"
     */
    @Test void addMorePlayerThanNeeded(){
        //the first player is added to the lobby, so he should receive the request for the number of players in the game
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        mockViews.add(new MockView("p4"));
        gameLogicExecutor.addListener(mockViews.get(3));
        gameLogicExecutor.addPlayerToLobby("p4");

        gameLogicExecutor.setNumberOfPlayers(3);

        assertTrue(mockViews.get(3).getLastReceivedMessage() instanceof GameStartedAndYouAreNotSelectedMessage);
    }
    /**
     * This function tests adding another player after the game starteed
     */
    @Test void addPlayerAfterGameStarted(){
        //the first player is added to the lobby, so he should receive the request for the number of players in the game
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");

        //other players are added to the lobby, check that their message is set to null
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //we set the number of players to 3 so the game will start immediately
        gameLogicExecutor.setNumberOfPlayers(3);

        //at this point the startGame function is called and one player will receive the message to set the cards
        String currentPlayerName=gameLogicExecutor.getCurrentPlayerName();
        mockViews.add(new MockView("p4"));
        gameLogicExecutor.addListener(mockViews.get(3));
        gameLogicExecutor.addPlayerToLobby("p4");

        assertTrue(mockViews.get(3).getLastReceivedMessage() instanceof GameStartedAndYouAreNotSelectedMessage);
    }
    /**
     * This function tests that once a game started one player gets the inGameCardsRequest the others the game started message
     */
    @Test void receiveGameStartedAndInGameCardsRequest(){
        //the first player is added to the lobby, so he should receive the request for the number of players in the game
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        assertTrue(mockViews.get(0).getLastReceivedMessage() instanceof NumberOfPlayersRequestMessage);
        assertEquals(mockViews.get(0).getName(),mockViews.get(0).getLastReceivedMessage().getRecipients().get(0));

        //other players are added to the lobby, check that their message is set to null
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");
        assertEquals(0,mockViews.get(1).getReceivedMessages().size());
        assertEquals(0,mockViews.get(2).getReceivedMessages().size());

        //we set the number of players to 3 so the game will start immediately
        gameLogicExecutor.setNumberOfPlayers(3);

        //at this point the startGame function is called and one player will receive the message to set the cards
        String currentPlayerName=gameLogicExecutor.getCurrentPlayerName();
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(currentPlayerName)){
                assertTrue(mockView.getLastReceivedMessage() instanceof InGameCardsRequestMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof GameStartMessage);
            }
        }
    }
    /**
     * This function tests that all the players receives the chosenCardRequest
     */
    @Test void everyOneReceiveChosenCardRequest(){
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");
        gameLogicExecutor.setNumberOfPlayers(3);
        ArrayList<Integer> inGameCards=new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(2);
        inGameCards.add(3);
        gameLogicExecutor.setInGameCards(inGameCards);

        MockView currentMockView=getCurrentPlayerMockView(); //the mockView of the user who set the ChosenCard
        assert currentMockView != null;
        gameLogicExecutor.setChosenCard(1);
        //everyone should have received a cardUpdate
        for(MockView mockView : mockViews){
            if(!mockView.getName().equals(gameLogicExecutor.getCurrentPlayerName())) {
                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) received).getName());
                assertEquals(1, ((ChosenCardUpdateMessage) received).getChosenCard().getId());
            }
            else{
                //if i am the current player mock i have received both the update and the request for my chosen card
                RequestAndUpdateMessage beforeTheLastMessage = mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2);
                assertTrue(beforeTheLastMessage instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) beforeTheLastMessage).getName());
                assertEquals(1, ((ChosenCardUpdateMessage) beforeTheLastMessage).getChosenCard().getId());

                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardRequestMessage);
                assertEquals(mockView.getName(), ((ChosenCardRequestMessage) received).getRecipients().get(0));
                assertEquals(2, ((ChosenCardRequestMessage) received).getAvailableCards().size());
            }
        }


        currentMockView=getCurrentPlayerMockView();
        assert currentMockView != null;
        gameLogicExecutor.setChosenCard(2);
        //everyone should have received a cardUpdate
        for(MockView mockView : mockViews){
            if(!mockView.getName().equals(gameLogicExecutor.getCurrentPlayerName())) {
                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) received).getName());
                assertEquals(2, ((ChosenCardUpdateMessage) received).getChosenCard().getId());
            }
            else{
                //if i am the current player mock i have received both the update and the request for my chosen card
                RequestAndUpdateMessage beforeTheLastMessage = mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2);
                assertTrue(beforeTheLastMessage instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) beforeTheLastMessage).getName());
                assertEquals(2, ((ChosenCardUpdateMessage) beforeTheLastMessage).getChosenCard().getId());

                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardRequestMessage);
                assertEquals(mockView.getName(), ((ChosenCardRequestMessage) received).getRecipients().get(0));
                assertEquals(1, ((ChosenCardRequestMessage) received).getAvailableCards().size());
            }
        }


        currentMockView=getCurrentPlayerMockView();
        assert currentMockView != null;
        gameLogicExecutor.setChosenCard(3);
        //everyone should have received a cardUpdate
        for(MockView mockView : mockViews){
            if(!mockView.getName().equals(gameLogicExecutor.getCurrentPlayerName())) {
                //everyone else a part of the last player to choose the card received only the ChosenCardUpdate
                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) received).getName());
                assertEquals(3, ((ChosenCardUpdateMessage) received).getChosenCard().getId());
            }
            else{
                //if this is the last player to have chosen the card, the message before the lastMessage should be the ChosenCardUpdate
                RequestAndUpdateMessage beforeTheLastMessage = mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2);
                assertTrue(beforeTheLastMessage instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) beforeTheLastMessage).getName());
                assertEquals(3, ((ChosenCardUpdateMessage) beforeTheLastMessage).getChosenCard().getId());

                //the last message should be FirstPlayerRequestMessage
                RequestAndUpdateMessage lastMessage = mockView.getLastReceivedMessage();
                assertTrue(lastMessage instanceof FirstPlayerRequestMessage);
                assertEquals(currentMockView.getName(), lastMessage.getRecipients().get(0));
            }
        }
    }
    /**
     * This function test that the initialPawnPositionRequest is received
     */
    @Test void ReceiveInitialPawnsPositionsRequest(){
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");
        gameLogicExecutor.setNumberOfPlayers(3);
        ArrayList<Integer> inGameCards=new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(2);
        inGameCards.add(3);
        gameLogicExecutor.setInGameCards(inGameCards);
        gameLogicExecutor.setChosenCard(1);
        gameLogicExecutor.setChosenCard(2);
        gameLogicExecutor.setChosenCard(3);


        assertTrue(getCurrentPlayerMockView().getLastReceivedMessage() instanceof FirstPlayerRequestMessage);
        gameLogicExecutor.setStartPlayer("p1");

        //at this point "p1" should have received a request for the initial pawn positions containing the allowed positions
        assertTrue(getCurrentPlayerMockView().getLastReceivedMessage() instanceof InitialPawnPositionRequestMessage);


        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        //at this point all the mock views should have received an double pawn position update, and one player should have received an Initial pawn position
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                //the current player should have both the update and the initial pawn position request
                assertTrue(mockView.getLastReceivedMessage() instanceof InitialPawnPositionRequestMessage);
                assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof DoublePawnPositionUpdateMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
            }
        }

        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        //at this point all the mock views should have received an double pawn position update, and one player should have received an Initial pawn position
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                //the current player should have both the update and the initial pawn position request
                assertTrue(mockView.getLastReceivedMessage() instanceof InitialPawnPositionRequestMessage);
                assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof DoublePawnPositionUpdateMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
            }
        }

        //this is the last one who should set the initial pawn positions
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        //at this point all the mock views should have received an double pawn position update, and one player should have received an Initial pawn position
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                //the current player should have both the update and the select pawn message
                assertTrue(mockView.getLastReceivedMessage() instanceof SelectPawnRequestMessage);
                assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof DoublePawnPositionUpdateMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
            }
        }

        //UNCOMMENT TO SEE THE BOARD SETUP
        //simpleBoardPrint();
    }
    /**
     * This function tests a complete turn of the first player
     */
    @Test void CompleteTurnOfFirstPlayer(){
        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(3);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards=new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(2);
        inGameCards.add(3);
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(1);
        gameLogicExecutor.setChosenCard(2);
        gameLogicExecutor.setChosenCard(3);

        //SET THE FIRST PLAYER
        gameLogicExecutor.setStartPlayer("p1");

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);

        int maxTurns=10;
        int numberOfTurns=0;

        MockView currentP=getCurrentPlayerMockView();
        SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        randomNum1= ThreadLocalRandom.current().nextInt(0,selectPawnRequestMessage.getAvailablePositions().size());
        gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));
        //At this point we should have a ChosenPositionRequestMessage and SelectPawnUpdateMessage in the current mock and in the other a doublePawnPositionUpdate
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                //the current player should have both the update and the select pawn message
                assertTrue(mockView.getLastReceivedMessage() instanceof ChosenPositionForMoveRequestMessage);
                assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof SelectedPawnUpdateMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
            }
        }
        while(!(currentP.getLastReceivedMessage() instanceof TurnEndedMessage)){

            if(currentP.getLastReceivedMessage() instanceof ChosenPositionForMoveRequestMessage){
                ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage = (ChosenPositionForMoveRequestMessage) currentP.getLastReceivedMessage();
                if(m.getAvailablePositions().size()<1){
                    System.out.println("BOT INCASTRATO player:"+currentP.getName()+"selectedPawn:"+game.getCurrentAction().getSelectedPawn().getId());
                    simpleCompleteBoardPrint();
                    return;
                }
                randomNum1= ThreadLocalRandom.current().nextInt(0, chosenPositionForMoveRequestMessage.getAvailablePositions().size());
                gameLogicExecutor.setChosenPosition(chosenPositionForMoveRequestMessage.getAvailablePositions().get(randomNum1));
            }
            else if(currentP.getLastReceivedMessage() instanceof ChosenPositionForConstructRequestMessage) {
                ChosenPositionForConstructRequestMessage chosenPositionForConstructRequestMessage = (ChosenPositionForConstructRequestMessage) currentP.getLastReceivedMessage();
                if (m.getAvailablePositions().size() < 1) {
                    System.out.println("BOT INCASTRATO player:" + currentP.getName() + "selectedPawn:" + game.getCurrentAction().getSelectedPawn().getId());
                    simpleCompleteBoardPrint();
                    return;
                }
                randomNum1 = ThreadLocalRandom.current().nextInt(0, chosenPositionForConstructRequestMessage.getAvailablePositions().size());
                gameLogicExecutor.setChosenPosition(chosenPositionForConstructRequestMessage.getAvailablePositions().get(randomNum1));

            }
            else if(currentP.getLastReceivedMessage() instanceof ChosenBlockTypeRequestMessage){
                ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage = (ChosenBlockTypeRequestMessage) currentP.getLastReceivedMessage();
                randomNum1= ThreadLocalRandom.current().nextInt(0,chosenBlockTypeRequestMessage.getAvailableBlockTypes().size());
                gameLogicExecutor.setChosenBlockType(chosenBlockTypeRequestMessage.getAvailableBlockTypes().get(randomNum1));
            }

        }


    }
    /**
     * This function tests a complete game with three bots
     */
    @Test void CompleteGameWithBots(){
        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(3);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards=new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(4);
        inGameCards.add(6);
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(1);
        gameLogicExecutor.setChosenCard(4);
        gameLogicExecutor.setChosenCard(6);

        //SET THE FIRST PLAYER
        gameLogicExecutor.setStartPlayer("p1");

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);

        int maxTurns=200;
        int numberOfTurns=0;
        while (!someOneWon() && numberOfTurns!=maxTurns){
            MockView currentP=getCurrentPlayerMockView();
            SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
            randomNum1= ThreadLocalRandom.current().nextInt(0,selectPawnRequestMessage.getAvailablePositions().size());
            gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));

/*            System.out.println("----------------------------------------------------------------------------");
            System.out.println("---- PLAYER: "+currentP.getName()+" Pawn: "+game.getSelectedPawnCopy().getId()+" GOD: "+game.getPlayer(currentP.getName()).getCurrentCard().getName()+" ----");
            System.out.println("BEFORE TURN:");
            simpleCompleteBoardPrint();*/

            while(!(currentP.getLastReceivedMessage() instanceof TurnEndedMessage) && !someOneWon()){

                if(currentP.getLastReceivedMessage() instanceof ChosenPositionForMoveRequestMessage){
                    ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage = (ChosenPositionForMoveRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionForMoveRequestMessage.getAvailablePositions().size()==0){
/*
                        System.out.println("BOT INCASTRATO player:"+currentP.getName()+" Card:"+game.getCurrentPlayer().getCurrentCard().getName()+" selectedPawn:"+game.getCurrentAction().getSelectedPawn().getId());
*/
/*
                        simpleCompleteBoardPrint();
*/
                        return;
                    }
                    randomNum1= ThreadLocalRandom.current().nextInt(0, chosenPositionForMoveRequestMessage.getAvailablePositions().size());
                    gameLogicExecutor.setChosenPosition(chosenPositionForMoveRequestMessage.getAvailablePositions().get(randomNum1));
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenPositionForConstructRequestMessage){
                    ChosenPositionForConstructRequestMessage chosenPositionForConstructRequestMessage = (ChosenPositionForConstructRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionForConstructRequestMessage.getAvailablePositions().size()==0){
/*
                        System.out.println("BOT INCASTRATO player:"+currentP.getName()+" Card:"+game.getCurrentPlayer().getCurrentCard().getName()+" selectedPawn:"+game.getCurrentAction().getSelectedPawn().getId());
*/
                        simpleCompleteBoardPrint();
                        return;
                    }
                    randomNum1= ThreadLocalRandom.current().nextInt(0, chosenPositionForConstructRequestMessage.getAvailablePositions().size());
                    gameLogicExecutor.setChosenPosition(chosenPositionForConstructRequestMessage.getAvailablePositions().get(randomNum1));
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenBlockTypeRequestMessage){
                    ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage = (ChosenBlockTypeRequestMessage) currentP.getLastReceivedMessage();
                    randomNum1= ThreadLocalRandom.current().nextInt(0,chosenBlockTypeRequestMessage.getAvailableBlockTypes().size());
                    gameLogicExecutor.setChosenBlockType(chosenBlockTypeRequestMessage.getAvailableBlockTypes().get(randomNum1));
                }


            }
            numberOfTurns++;
            /*System.out.println("AFTER TURN");
            simpleCompleteBoardPrint();
            System.out.println("----------------------------------------------------------------------------");*/

        }
/*
        System.out.println("VINCITAAA");
*/


    }
    /**
     * This function tests random combinations of cards with three bots setting a limit to the maximum amount of turns
     */
    @Test void loopTwoBotsGame(){
        //SETUP VARIABLES
        int counter=0;
        int numberOfCardsRandomCombinationSwitch=20;
        int numberOfGamesToRunWithSameCards=20;
        int maxTurns=200;

        //COMMODITY VARIABLES
        int blocked,winner,unknown,maxNumberOfTurns;
        int totalBlocked=0, totalWinner=0,totalUnknown=0,totalMaxNumberOfTurns=0;
        int res,numberOfGamesTriedWithSameCards=0,numberOfCardsRandomCombinationSwitchTried=0;
        ArrayList<Integer> cardIds;

        do {
            blocked=0;
            winner=0;
            unknown=0;
            maxNumberOfTurns=0;
            numberOfGamesTriedWithSameCards=0;

            do {
                cardIds = randomCards();
                res = twoBotsGame(cardIds.get(0), cardIds.get(1), maxTurns);
                numberOfGamesTriedWithSameCards++;
                counter++;
                if (res == 0) {
                    blocked++;
                }
                else if (res == -1) {
                    maxNumberOfTurns++;
                }
                else if (res == 2) {
                    unknown++;
                }
                else if (res == 1) {
                    winner++;
                }
                else{
                    System.out.println("==========================ERROR=================================");
                }
            } while (numberOfGamesTriedWithSameCards < numberOfGamesToRunWithSameCards);

            System.out.println(String.format("%-18s","CARDS: " + cardIds.get(0) + ", " + cardIds.get(1)) + " === NUMBER OF GAMES RUNNED: " + numberOfGamesTriedWithSameCards + " RESULTS -> winner:" + winner + " | blocked: " + blocked + " | maxNumberOfTurns: " + maxNumberOfTurns + " | unknown: " + unknown);

            totalBlocked+=blocked;
            totalWinner+=winner;
            totalMaxNumberOfTurns+=maxNumberOfTurns;
            totalUnknown+=unknown;

            numberOfCardsRandomCombinationSwitchTried++;
        }while(numberOfCardsRandomCombinationSwitchTried<numberOfCardsRandomCombinationSwitch);

        System.out.println("-----------------------------------------------------------------------------------------------------");
        int tot=totalBlocked+totalMaxNumberOfTurns+totalUnknown+totalWinner;
        System.out.println("CURRENT SETTINGS -> numberOfPermutations: "+numberOfCardsRandomCombinationSwitch+" numberOfGamesWithThatPermutation: "+numberOfGamesToRunWithSameCards+" maxGameTurns: "+maxTurns);
        System.out.println("TOTAL("+counter+")"+" -> winner: "+totalWinner+ " maxNumberOfTurns: "+totalMaxNumberOfTurns+" blocked:"+totalBlocked+ " unknown: "+totalUnknown);
        System.out.println("-----------------------------------------------------------------------------------------------------");

    }
    /**
     * This function tests random combinations of cards with two bots setting a limit to the maximum amount of turns
     */
    @Test void loopThreeBotsGame(){
        //SETUP VARIABLES
        int counter=0;
        int numberOfCardsRandomCombinationSwitch=20;
        int numberOfGamesToRunWithSameCards=20;
        int maxTurns=200;

        //COMMODITY VARIABLES
        int blocked,winner,unknown,maxNumberOfTurns;
        int totalBlocked=0, totalWinner=0,totalUnknown=0,totalMaxNumberOfTurns=0;
        int res,numberOfGamesTriedWithSameCards=0,numberOfCardsRandomCombinationSwitchTried=0;
        ArrayList<Integer> cardIds;

        do {
            blocked=0;
            winner=0;
            unknown=0;
            maxNumberOfTurns=0;
            numberOfGamesTriedWithSameCards=0;

            do {
                cardIds = randomCards();
                res = threeBotsGame(cardIds.get(0), cardIds.get(1), cardIds.get(2), maxTurns);
                numberOfGamesTriedWithSameCards++;
                counter++;
                if (res == 0) {
                    blocked++;
                }
                else if (res == -1) {
                    maxNumberOfTurns++;
                }
                else if (res == 2) {
                    unknown++;
                }
                else if (res == 1) {
                    winner++;
                }
                else{
                    System.out.println("==========================ERROR=================================");
                }
            } while (numberOfGamesTriedWithSameCards < numberOfGamesToRunWithSameCards);

            System.out.println(String.format("%-18s","CARDS: " + cardIds.get(0) + ", " + cardIds.get(1) + ", " + cardIds.get(2)) + " === NUMBER OF GAMES RUNNED: " + numberOfGamesTriedWithSameCards + " RESULTS -> winner:" + winner + " | blocked: " + blocked + " | maxNumberOfTurns: " + maxNumberOfTurns + " | unknown: " + unknown);

            totalBlocked+=blocked;
            totalWinner+=winner;
            totalMaxNumberOfTurns+=maxNumberOfTurns;
            totalUnknown+=unknown;

            numberOfCardsRandomCombinationSwitchTried++;
        }while(numberOfCardsRandomCombinationSwitchTried<numberOfCardsRandomCombinationSwitch);

        System.out.println("-----------------------------------------------------------------------------------------------------");
        int tot=totalBlocked+totalMaxNumberOfTurns+totalUnknown+totalWinner;
        System.out.println("CURRENT SETTINGS -> numberOfPermutations: "+numberOfCardsRandomCombinationSwitch+" numberOfGamesWithThatPermutation: "+numberOfGamesToRunWithSameCards+" maxGameTurns: "+maxTurns);
        System.out.println("TOTAL("+counter+")"+" -> winner: "+totalWinner+ " maxNumberOfTurns: "+totalMaxNumberOfTurns+" blocked:"+totalBlocked+ " unknown: "+totalUnknown);
        System.out.println("-----------------------------------------------------------------------------------------------------");


    }
    /**
     * This function tests the removal of a player once the game is already started
     */
    @Test void removePlayerGameAlreadyStarted(){
        //the first player is added to the lobby, so he should receive the request for the number of players in the game
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        assertTrue(mockViews.get(0).getLastReceivedMessage() instanceof NumberOfPlayersRequestMessage);
        assertEquals(mockViews.get(0).getName(),mockViews.get(0).getLastReceivedMessage().getRecipients().get(0));

        //other players are added to the lobby, check that their message is set to null
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");
        assertEquals(0,mockViews.get(1).getReceivedMessages().size());
        assertEquals(0,mockViews.get(2).getReceivedMessages().size());

        //we set the number of players to 3 so the game will start immediately
        gameLogicExecutor.setNumberOfPlayers(3);

        //at this point the startGame function is called and one player will receive the message to set the cards
        String currentPlayerName=gameLogicExecutor.getCurrentPlayerName();
        gameLogicExecutor.removePlayer(currentPlayerName);

        for(MockView mockView : mockViews){
            if(!mockView.getName().equals(currentPlayerName)){
                assertTrue(mockView.getLastReceivedMessage() instanceof GameEndedMessage);
            }
        }


    }
    /**
     * This function tests the removal of a player when the game is not already started
     */
    @Test void removePlayerGameNotStarted(){
        //the first player is added to the lobby, so he should receive the request for the number of players in the game
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        assertTrue(mockViews.get(0).getLastReceivedMessage() instanceof NumberOfPlayersRequestMessage);
        assertEquals(mockViews.get(0).getName(),mockViews.get(0).getLastReceivedMessage().getRecipients().get(0));

        //other players are added to the lobby, check that their message is set to null
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");
        assertEquals(0,mockViews.get(1).getReceivedMessages().size());
        assertEquals(0,mockViews.get(2).getReceivedMessages().size());

        //at this point the startGame function is called and one player will receive the message to set the cards
        gameLogicExecutor.removePlayer("p1");


        for(MockView mockView : mockViews){
            if(!mockView.getName().equals("p1")){
                assertTrue(mockView.getReceivedMessages().size()==0 || mockView.getLastReceivedMessage() instanceof NumberOfPlayersRequestMessage);
            }
        }


    }
    /**
     * This function test the undo of the current action
     */
    @Test void undoCurrentAction() {
        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(3);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards = new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(2);
        inGameCards.add(3);
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(1);
        gameLogicExecutor.setChosenCard(2);
        gameLogicExecutor.setChosenCard(3);

        //SET THE FIRST PLAYER
        gameLogicExecutor.setStartPlayer("p1");

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        int randomNum2 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        while (randomNum2 == randomNum1) {
            randomNum2 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        randomNum2 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        while (randomNum2 == randomNum1) {
            randomNum2 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        randomNum2 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        while (randomNum2 == randomNum1) {
            randomNum2 = ThreadLocalRandom.current().nextInt(0, m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);


        MockView currentP = getCurrentPlayerMockView();
        SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        randomNum1 = ThreadLocalRandom.current().nextInt(0, selectPawnRequestMessage.getAvailablePositions().size());
        gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));

        ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage = (ChosenPositionForMoveRequestMessage) currentP.getLastReceivedMessage();
        if (m.getAvailablePositions().size() < 1) {
            System.out.println("BOT INCASTRATO player:" + currentP.getName() + "selectedPawn:" + game.getCurrentAction().getSelectedPawn().getId());
            simpleCompleteBoardPrint();
            return;
        }
        randomNum1 = ThreadLocalRandom.current().nextInt(0, chosenPositionForMoveRequestMessage.getAvailablePositions().size());

        gameLogicExecutor.undoCurrentAction();
        assertTrue(currentP.getLastReceivedMessage() instanceof ChosenPositionForMoveRequestMessage);
        assertTrue(currentP.getReceivedMessages().get(currentP.getReceivedMessages().size()-2) instanceof UndoUpdateMessage);



    }
    /**
     * This function stress test the model with lots of games with medusa
     */
    @Test void medusaStressTest(){
        //SETUP VARIABLES
        int counter=0;
        int numberOfCardsRandomCombinationSwitch=20;
        int numberOfGamesToRunWithSameCards=100;
        int maxTurns=200;

        //COMMODITY VARIABLES
        int blocked,winner,unknown,maxNumberOfTurns;
        int totalBlocked=0, totalWinner=0,totalUnknown=0,totalMaxNumberOfTurns=0;
        int res,numberOfGamesTriedWithSameCards=0,numberOfCardsRandomCombinationSwitchTried=0;
        ArrayList<Integer> cardIds;

        do {
            blocked=0;
            winner=0;
            unknown=0;
            maxNumberOfTurns=0;
            numberOfGamesTriedWithSameCards=0;

            do {
                cardIds = randomCardsWithMedusa();
                res = threeBotsGame(cardIds.get(0), cardIds.get(1), cardIds.get(2), maxTurns);
                numberOfGamesTriedWithSameCards++;
                counter++;
                if (res == 0) {
                    blocked++;
                }
                else if (res == -1) {
                    maxNumberOfTurns++;
                }
                else if (res == 2) {
                    unknown++;
                }
                else if (res == 1) {
                    winner++;
                }
                else{
                    System.out.println("==========================ERROR=================================");
                }
            } while (numberOfGamesTriedWithSameCards < numberOfGamesToRunWithSameCards);

            System.out.println(String.format("%-18s","CARDS: " + cardIds.get(0) + ", " + cardIds.get(1) + ", " + cardIds.get(2)) + " === NUMBER OF GAMES RUNNED: " + numberOfGamesTriedWithSameCards + " RESULTS -> winner:" + winner + " | blocked: " + blocked + " | maxNumberOfTurns: " + maxNumberOfTurns + " | unknown: " + unknown);

            totalBlocked+=blocked;
            totalWinner+=winner;
            totalMaxNumberOfTurns+=maxNumberOfTurns;
            totalUnknown+=unknown;

            numberOfCardsRandomCombinationSwitchTried++;
        }while(numberOfCardsRandomCombinationSwitchTried<numberOfCardsRandomCombinationSwitch);

        System.out.println("-----------------------------------------------------------------------------------------------------");
        int tot=totalBlocked+totalMaxNumberOfTurns+totalUnknown+totalWinner;
        System.out.println("CURRENT SETTINGS -> numberOfPermutations: "+numberOfCardsRandomCombinationSwitch+" numberOfGamesWithThatPermutation: "+numberOfGamesToRunWithSameCards+" maxGameTurns: "+maxTurns);
        System.out.println("TOTAL("+counter+")"+" -> winner: "+totalWinner+ " maxNumberOfTurns: "+totalMaxNumberOfTurns+" blocked:"+totalBlocked+ " unknown: "+totalUnknown);
        System.out.println("-----------------------------------------------------------------------------------------------------");
    }


    //COMMODITY FUNCTIONS

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
            a.addVisitor(gameLogicExecutor);
        }
        for(Action a : basicActionList2){
            a.addVisitor(gameLogicExecutor);
        }
        for(Action a : basicActionList3){
            a.addVisitor(gameLogicExecutor);
        }

        p = new Player("ian");
        currentPlayer=p;
        p.addPawn(new Pawn("black",0));
        p.addPawn(new Pawn("black",1));
        p.setCurrentCard(new Card("Pippo1", 1, basicActionList1));
        game.addPlayer(p);
        game.setCurrentPlayer(p);
        p1 = new Position(1, 1);
        p2 = new Position(4, 4);
        game.board.setPawnPosition(p.getPawnList().get(0), p1);
        game.board.setPawnPosition(p.getPawnList().get(1), p2);

        p = new Player("luca");
        p.addPawn(new Pawn("white",2));
        p.addPawn(new Pawn("white",3));
        p.setCurrentCard(new Card("Pippo2", 2, basicActionList2));
        game.addPlayer(p);
        p1 = new Position(1, 2);
        p2 = new Position(4, 3);
        game.board.setPawnPosition(p.getPawnList().get(0), p1);
        game.board.setPawnPosition(p.getPawnList().get(1), p2);

        p = new Player("riccardo");
        p.addPawn(new Pawn("green",4));
        p.addPawn(new Pawn("green",5));
        p.setCurrentCard(new Card("Pippo3", 3, basicActionList3));
        game.addPlayer(p);
        p1 = new Position(1, 3);
        p2 = new Position(4, 0);
        game.board.setPawnPosition(p.getPawnList().get(0), p1);
        game.board.setPawnPosition(p.getPawnList().get(1), p2);
        gameLogicExecutor.loadCards();
    }
    /**
     * This function prints the complete board
     */
    private void simpleCompleteBoardPrint(){
        Cell[][] matrix=game.getBoard().getMatrixCopy();
        for(int i=0; i<matrix.length; i++){
            if(i==0){
                System.out.println("___________________________________");
            }
            for(int j=0; j<matrix[0].length; j++){
                Pawn p =matrix[i][j].getPawn();
                BlockType blockType = matrix[i][j].peekBlock();
                int level = blockType.getLevel();
                if(j==0){
                    System.out.print("|");
                }
                if(p==null) {
                    if(level!=0) {
                        System.out.print("l:" + level + "   |");
                    }else{
                        System.out.print("     |");
                    }
                }
                else {
                    if(level!=0) {
                        System.out.print("l:" + level + " "+p.getId()+"|");
                    }else{
                        System.out.print("    "+p.getId()+"|");
                    }
                }
                if(j==matrix[0].length-1){
                    System.out.print("\n");
                }
            }
            System.out.println("___________________________________");
        }
        System.out.println(" ");
    }
    /**
     * This function returns the not current players
     * @return the not current players arraylist
     */
    private ArrayList<Player> getNonCurrentPlayers(){
        ArrayList<Player> opponents = new ArrayList<>();
        for(Player p : game.getPlayers()){
            if(!p.getName().equals(game.getCurrentPlayer().getName())){
                opponents.add(p);
            }
        }
        return opponents;
    }
    /**
     * This function returns the not loser and not winner players
     * @return the list of players
     */
    private ArrayList<Player> getNonLoserOrWinnerPlayers(){
        ArrayList<Player> opponents = new ArrayList<>();
        for(Player p : game.getPlayers()){
            if(!p.getLoser()&&!p.getWinner()){
                opponents.add(p);
            }
        }
        return opponents;
    }
    /**
     * This function runs a bot game returning the status of the end
     * @param indexCard1 card in game
     * @param indexCard2 card in game
     * @param indexCard3 card in game
     * @param maxTurns maximum turns
     * @return the end status of the game
     */
    private int threeBotsGame(int indexCard1,int indexCard2,int indexCard3, int maxTurns){
        //create the mock views
        mockViews=new ArrayList<>();
        mockViews.add(new MockView("p1"));
        mockViews.add(new MockView("p2"));
        mockViews.add(new MockView("p3"));

        //create the game accordingly adding the mock views
        game= new Game();
        gameLogicExecutor=new GameLogicExecutor(game);

        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(3);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards=new ArrayList<>();

        inGameCards.add(game.getLoadedCards().get(indexCard1).getId());
        inGameCards.add(game.getLoadedCards().get(indexCard2).getId());
        inGameCards.add(game.getLoadedCards().get(indexCard3).getId());
        //System.out.println("Cards: "+inGameCards.get(0)+", "+inGameCards.get(1)+", "+inGameCards.get(2));
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard1).getId());
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard2).getId());
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard3).getId());

        //SET THE FIRST PLAYER
        int startPlayer = ThreadLocalRandom.current().nextInt(0,game.getPlayers().size());
        gameLogicExecutor.setStartPlayer(game.getPlayers().get(startPlayer).getName());

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);

        int numberOfTurns=0;
        while (!someOneWon() && numberOfTurns!=maxTurns){
            MockView currentP=getCurrentPlayerMockView();
            SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
            randomNum1= ThreadLocalRandom.current().nextInt(0,selectPawnRequestMessage.getAvailablePositions().size());
            gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));

            while(!(currentP.getLastReceivedMessage() instanceof TurnEndedMessage) && !someOneWon()){

                if(currentP.getLastReceivedMessage() instanceof ChosenPositionForMoveRequestMessage){
                    ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage = (ChosenPositionForMoveRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionForMoveRequestMessage.getAvailablePositions().size()==0){
                        gameLogicExecutor.undoTurn();
                        break;
                    }else {
                        randomNum1 = ThreadLocalRandom.current().nextInt(0, chosenPositionForMoveRequestMessage.getAvailablePositions().size());
                        gameLogicExecutor.setChosenPosition(chosenPositionForMoveRequestMessage.getAvailablePositions().get(randomNum1));
                    }
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenPositionForConstructRequestMessage){
                    ChosenPositionForConstructRequestMessage chosenPositionForConstructRequestMessage = (ChosenPositionForConstructRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionForConstructRequestMessage.getAvailablePositions().size()==0){
                        gameLogicExecutor.undoTurn();
                        break;
                    }
                    else {
                        randomNum1 = ThreadLocalRandom.current().nextInt(0, chosenPositionForConstructRequestMessage.getAvailablePositions().size());
                        gameLogicExecutor.setChosenPosition(chosenPositionForConstructRequestMessage.getAvailablePositions().get(randomNum1));
                    }
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenBlockTypeRequestMessage){
                    ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage = (ChosenBlockTypeRequestMessage) currentP.getLastReceivedMessage();
                    randomNum1= ThreadLocalRandom.current().nextInt(0,chosenBlockTypeRequestMessage.getAvailableBlockTypes().size());
                    gameLogicExecutor.setChosenBlockType(chosenBlockTypeRequestMessage.getAvailableBlockTypes().get(randomNum1));
                }
            }
            numberOfTurns++;
        }

        if(numberOfTurns==maxTurns){
            return -1;
        }
        else if(someOneWon()){
            return 1;
        }
        else{
            return 2;
        }

    }
    /**
     * This function runs a bot game returning the status of the end
     * @param indexCard1 card in game
     * @param indexCard2 card in game
     * @param maxTurns maximum turns
     * @return the end status of the game
     */
    private int twoBotsGame(int indexCard1, int indexCard2, int maxTurns){
        //create the mock views
        mockViews=new ArrayList<>();
        mockViews.add(new MockView("p1"));
        mockViews.add(new MockView("p2"));

        //create the game accordingly adding the mock views
        game= new Game();
        gameLogicExecutor=new GameLogicExecutor(game);

        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(2);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards=new ArrayList<>();

        inGameCards.add(game.getLoadedCards().get(indexCard1).getId());
        inGameCards.add(game.getLoadedCards().get(indexCard2).getId());
        //System.out.println("Cards: "+inGameCards.get(0)+", "+inGameCards.get(1)+", "+inGameCards.get(2));
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard1).getId());
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard2).getId());


        //SET THE FIRST PLAYER
        int startPlayer = ThreadLocalRandom.current().nextInt(0,game.getPlayers().size());
        gameLogicExecutor.setStartPlayer(game.getPlayers().get(startPlayer).getName());

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);

        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);


        int numberOfTurns=0;
        while (!someOneWon() && numberOfTurns!=maxTurns){
            MockView currentP=getCurrentPlayerMockView();
            SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
            randomNum1= ThreadLocalRandom.current().nextInt(0,selectPawnRequestMessage.getAvailablePositions().size());
            gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));

            while(!(currentP.getLastReceivedMessage() instanceof TurnEndedMessage) && !someOneWon()){

                if(currentP.getLastReceivedMessage() instanceof ChosenPositionForMoveRequestMessage){
                    ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage = (ChosenPositionForMoveRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionForMoveRequestMessage.getAvailablePositions().size()==0){
                        gameLogicExecutor.undoTurn();
                        break;
                    }else {
                        randomNum1 = ThreadLocalRandom.current().nextInt(0, chosenPositionForMoveRequestMessage.getAvailablePositions().size());
                        gameLogicExecutor.setChosenPosition(chosenPositionForMoveRequestMessage.getAvailablePositions().get(randomNum1));
                    }
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenPositionForConstructRequestMessage){
                    ChosenPositionForConstructRequestMessage chosenPositionForConstructRequestMessage = (ChosenPositionForConstructRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionForConstructRequestMessage.getAvailablePositions().size()==0){
                        gameLogicExecutor.undoTurn();
                        break;
                    }
                    else {
                        randomNum1 = ThreadLocalRandom.current().nextInt(0, chosenPositionForConstructRequestMessage.getAvailablePositions().size());
                        gameLogicExecutor.setChosenPosition(chosenPositionForConstructRequestMessage.getAvailablePositions().get(randomNum1));
                    }
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenBlockTypeRequestMessage){
                    ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage = (ChosenBlockTypeRequestMessage) currentP.getLastReceivedMessage();
                    randomNum1= ThreadLocalRandom.current().nextInt(0,chosenBlockTypeRequestMessage.getAvailableBlockTypes().size());
                    gameLogicExecutor.setChosenBlockType(chosenBlockTypeRequestMessage.getAvailableBlockTypes().get(randomNum1));
                }
            }
            numberOfTurns++;
        }

        if(numberOfTurns==maxTurns){
            return -1;
        }
        else if(someOneWon()){
            return 1;
        }
        else{
            return 2;
        }
    }
    /**
     * This function returns a random permutation of cards
     * @return the random cards ids
     */
    private ArrayList<Integer> randomCards(){
        ArrayList<Integer> cardIds = new ArrayList<>();
        //cardIds.add(11); //WE WANT MEDUSA
        //cardIds.add(12); //WE WANT TRITON
        int randomId;
        while(cardIds.size()<3){
            randomId=ThreadLocalRandom.current().nextInt(0,14);
            if(!cardIds.contains(randomId)){
                cardIds.add(randomId);
            }
        }
        return cardIds;
    }
    /**
     * This function returns the current player mockview
     * @return the mockview
     */
    private MockView getCurrentPlayerMockView(){
        String currentPlayerName=gameLogicExecutor.getCurrentPlayerName();
        for(MockView mock : mockViews){
            if(mock.getName().equals(currentPlayerName)){
                return mock;
            }
        }
        return null;
    }
    /**
     * This function checks if someone won
     * @return the result of the operation
     */
    private boolean someOneWon(){
        for(MockView mockView : mockViews){
            for(RequestAndUpdateMessage m : mockView.getReceivedMessages()){
                if(m instanceof YouWonMessage){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This function returns random cards with medusa in it
     * @return three cards ids
     */
    private ArrayList<Integer> randomCardsWithMedusa() {
        ArrayList<Integer> cardIds = new ArrayList<>();
        cardIds.add(11); //WE WANT MEDUSA
        int randomId;
        while(cardIds.size()<3){
            randomId=ThreadLocalRandom.current().nextInt(0,14);
            if(!cardIds.contains(randomId)){
                cardIds.add(randomId);
            }
        }
        return cardIds;
    }


}