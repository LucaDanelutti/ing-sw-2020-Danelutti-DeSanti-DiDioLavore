package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ConstructAction;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.playerstate.ActionState;
import it.polimi.ingsw.model.playerstate.IdleState;
import it.polimi.ingsw.model.playerstate.PlayerState;
import it.polimi.ingsw.model.playerstate.PlayerStateType;
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
        basicMove= new MoveAction(false, new ArrayList<>(),true, false, false, false,false, false, new ArrayList<>(), false);
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

}