package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Card: represents a power up card, has a name and an id
 */
public class Card {
    private final String name;
    private final int id;
    private ArrayList<Action> defaultActionList = new ArrayList<>();
    private ArrayList<Action> currentActionList = new ArrayList<>();
    private String description;


                                                    //CONSTRUCTORS

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
                this.defaultActionList.add(action.duplicate());
            }
        }
        if(toBeCopied.currentActionList != null) {
            for (Action action : toBeCopied.currentActionList) {
                this.currentActionList.add(action.duplicate());
            }
        }
    }




                                                //SPECIFIC FUNCTIONS

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





                                                    //GETTERS

    /**
     * This is the getter for the name of the card
     * @return the name of the card
     */
    String getName() {
        return name;
    }
    /**
     * This is the getter for the ID of the card
     * @return the id of the card
     */
    int getId() {
        return id;
    }
    /**
     * This is the getter for the description of the card
     * @return the description of the card
     */
    String getDescription() {
        return description;
    }
    /**
     * Get method of the variable currentActionList, returns the reference of currentActionList
     */
    ArrayList<Action> getCurrentActionList() {
        return currentActionList;
    }
    /**
     * This is the getter for the default action list
     * @return the reference to the default action list
     */
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



                                                    //SETTERS

    public void setDescription(String description) {
        this.description = description;
    }





    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id &&
                Objects.equals(name, card.name) &&
                Objects.equals(defaultActionList, card.defaultActionList) &&
                Objects.equals(currentActionList, card.currentActionList);
    }
    @Override public int hashCode() {
        return Objects.hash(name, id, defaultActionList, currentActionList);
    }
}
