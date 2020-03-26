package it.polimi.ingsw.model;

import java.util.ArrayList;

class Player {
    final String name;
    ArrayList<Pawn> pawnList;
    Card currentCard;
    PlayerState state;

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

    void addPawn(Pawn pawn) {
        this.pawnList.add(pawn);
    }

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
