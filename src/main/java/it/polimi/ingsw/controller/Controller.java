package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameLogicExecutor;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.listeners.SetsListener;

public class Controller implements SetsListener {
    private final GameLogicExecutor gameLogic;

    public Controller(GameLogicExecutor gameLogic){
        this.gameLogic = gameLogic;
    }

    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(chosenBlockTypeSetMessage.getNameOfTheSender())) {
            gameLogic.setChosenBlockType(chosenBlockTypeSetMessage.getBlockType());
        }
    }

    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(chosenCardSetMessage.getNameOfTheSender())) {
            gameLogic.setChosenCard(chosenCardSetMessage.getCardId());
        }
    }

    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(chosenPositionSetMessage.getNameOfTheSender())) {
            gameLogic.setChosenPosition(chosenPositionSetMessage.getWorkerPos());
        }
    }

    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(firstPlayerSetMessage.getNameOfTheSender())) {
            gameLogic.setStartPlayer(firstPlayerSetMessage.getName());
        }
    }

    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(inGameCardsSetMessage.getNameOfTheSender())) {
            gameLogic.setInGameCards(inGameCardsSetMessage.getCardsId());
        }
    }

    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(initialPawnPositionSetMessage.getNameOfTheSender())) {
            gameLogic.setPawnsPositions(initialPawnPositionSetMessage.getWorkerId1(), initialPawnPositionSetMessage.getWorkerPos1(), initialPawnPositionSetMessage.getWorkerId2(), initialPawnPositionSetMessage.getWorkerPos2());
        }
    }

    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(nicknameSetMessage.getNameOfTheSender())) {

        }
    }

    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {
        gameLogic.setNumberOfPlayers(numberOfPlayersSetMessage.getNumberOfPlayers());
    }

    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(selectedPawnSetMessage.getNameOfTheSender())) {
            gameLogic.setSelectedPawn(selectedPawnSetMessage.getWorkerPos());
        }
    }

    @Override
    public void update(UndoTurnSetMessage undoTurnSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(undoTurnSetMessage.getNameOfTheSender())) {
            gameLogic.undoTurn();
        }
    }

    @Override
    public void update(UndoActionSetMessage undoActionSetMessage) {
        if (gameLogic.getCurrentPlayerName().equals(undoActionSetMessage.getNameOfTheSender())) {
            gameLogic.undoCurrentAction();
        }
    }
}
