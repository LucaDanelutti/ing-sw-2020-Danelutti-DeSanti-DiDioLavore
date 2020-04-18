package it.polimi.ingsw.model.playerstate;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.MoveAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for ActionState
 */
class ActionStateTest {
    ActionState testState;
    ArrayList<Action> testActionList;

    @BeforeEach
    void init() {
        testActionList = new ArrayList<Action>();
        testState = new ActionState(testActionList);
    }

    /**
     * The scope of this test function is to test that the constructor does not create
     * an ActionState object with actionList set to null
     */
    @Test
    void ActionState_NoActionList() {
        assertThrows(InvalidGameException.class, () -> {new ActionState(null);});
    }


    /**
     * The scope of this test function is to test that setCurrentAction method sets the right action
     */
    @Test
    void setCurrentAction_FirstAction() {
        MoveAction testMoveAction = new MoveAction(true, null, true, false, false, false, false, false, null, false);
        testActionList.add(testMoveAction);
        testState = new ActionState(testActionList);
        testState.setCurrentAction();
        assertEquals(testMoveAction, testState.getCurrentActionCopy());
    }

    /**
     * The scope of this test function is to test that setCurrentAction method sets the right action
     */
    @Test
    void setCurrentAction_NotFirstAction() {
        MoveAction testMoveAction1 = new MoveAction(true, null, true, false, false, false, false, false, null, false);
        MoveAction testMoveAction2 = new MoveAction(true, null, true, true, true, false, false, false, null, false);
        testActionList.add(testMoveAction1);
        testActionList.add(testMoveAction2);
        testState = new ActionState(testActionList);
        testState.setCurrentAction();
        assertEquals(testMoveAction1, testState.getCurrentActionCopy());
        testState.setCurrentAction();
        assertEquals(testMoveAction2, testState.getCurrentActionCopy());
    }

    /**
     * The scope of this test function is to test that setCurrentAction method sets the right action
     */
    @Test
    void setCurrentAction_NoNextAction() {
        MoveAction testMoveAction1 = new MoveAction(true, null, true, false, false, false, false, false, null, false);
        MoveAction testMoveAction2 = new MoveAction(true, null, true, true, true, false, false, false, null, false);
        testActionList.add(testMoveAction1);
        testActionList.add(testMoveAction2);
        testState = new ActionState(testActionList);
        testState.setCurrentAction();
        assertEquals(testMoveAction1, testState.getCurrentActionCopy());
        testState.setCurrentAction();
        assertEquals(testMoveAction2, testState.getCurrentActionCopy());
        testState.setCurrentAction();
        assertTrue(testState.getCurrentActionCopy() == null);
    }

    @Test
    void addActionAfterCurrentOne() {
        MoveAction testMoveAction1 = new MoveAction(true, null, true, false, false, false, false, false, null, false);
        MoveAction testMoveAction2 = new MoveAction(true, null, true, true, true, false, false, false, null, false);
        testActionList.add(testMoveAction1);
        testActionList.add(testMoveAction2);
        testState = new ActionState(testActionList);
        testState.setCurrentAction();
        assertTrue(testState.getActionListCopy().size() == 2);
        MoveAction testMoveAction3 = new MoveAction(true, null, true, false, true, true, false, false, null, false);
        testState.addActionAfterCurrentOne(testMoveAction3);
        assertTrue(testState.getActionListCopy().size() == 3);
        assertEquals(testMoveAction1, testState.getActionListCopy().get(0));
        assertEquals(testMoveAction3, testState.getActionListCopy().get(1));
        assertEquals(testMoveAction2, testState.getActionListCopy().get(2));
    }

    @Test
    void addActionAfterCurrentOne_NoCurrentAction() {
        MoveAction testMoveAction1 = new MoveAction(true, null, true, false, false, false, false, false, null, false);
        MoveAction testMoveAction2 = new MoveAction(true, null, true, true, true, false, false, false, null, false);
        testActionList.add(testMoveAction1);
        testState = new ActionState(testActionList);
        assertTrue(testState.getActionListCopy().size() == 1);
        testState.addActionAfterCurrentOne(testMoveAction2);
        assertTrue(testState.getActionListCopy().size() == 2);
        assertEquals(testMoveAction2, testState.getActionListCopy().get(0));
        assertEquals(testMoveAction1, testState.getActionListCopy().get(1));
    }
}