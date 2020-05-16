package it.polimi.ingsw.view;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;
import it.polimi.ingsw.view.listeners.SetsListener;

public class VirtualView extends SetObservable implements RequestsAndUpdateListener, SetsListener{
    private ClientConnection clientConnection;
    private String name;

    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {
        chosenBlockTypeSetMessage.setNameOfTheSender(name);
        notifyListeners(chosenBlockTypeSetMessage);
    }

    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        chosenCardSetMessage.setNameOfTheSender(name);
        notifyListeners(chosenCardSetMessage);
    }

    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {
        chosenPositionSetMessage.setNameOfTheSender(name);
        notifyListeners(chosenPositionSetMessage);
    }

    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {
        firstPlayerSetMessage.setNameOfTheSender(name);
        notifyListeners(firstPlayerSetMessage);
    }

    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {
        inGameCardsSetMessage.setNameOfTheSender(name);
        notifyListeners(inGameCardsSetMessage);
    }

    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {
        initialPawnPositionSetMessage.setNameOfTheSender(name);
        notifyListeners(initialPawnPositionSetMessage);
    }

    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {
        nicknameSetMessage.setNameOfTheSender(name);
        notifyListeners(nicknameSetMessage);
    }

    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {
        numberOfPlayersSetMessage.setNameOfTheSender(name);
        notifyListeners(numberOfPlayersSetMessage);
    }

    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {
        selectedPawnSetMessage.setNameOfTheSender(name);
        notifyListeners(selectedPawnSetMessage);
    }

    @Override
    public void update(UndoTurnSetMessage message) {

    }

    @Override
    public void update(UndoActionSetMessage message) {

    }

    public VirtualView(ClientConnection c, String name) {
        this.clientConnection = c;
        this.name = name;
        c.addListener(this);
        System.out.println(name + ": virtualView created!");
    }

    @Override
    public void update(NicknameRequestMessage nicknameRequestMessage) {
        if (nicknameRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(nicknameRequestMessage);
        }
    }

    @Override
    public void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage) {
        if (chosenBlockTypeRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenBlockTypeRequestMessage);
        }
    }

    @Override
    public void update(ChosenCardRequestMessage chosenCardRequestMessage) {
        if (chosenCardRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenCardRequestMessage);
        }
    }

    @Override
    public void update(ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage) {
        if (chosenPositionForMoveRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenPositionForMoveRequestMessage);
        }
    }

    @Override
    public void update(FirstPlayerRequestMessage firstPlayerRequestMessage) {
        if (firstPlayerRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(firstPlayerRequestMessage);
        }
    }

    @Override
    public void update(InGameCardsRequestMessage inGameCardsRequestMessage) {
        if (inGameCardsRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(inGameCardsRequestMessage);
        }
    }

    @Override
    public void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage) {
        if (initialPawnPositionRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(initialPawnPositionRequestMessage);
        }
    }

    @Override
    public void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage) {
        if (numberOfPlayersRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(numberOfPlayersRequestMessage);
        }
    }

    @Override
    public void update(SelectPawnRequestMessage selectPawnRequestMessage) {
        if (selectPawnRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(selectPawnRequestMessage);
        }
    }

    @Override
    public void update(CellUpdateMessage cellUpdateMessage) {
        if (cellUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(cellUpdateMessage);
        }
    }

    @Override
    public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {
        if (chosenCardUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenCardUpdateMessage);
        }
    }

    @Override
    public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage) {
        if (doublePawnPositionUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(doublePawnPositionUpdateMessage);
        }
    }

    @Override
    public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage) {
        if (pawnPositionUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(pawnPositionUpdateMessage);
        }
    }

    @Override
    public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage) {
        if (pawnRemoveUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(pawnRemoveUpdateMessage);
        }
    }


    @Override
    public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
        if (selectedPawnUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(selectedPawnUpdateMessage);
        }
    }

    @Override
    public void update(GameStartMessage gameStartMessage) {
        if (gameStartMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(gameStartMessage);
        }
    }

    @Override
    public void update(TurnEndedMessage turnEndedMessage) {
        if (turnEndedMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(turnEndedMessage);
        }
    }

    @Override
    public void update(YouLostMessage youLostMessage) {
        if (youLostMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(youLostMessage);
        }
    }

    @Override
    public void update(YouWonMessage youWonMessage) {
        if (youWonMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(youWonMessage);
        }
    }

    @Override
    public void update(gameStartedAndYouAreNotSelectedMessage gameStartedAndYouAreNotSelectedMessage) {
        if (gameStartedAndYouAreNotSelectedMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(gameStartedAndYouAreNotSelectedMessage);
        }
    }

    @Override
    public void update(GameEndedMessage gameEndedMessage) {

    }

    @Override
    public void update(UndoUpdateMessage m) {

    }

    @Override
    public void update(ChosenPositionForConstructRequestMessage m) {

    }

    @Override
    public void update(YouLostAndSomeoneWonMessage youLostAndSomeoneWonMessage) {
        if (youLostAndSomeoneWonMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(youLostAndSomeoneWonMessage);
        }
    }
}
