package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameLogicExecutor;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.listeners.SetsListener;

//TODO: add check on the FROM parameter of the message with the getCurrentPlayerName of the Model

public class Controller implements SetsListener {
    private final GameLogicExecutor gameLogic;

    public Controller(GameLogicExecutor gameLogic){
        this.gameLogic = gameLogic;
    }

    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {}

    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        System.out.println("ChosenCardSetMessage message received by controller!");
    }

    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {}

    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {}

    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {}

    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {}

    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {}

    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {}

    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {}
}
