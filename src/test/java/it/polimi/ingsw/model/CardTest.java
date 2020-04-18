package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.MoveAction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    /**
     * This test checks whether the getActionList() methods manages properly an actionList = null
     */
    @Test
    void getActionList() {
        Card cardTester = new Card("Apollo", 1, null);
        ArrayList<Action> actionListTest = cardTester.getDefaultActionListCopy();
    }

    /**
     * This test checks whether the resetCurrentActionList() copies properly the defaultActionList to currentActionList
     */
    @Test
    void resetCurrentActionList() {
        ArrayList<Action> testActionList = new ArrayList<>();
        MoveAction testAction1 = new MoveAction(true, null, true, false, false, false, false, false, null, false);
        MoveAction testAction2 = new MoveAction(true, null, true, true, true, false, false, false, null, false);
        testActionList.add(testAction1);
        testActionList.add(testAction2);
        Card testCard = new Card("Apollo", 1, testActionList);
        assertTrue(testCard.getCurrentActionList().size() == 2);
        MoveAction testAction3 = new MoveAction(true, null, true, false, true, true, false, false, null, false);
        testCard.getCurrentActionList().add(testAction3);
        assertTrue(testCard.getCurrentActionList().size() == 3);
        testCard.resetCurrentActionList();
        assertTrue(testCard.getCurrentActionList().size() == 2);
        assertEquals(testCard.getCurrentActionList().get(0), testAction1);
        assertEquals(testCard.getCurrentActionList().get(1), testAction2);
        assertNotSame(testCard.getCurrentActionList().get(0), testAction1);
        assertNotSame(testCard.getCurrentActionList().get(1), testAction2);
    }
}