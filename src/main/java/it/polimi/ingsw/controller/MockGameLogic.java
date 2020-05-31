package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ModelInterface;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;

import java.util.ArrayList;

public class MockGameLogic implements ModelInterface {
    private String lastCall = "";
    private String currentPlayer;

    public MockGameLogic(Game game, String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getLastCall() {
        return lastCall;
    }

    @Override
    public String getCurrentPlayerName() {
        return currentPlayer;
    }

    @Override
    public Boolean setChosenBlockType(BlockType blockType) {
        lastCall = "setChosenBlockType";
        return true;
    }

    @Override
    public Boolean setChosenCard(int cardID) {
        lastCall = "setChosenCard";
        return true;
    }

    @Override
    public Boolean setChosenPosition(Position chosenPos) {
        lastCall = "setChosenPosition";
        return true;
    }

    @Override
    public Boolean setStartPlayer(String player) {
        lastCall = "setStartPlayer";
        return true;
    }

    @Override
    public Boolean setInGameCards(ArrayList<Integer> cards) {
        lastCall = "setInGameCards";
        return true;
    }

    @Override
    public Boolean setPawnsPositions(int idWorker1, Position workerPos1, int idWorker2, Position workerPos2) {
        lastCall = "setPawnsPositions";
        return true;
    }

    @Override
    public Boolean setNumberOfPlayers(int numberOfPlayers) {
        lastCall = "setNumberOfPlayers";
        return true;
    }

    @Override
    public Boolean setSelectedPawn(Position selectedPawnPosition) {
        lastCall = "setSelectedPawn";
        return true;
    }

    @Override
    public Boolean undoTurn() {
        lastCall = "undoTurn";
        return true;
    }

    @Override
    public Boolean undoCurrentAction() {
        lastCall = "undoCurrentAction";
        return true;
    }
}
