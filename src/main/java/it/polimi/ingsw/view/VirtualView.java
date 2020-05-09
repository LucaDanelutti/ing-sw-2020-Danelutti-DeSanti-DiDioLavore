package it.polimi.ingsw.view;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;
import it.polimi.ingsw.view.listeners.SetsListener;

public class VirtualView extends SetObservable implements RequestsAndUpdateListener {
    private ClientConnection clientConnection;

    private class MessageReceiver implements SetsListener {
        @Override
        public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {}

        @Override
        public void update(ChosenCardSetMessage chosenCardSetMessage) {
            notifyListeners(chosenCardSetMessage);
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

    public VirtualView(ClientConnection c) {
        this.clientConnection = c;
        c.addListener(new MessageReceiver());
        System.out.println("VirtualView created!");
    }

    @Override
    public void update(NicknameRequestMessage nicknameRequestMessage) {
    }

    @Override
    public void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage) {
    }

    @Override
    public void update(ChosenCardRequestMessage chosenCardRequestMessage) {
    }

    @Override
    public void update(ChosenPositionRequestMessage chosenPositionRequestMessage) {
    }

    @Override
    public void update(FirstPlayerRequestMessage firstPlayerRequestMessage) {
    }

    @Override
    public void update(InGameCardsRequestMessage inGameCardsRequestMessage) {
    }

    @Override
    public void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage) {
    }

    @Override
    public void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage) {
    }

    @Override
    public void update(SelectPawnRequestMessage selectPawnRequestMessage) {
    }

    @Override
    public void update(CellUpdateMessage cellUpdateMessage) {
    }

    @Override
    public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {
    }

    @Override
    public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage) {
    }

    @Override
    public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage) {
    }

    @Override
    public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage) {
    }


    @Override
    public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
    }

    @Override
    public void update(GameStartMessage gameStartMessage) {
        clientConnection.asyncSend(gameStartMessage);
    }

    @Override
    public void update(TurnEndedMessage turnEndedMessage) {
    }

    @Override
    public void update(YouLostMessage youLostMessage) {
    }

    @Override
    public void update(YouWonMessage youWonMessage) {
    }

    @Override
    public void update(gameStartedAndYouAreNotSelectedMessage gameStartedAndYouAreNotSelectedMessage) {
    }

    @Override
    public void update(YouLostAndSomeoneWonMessage youLostAndSomeoneWonMessage) {
    }
}
