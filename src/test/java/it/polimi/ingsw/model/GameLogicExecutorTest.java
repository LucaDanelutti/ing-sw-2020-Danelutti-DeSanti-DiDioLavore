package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
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
     * This test checks that if an action with denyMoveUp enabled and with a selectedPawn that has moved up, disables moveUp for other players
     */
    @Test void moveActionDenyMoveUpForOtherPlayers(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        MoveAction athenaMove = new MoveAction(false, new ArrayList<>(),true,false,false,false,true,false,new ArrayList<>(),false,false);
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false, false);
        basicConstruct= new ConstructAction(false,new ArrayList<>(),false,new ArrayList<>(),false,false,false);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(athenaMove);
        actions.add(basicConstruct.duplicate());
        for(Action a : actions){
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setState(new ActionState(actions));

        //before the action since our opponets have basicMove and basicConstruct, they should have moveUpEnable
        for(Player opponent : game.getPlayersIn(PlayerStateType.IdleState)){
            for (Action a : opponent.getCurrentCard().getCurrentActionList()){
                if(a.getActionType()==ActionType.MOVE){
                    assertEquals(true, ((MoveAction)a).getMoveUpEnable());
                }
            }
        }

        //to activate this effect we have to moveUp of one block our pawn, we create a level1 block near our pawn(0)
        game.getBoard().pawnConstruct(null, new Position(0,0),BlockType.LEVEL1);

        //then we move the pawn to the selected position
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(0,0));

        //we check that all other players have moveUp disable (in currentActionList) since our pawn moved up of one position
        assertEquals(1,currentPlayer.getPawnList().get(0).getDeltaHeight());
        for(Player opponent : game.getPlayersIn(PlayerStateType.IdleState)){
            for (Action a : opponent.getCurrentCard().getCurrentActionList()){
                if(a.getActionType()==ActionType.MOVE){
                    assertEquals(false, ((MoveAction)a).getMoveUpEnable());
                }
            }
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
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setState(new ActionState(actions));
        ActionState actionState = (ActionState)currentPlayer.getState();

        //before the move, the action list should have a size of 2
        assertEquals(2,actionState.getActionListCopy().size());

        //then we move the pawn to the selected position
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(0,0));

        //after the move the actionList should have a size of 3 and the added action should be a moveAction with the same addMoveIfOn array (a copy)
        assertEquals(3,actionState.getActionListCopy().size());
        assertEquals(tritonMove,actionState.getActionList().get(1));

        gameLogicExecutor.setChosenPosition(new Position(0,1));
        assertEquals(4,actionState.getActionListCopy().size());
        assertEquals(tritonMove,actionState.getActionList().get(2));


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
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setState(new ActionState(actions));
        ActionState actionState = (ActionState)currentPlayer.getState();

        //in 1,1 we have the selected pawn, in 1,2 we have an opponent pawn that we can swap
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
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
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setState(new ActionState(actions));
        ActionState actionState = (ActionState)currentPlayer.getState();

        //in 1,1 we have the selected pawn, in 1,2 we have an opponent pawn that we can push in 1,3
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
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
        game.getBoard().pawnConstruct(null, new Position(1,1),BlockType.LEVEL2);
        game.getBoard().pawnConstruct(null, new Position(0,0),BlockType.LEVEL1);
        game.getBoard().pawnConstruct(null, new Position(0,0),BlockType.LEVEL2);
        game.getBoard().pawnConstruct(null, new Position(0,0),BlockType.LEVEL3);

        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
        gameLogicExecutor.setChosenPosition(new Position(0,0));

        assertEquals(2,game.getPlayersIn(PlayerStateType.LoserState).size());
        assertEquals(PlayerStateType.WinnerState,currentPlayer.getState().getType());
        assertEquals(PlayerStateType.LoserState,game.getPlayer("luca").getState().getType());
        assertEquals(PlayerStateType.LoserState,game.getPlayer("riccardo").getState().getType());
    }



    //ConstructAction tests

    /**
     * This test checks that the execution of a constructAction in the simpleGameSetup works properly and the turn is passed
     * to the next player.
     */
    @Test void setChosenPositionForConstructActionTest(){
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
            a.addObserver(gameLogicExecutor);
        }

        //game.getPlayer("ian").setCurrentCard(new Card("prometheus",10,prometheusActionList));
        currentPlayer.setCurrentCard(new Card("prometheus",10,prometheusActionList));
        currentPlayer.setState(new ActionState(prometheusActionList));

        gameLogicExecutor.setSelectedPawn(game.getPlayer("ian").getPawnList().get(0).getPosition(), game.getPlayer("ian").getPawnList().get(1).getPosition());

        ActionState actionState = (ActionState) game.getPlayer("ian").getState();
        assertEquals(true, ((MoveAction)actionState.getActionList().get(1)).getMoveUpEnable());

        ArrayList<Position> chosenPos = actionState.getCurrentAction().availableCells(game.getBoard().getMatrixCopy());
        //let's skip the position for the optional construct and see if the MoveUp on the move for the current player is updated correctly
        gameLogicExecutor.setChosenPosition(chosenPos.get(0));
        ArrayList<BlockType> blockTypes = ((ConstructAction)actionState.getCurrentAction()).availableBlockTypes(chosenPos.get(0),game.getBoard().getMatrixCopy());
        gameLogicExecutor.setChosenBlockType(blockTypes.get(0)); //this is needed, a construct action is executed only if the chosenBlockType is set

        //at this time the next action should be loaded, and it is the moveAction, this moveAction must have MoveUp set to true
        assertEquals(false, ((MoveAction)actionState.getCurrentAction()).getMoveUpEnable());
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
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setCurrentCard(new Card("optionalConstruct",33, actions));
        currentPlayer.setState(new ActionState(actions));
        ActionState actionState = (ActionState) currentPlayer.getState();

        Pawn prevPawn = currentPlayer.getPawnList().get(0);
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
        gameLogicExecutor.setChosenPosition(null);
        gameLogicExecutor.setChosenBlockType(null);

        assertEquals(prevPawn, currentPlayer.getPawnList().get(0));
        assertEquals(basicMove,actionState.getCurrentAction());
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
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setState(new ActionState(actions));
        ActionState actionState = (ActionState)currentPlayer.getState();

        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
        game.getBoard().pawnConstruct(null, new Position(0,1),BlockType.LEVEL1);

        //MoveAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,1));

        //ConstructAction executed
        gameLogicExecutor.setChosenPosition(new Position(1,1));
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);

        //GeneralAction executed
        gameLogicExecutor.setChosenPosition(null);

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
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setState(new ActionState(actions));
        ActionState actionState = (ActionState)currentPlayer.getState();

        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
        game.getBoard().pawnConstruct(null, new Position(0,1),BlockType.LEVEL1);
        game.getBoard().pawnConstruct(null, new Position(4,4),BlockType.LEVEL1);


        //MoveAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,1));

        //ConstructAction executed
        gameLogicExecutor.setChosenPosition(new Position(1,1));
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);

        //GeneralAction executed
        gameLogicExecutor.setChosenPosition(null);

        assertEquals(0,game.getPlayer("luca").getPawnList().size());
        assertEquals(BlockType.LEVEL1,game.getBoard().getMatrixCopy()[1][2].peekBlock());
        assertNull(game.getBoard().getMatrixCopy()[1][2].getPawn());
        assertEquals(PlayerStateType.LoserState,game.getPlayer("luca").getState().getType());

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
            a.addObserver(gameLogicExecutor);
        }
        currentPlayer.setState(new ActionState(actions));
        ActionState actionState = (ActionState)currentPlayer.getState();

        //we execute the general action
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());
        gameLogicExecutor.setChosenPosition(null);

        for(Player opponent : game.getPlayersIn(PlayerStateType.IdleState)){
            for(Action a : opponent.getCurrentCard().getCurrentActionList()){
                if(a.getActionType()==ActionType.MOVE){
                    assertEquals(true, ((MoveAction)a).getNoWinIfOnPerimeter());
                }
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
     * The test checks that after a player has finished all of its actions, it is placed in idle and the next player is loaded
     */
    @Test void loadNextPlayer(){
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        gameLogicExecutor.setSelectedPawn(currentPlayer.getPawnList().get(0).getPosition(),currentPlayer.getPawnList().get(1).getPosition());

        //moveAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,0));

        //constructAction executed
        gameLogicExecutor.setChosenPosition(new Position(0,1));
        gameLogicExecutor.setChosenBlockType(BlockType.LEVEL1);

        //so here we should have that luca is in actionState and ian is in idle state, riccardo too.
        assertEquals(PlayerStateType.ActionState,game.getPlayer("luca").getState().getType());
        assertEquals(PlayerStateType.IdleState,game.getPlayer("ian").getState().getType());
        assertEquals(PlayerStateType.IdleState,game.getPlayer("riccardo").getState().getType());

        //let's check that luca has the correct action loaded in his ActionState
        ActionState actionState = (ActionState) game.getPlayer("luca").getState();
        assertEquals(game.getPlayer("luca").getCurrentCard().getCurrentActionList(),actionState.getActionListCopy());

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
        assertTrue(testGameLogicExecutor.setChosenCard(1));
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
        assertTrue(testGameLogicExecutor.setChosenCard(1));
        assertTrue(testGameLogicExecutor.setChosenCard(2));
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
        assertFalse(testGameLogicExecutor.setChosenCard(2));
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
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        testPlayer1.addPawn(new Pawn("990000"));
        testPlayer1.addPawn(new Pawn("990000"));
        testPlayer2.addPawn(new Pawn("009900"));
        testPlayer2.addPawn(new Pawn("009900"));
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        ArrayList<Position> testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(0, 0));
        testPositionArray.add(new Position(1, 1));
        assertTrue(testGameLogicExecutor.setPawnsPositions(testPositionArray));
        assertEquals(new Position(0, 0), testPlayer1.getPawnList().get(0).getPosition());
        assertEquals(new Position(1, 1), testPlayer1.getPawnList().get(1).getPosition());
        assertEquals(new Position(0, 0), testGame.getBoard().getMatrixCopy()[0][0].getPawn().getPosition());
        assertEquals(new Position(1, 1), testGame.getBoard().getMatrixCopy()[1][1].getPawn().getPosition());
        assertEquals(PlayerStateType.IdleState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.ChoosePawnsPositionState, testPlayer2.getState().getType());
    }

    /**
     * The scope of this test function is to test that setPawnsPositions method correctly sets the player pawns
     */
    @Test
    void setPawnsPositions_OkPositions_LastPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new ChoosePawnsPositionState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        testPlayer2.setCurrentCard(new Card("testCard1", 1, new ArrayList<>()));
        testPlayer1.addPawn(new Pawn("990000"));
        testPlayer1.addPawn(new Pawn("990000"));
        testPlayer2.addPawn(new Pawn("009900"));
        testPlayer2.addPawn(new Pawn("009900"));
        testPlayer2.getPawnList().get(0).setPosition(new Position(4,5));
        testPlayer2.getPawnList().get(1).setPosition(new Position(5,5));
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        ArrayList<Position> testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(0, 0));
        testPositionArray.add(new Position(1, 1));
        assertTrue(testGameLogicExecutor.setPawnsPositions(testPositionArray));
        assertEquals(new Position(0, 0), testPlayer1.getPawnList().get(0).getPosition());
        assertEquals(new Position(1, 1), testPlayer1.getPawnList().get(1).getPosition());
        assertEquals(new Position(0, 0), testGame.getBoard().getMatrixCopy()[0][0].getPawn().getPosition());
        assertEquals(new Position(1, 1), testGame.getBoard().getMatrixCopy()[1][1].getPawn().getPosition());
        assertEquals(PlayerStateType.IdleState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.ActionState, testPlayer2.getState().getType());
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
     * The scope of this test function is to test that setStartPlayer method correctly sets the first player
     */
    @Test
    void setStartPlayer_OtherPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new SelectFirstPlayerState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        Player testPlayer3 = new Player("testPlayer3", new IdleState());
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        testGame.addPlayer(testPlayer3);
        assertTrue(testGameLogicExecutor.setStartPlayer("testPlayer2"));
        assertEquals(PlayerStateType.IdleState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.ChoosePawnsPositionState, testPlayer2.getState().getType());
        assertEquals(PlayerStateType.IdleState, testPlayer3.getState().getType());
    }

    /**
     * The scope of this test function is to test that setStartPlayer method correctly sets the first player
     */
    @Test
    void setStartPlayer_SamePlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new SelectFirstPlayerState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        Player testPlayer3 = new Player("testPlayer3", new IdleState());
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        testGame.addPlayer(testPlayer3);
        assertTrue(testGameLogicExecutor.setStartPlayer("testPlayer1"));
        assertEquals(PlayerStateType.ChoosePawnsPositionState, testPlayer1.getState().getType());
        assertEquals(PlayerStateType.IdleState, testPlayer2.getState().getType());
        assertEquals(PlayerStateType.IdleState, testPlayer3.getState().getType());
    }

    /**
     * The scope of this test function is to test that setStartPlayer method correctly sets the first player
     */
    @Test
    void setStartPlayer_NotOkPlayer() {
        Game testGame = new Game();
        GameLogicExecutor testGameLogicExecutor = new GameLogicExecutor(testGame);
        Player testPlayer1 = new Player("testPlayer1", new SelectFirstPlayerState());
        Player testPlayer2 = new Player("testPlayer2", new IdleState());
        testGame.addPlayer(testPlayer1);
        testGame.addPlayer(testPlayer2);
        assertFalse(testGameLogicExecutor.setStartPlayer("testPlayer3"));
    }

    /**
     * The scope of this test function is to test that the set-up phase works as expected
     */
    @Test
    void setUpGame_3Players() {
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
        //Load cards end

        assertTrue(testGameLogicExecutor.addPlayer("testPlayer1"));
        assertTrue(testGameLogicExecutor.addPlayer("testPlayer2"));
        assertTrue(testGameLogicExecutor.addPlayer("testPlayer3"));
        assertTrue(testGameLogicExecutor.startGame());

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
        assertTrue(testGameLogicExecutor.setPawnsPositions(testPositionArray));
        testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(1, 0));
        testPositionArray.add(new Position(1, 1));
        assertTrue(testGameLogicExecutor.setPawnsPositions(testPositionArray));
        testPositionArray = new ArrayList<>();
        testPositionArray.add(new Position(2, 0));
        testPositionArray.add(new Position(2, 1));
        assertTrue(testGameLogicExecutor.setPawnsPositions(testPositionArray));

        assertTrue(testGame.getPlayersIn(PlayerStateType.ActionState).size() == 1);
        assertTrue(testGame.getPlayersIn(PlayerStateType.ActionState).get(0).getName() == "testPlayer1");
        assertTrue(testGame.getPlayersIn(PlayerStateType.IdleState).size() == 2);

        assertEquals(testCard1, testPlayer.getCurrentCard());
        assertEquals(testCard2, testPlayerBis.getCurrentCard());
        assertEquals(testCard3, godLikePlayer.getCurrentCard());

        assertEquals(new Position(0, 0), testGame.getPlayer("testPlayer1").getPawnList().get(0).getPosition());
        assertEquals(new Position(0, 0), testGame.getPlayer("testPlayer1").getPawnList().get(0).getPosition());

        assertEquals(new Position(1, 0), testGame.getPlayer(testGame.getNextPlayer(PlayerStateType.ActionState).getName()).getPawnList().get(0).getPosition());
        assertEquals(new Position(1, 0), testGame.getPlayer(testGame.getNextPlayer(PlayerStateType.ActionState).getName()).getPawnList().get(0).getPosition());
    }

    /**
     * The scope of this test function is to test that loadCards properly loads the json into Cards
     */
    @Test
    void loadCards() {
        simpleGameSetupWith3PlayersOneInActionStateOthersInIdle();
        gameLogicExecutor.loadCards();

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
        assertEquals(ActionType.MOVE, game.getLoadedCards().get(1).getDefaultActionListCopy().get(1).getActionType());
        //checks whether the list of notAvailableCells loaded from the json is properly set to the action attribute
        assertTrue(game.getLoadedCards().get(10).getDefaultActionListCopy().get(2).getNotAvailableCell().containsAll(expectedList) && expectedList.containsAll(game.getLoadedCards().get(10).getDefaultActionListCopy().get(2).getNotAvailableCell()));
    }
}