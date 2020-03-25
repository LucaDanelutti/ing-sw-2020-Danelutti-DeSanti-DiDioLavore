package it.polimi.ingsw.model;

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
        ArrayList<Action> actionListTest = cardTester.getActionList();
    }
}