package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Card: represents a power up card, has a name and an id
 */
public class Card {
    private String name;
    private int id;
    private ArrayList<Action> defaultActionList = new ArrayList<>();
    private ArrayList<Action> currentActionList = new ArrayList<>();
    private String description;

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

    /**
     * this is the copy constructor for Card class
     * @param toBeCopied the card to be copied
     */
    public Card(Card toBeCopied){
        this.name = toBeCopied.name;
        this.id = toBeCopied.id;
        this.description = toBeCopied.description;
        if(toBeCopied.defaultActionList != null) {
            for (Action action : toBeCopied.defaultActionList) {
                this.defaultActionList.add(action);
            }
        }
        if(toBeCopied.currentActionList != null) {
            for (Action action : toBeCopied.currentActionList) {
                this.currentActionList.add(action);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id &&
                Objects.equals(name, card.name) &&
                Objects.equals(defaultActionList, card.defaultActionList) &&
                Objects.equals(currentActionList, card.currentActionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, defaultActionList, currentActionList);
    }

    String getName() {
        return name;
    }

    int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    ArrayList<Action> getDefaultActionListCopy() {
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

    /**
     * Get method of the variable currentActionList, returns the reference of currentActionList
     */
    ArrayList<Action> getCurrentActionList() {
        return currentActionList;
    }

    /**
     * resetCurrentActionList method: copies the defaultActionList to currentActionList
     */
    void resetCurrentActionList() {
        currentActionList = new ArrayList<>();
        if (defaultActionList != null) {
            for (Action action : defaultActionList) {
                currentActionList.add(action.duplicate());
            }
        } else {
            currentActionList = null;
        }
    }
}
