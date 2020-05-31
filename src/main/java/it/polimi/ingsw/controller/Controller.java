package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameLogicExecutor;
import it.polimi.ingsw.model.ModelInterface;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.listeners.SetsListener;

public class Controller implements SetsListener {
    private final ModelInterface modelInterface;

    public Controller(ModelInterface modelInterface){
        this.modelInterface = modelInterface;
    }

    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(chosenBlockTypeSetMessage.getNameOfTheSender())) {
            modelInterface.setChosenBlockType(chosenBlockTypeSetMessage.getBlockType());
        }
    }

    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(chosenCardSetMessage.getNameOfTheSender())) {
            modelInterface.setChosenCard(chosenCardSetMessage.getCardId());
        }
    }

    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(chosenPositionSetMessage.getNameOfTheSender())) {
            modelInterface.setChosenPosition(chosenPositionSetMessage.getWorkerPos());
        }
    }

    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(firstPlayerSetMessage.getNameOfTheSender())) {
            modelInterface.setStartPlayer(firstPlayerSetMessage.getName());
        }
    }

    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(inGameCardsSetMessage.getNameOfTheSender())) {
            modelInterface.setInGameCards(inGameCardsSetMessage.getCardsId());
        }
    }

    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(initialPawnPositionSetMessage.getNameOfTheSender())) {
            modelInterface.setPawnsPositions(initialPawnPositionSetMessage.getWorkerId1(), initialPawnPositionSetMessage.getWorkerPos1(), initialPawnPositionSetMessage.getWorkerId2(), initialPawnPositionSetMessage.getWorkerPos2());
        }
    }

    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(nicknameSetMessage.getNameOfTheSender())) {

        }
    }

    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {
        modelInterface.setNumberOfPlayers(numberOfPlayersSetMessage.getNumberOfPlayers());
    }

    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(selectedPawnSetMessage.getNameOfTheSender())) {
            modelInterface.setSelectedPawn(selectedPawnSetMessage.getWorkerPos());
        }
    }

    @Override
    public void update(UndoTurnSetMessage undoTurnSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(undoTurnSetMessage.getNameOfTheSender())) {
            modelInterface.undoTurn();
        }
    }

    @Override
    public void update(UndoActionSetMessage undoActionSetMessage) {
        if (modelInterface.getCurrentPlayerName().equals(undoActionSetMessage.getNameOfTheSender())) {
            modelInterface.undoCurrentAction();
        }
    }
}
