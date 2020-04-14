package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CardTest {

    /**
     * This test checks whether the getActionList() methods manages properly an actionList = null
     */
    @Test
    void getActionList() {
        Card cardTester = new Card("Apollo", 1, null);
        ArrayList<Action> actionListTest = cardTester.getDefaultActionList();
    }
}