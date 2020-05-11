package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;
import it.polimi.ingsw.view.listeners.SetsListener;
import it.polimi.ingsw.view.modelview.ModelView;
import it.polimi.ingsw.view.modelview.PlayerView;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientView implements SetsListener {
    private String name;
    private ModelView modelView;
    private UserInterface userInterface;
    private ServerConnection serverConnection;

    public ClientView() {
        this.name = null;
        this.modelView = new ModelView();
        this.userInterface = null;
        this.serverConnection = null;
    };

    //TODO: replace with set
    public ClientView(ServerConnection c) {
        this.serverConnection = c;
        c.addListener(new MessageReceiver(this));
        System.out.println("ClientView created!");
    }

    public ModelView getModelView() {
        return modelView;
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
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
            clientView.update(new NicknameSetMessage(inputLine, inputLine));
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
            System.out.println("How many players do you want to play with?");
        }

        @Override
        public void update(SelectPawnRequestMessage selectPawnRequestMessage) {

        }

        @Override
        public void update(CellUpdateMessage cellUpdateMessage) {
            modelView.onCellUpdate(cellUpdateMessage.getPosition(), cellUpdateMessage.getBlockType());
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
        public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
            modelView.onSelectPawnUpdate(selectedPawnUpdateMessage.getWorkerId());
        }

        @Override
        public void update(GameStartMessage gameStartMessage) {
            for(PlayerView p : gameStartMessage.getInGamePlayers()){
                modelView.onPlayerUpdate(p.getName(), p.getPawnList().get(0).getColor(), p.getPawnList().get(0).getId(), p.getPawnList().get(1).getId());
            }
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
        public void update(gameStartedAndYouAreNotSelectedMessage gameStartedAndYouAreNotSelectedMessage) {
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
