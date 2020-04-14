package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;

//TODO: gestire meglio/correttamente le NullPointerException nel costruttore e in getActionList
public class Card {
    private String name;
    private int id;
    private ArrayList<Action> defaultActionList;
    private ArrayList<Action> currentActionList;

    /**
     This is the constructor of the class Card
     */
    Card(String name, int id, ArrayList<Action> actionList) {
        this.name = name;
        this.id = id;
        if (actionList != null) {
            for (Action action : actionList) {
                this.defaultActionList.add(action.duplicate());
                this.currentActionList.add(action.duplicate());
            }
        } else {
            this.defaultActionList = null;
        }

    }

    String getName() {
        return name;
    }

    int getId() {
        return id;
    }

    ArrayList<Action> getDefaultActionList() {
        if (defaultActionList != null) {
            ArrayList<Action> copiedActionList = new ArrayList<>();
            for (Action action : defaultActionList) {
                copiedActionList.add(action.duplicate());
            }
            return copiedActionList;
        } else {
            return null;
        }
    }

    ArrayList<Action> getCurrentActionList() {
        if (currentActionList != null) {
            ArrayList<Action> copiedActionList = new ArrayList<>();
            for (Action action : currentActionList) {
                copiedActionList.add(action.duplicate());
            }
            return copiedActionList;
        } else {
            return null;
        }
    }
}
