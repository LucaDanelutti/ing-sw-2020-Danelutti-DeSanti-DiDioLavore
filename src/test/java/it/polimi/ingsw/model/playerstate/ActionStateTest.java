package it.polimi.ingsw.model.playerstate;

import it.polimi.ingsw.model.action.Action;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

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
}