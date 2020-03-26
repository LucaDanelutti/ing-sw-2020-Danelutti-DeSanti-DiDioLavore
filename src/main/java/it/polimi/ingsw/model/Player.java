package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * This class represents every player in the game
 */
class Player {
    final String name;
    ArrayList<Pawn> pawnList;
    Card currentCard;
    PlayerState state;

    /**
     * Constructor of this class
     * @param name defines the name of the player
     * @param state defines the state of the player
     */
    Player(String name, PlayerState state) {
        this.name = name;
        this.state = state;
        pawnList = new ArrayList<Pawn>();
    }

    String getName() {
        return name;
    }

    ArrayList<Pawn> getPawnList() {
        return pawnList;
    }

    /**
     * addPawn method to add the provided Pawn to the pawnList
     * @param pawn is the pawn to add
     */
    void addPawn(Pawn pawn) {
        this.pawnList.add(pawn);
    }

    /**
     * removePawn method to remove the provided Pawn from the pawnList
     * @param pawn is the pawn to remove
     */
    void removePawn(Pawn pawn) {
        this.pawnList.remove(pawn);
    }

    Card getCurrentCard() {
        return currentCard;
    }

    void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    PlayerState getState() {
        return state;
    }

    void setState(PlayerState state) {
        this.state = state;
    }
}
