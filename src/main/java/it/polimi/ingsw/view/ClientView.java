package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;
import it.polimi.ingsw.view.listeners.SetsListener;
import it.polimi.ingsw.view.modelview.ModelView;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientView implements SetsListener {
    private String name;
    ModelView modelView;

    private ServerConnection serverConnection;

    public ClientView(ServerConnection c) {
        this.serverConnection = c;
        c.addListener(new MessageReceiver(this));
        System.out.println("ClientView created!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private class MessageReceiver implements RequestsAndUpdateListener {
        final ClientView clientView;

        public MessageReceiver(ClientView clientView) {
            this.clientView = clientView;
        }

        @Override
        public void update(NicknameRequestMessage nicknameRequestMessage) {
            System.out.println("What's your name?");
            Scanner stdin = new Scanner(System.in);
            String inputLine = stdin.nextLine();
            clientView.update(new NicknameSetMessage(new ArrayList<>(), inputLine));
        }

        @Override
        public void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage) {

        }

        @Override
        public void update(ChosenCardRequestMessage chosenCardRequestMessage) {
            System.out.println("ChosenCardRequestMessage message received!");
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
            modelView.onCellUpdate(cellUpdateMessage.getPosition(), cellUpdateMessage.getCell());
        }

        @Override
        public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {
            modelView.onChosenCardUpdate(chosenCardUpdateMessage.getChosenCard(), chosenCardUpdateMessage.getName());
        }

        @Override
        public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage) {
            modelView.onDoublePawnPositionUpdate(doublePawnPositionUpdateMessage.getWorkerId1(), doublePawnPositionUpdateMessage.getWorkerPos1(), doublePawnPositionUpdateMessage.getWorkerId2(), doublePawnPositionUpdateMessage.getWorkerPos2());
        }

        @Override
        public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage) {
            modelView.onPawnPositionUpdate(pawnPositionUpdateMessage.getWorkerId(), pawnPositionUpdateMessage.getWorkerPos());
        }

        @Override
        public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage) {
            modelView.onPawnRemoved(pawnRemoveUpdateMessage.getWorkerId());
        }

        @Override
        public void update(PlayerUpdateMessage playerUpdateMessage) {
            modelView.onPlayerUpdate(playerUpdateMessage.getName(), playerUpdateMessage.getColor(), playerUpdateMessage.getWorkerId1(), playerUpdateMessage.getWorkerId2());
        }

        @Override
        public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
            modelView.onSelectPawnUpdate(selectedPawnUpdateMessage.getWorkerId());
        }

        @Override
        public void update(GameStartMessage gameStartMessage) {
            System.out.println("GameStartMessage message received!");
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
        public void update(YouLostAndSomeoneWonMessage youLostAndSomeoneWonMessage) {

        }
    }

    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {

    }

    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        serverConnection.asyncSend(chosenCardSetMessage);
    }

    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {

    }

    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {

    }

    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {

    }

    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {

    }

    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {
        serverConnection.asyncSend(nicknameSetMessage);
    }

    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {

    }

    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {

    }
}
