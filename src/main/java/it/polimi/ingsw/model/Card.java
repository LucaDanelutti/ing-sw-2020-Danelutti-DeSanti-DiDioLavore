package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;

//TODO: gestire meglio/correttamente le NullPointerException nel costruttore e in getActionList
public class Card {
    private String name;
    private int id;
    private ArrayList<Action> actionList;

    /**
     This is the constructor of the class Card
     */
    Card(String name, int id, ArrayList<Action> actionList) {
        this.name = name;
        this.id = id;
        if (actionList != null) {
            for (Action action : actionList) {
                this.actionList.add(action.duplicate());
            }
        } else {
            this.actionList = null;
        }

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Action> getActionList() {
        if (actionList != null) {
            ArrayList<Action> copiedActionList = new ArrayList<>();
            for (Action action : actionList) {
                copiedActionList.add(action.duplicate());
            }
            return copiedActionList;
        } else {
            return null;
        }
    }
}
