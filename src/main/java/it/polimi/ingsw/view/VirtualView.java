package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Observable;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.listeners.SetsListener;

public class VirtualView extends Observable {
    private ClientConnection clientConnection;

    private class MessageReceiver implements SetsListener {

        @Override
        public void update(Object o) {}

        @Override
        public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {}

        @Override
        public void update(ChosenCardSetMessage chosenCardSetMessage) {
            clientConnection.asyncSend("ChosenCardSetMessage message received!");
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
        c.asyncSend("VirtualView created!");
    }
}
