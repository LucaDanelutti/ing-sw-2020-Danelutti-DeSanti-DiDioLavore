package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.client.cli.CLIEngine;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;
import it.polimi.ingsw.view.listeners.SetsListener;
import it.polimi.ingsw.view.modelview.ModelView;
import it.polimi.ingsw.view.modelview.PlayerView;

import java.io.IOException;

public class ClientView implements SetsListener, RequestsAndUpdateListener {
    private String name;
    private ModelView modelView;
    private UserInterface userInterface;
    private ServerConnection serverConnection;

    public ClientView() {
        this.name = null;
        this.modelView = new ModelView();
        this.userInterface = null;
        this.serverConnection = null;
    }

    public void startServerConnection(String hostname, int port) {
        this.modelView = new ModelView();
        Client client = new Client(hostname, port);
        serverConnection = client;
        try{
            client.run(this);
        }catch (IOException e){
            System.err.println(e.getMessage()); //TODO: logging
        }
    }

    public UserInterface getUserInterface() {
        return userInterface;
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

    @Override
    public void update(NicknameRequestMessage nicknameRequestMessage) {
        //System.out.println("What's your name?"); //TODO: remove
        //Scanner stdin = new Scanner(System.in);
        //String inputLine = stdin.nextLine();
        //this.update(new NicknameSetMessage(inputLine));
        userInterface.onNicknameRequest();
    }

    @Override
    public void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage) {
        userInterface.onChosenBlockTypeRequest(chosenBlockTypeRequestMessage.getAvailableBlockTypes());
    }

    @Override
    public void update(ChosenCardRequestMessage chosenCardRequestMessage) {
        userInterface.onChosenCardRequest(chosenCardRequestMessage.getAvailableCards());
    }

    @Override
    public void update(ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage) {
        userInterface.onChosenPositionForMoveRequest(chosenPositionForMoveRequestMessage.getAvailablePositions());
    }

    @Override
    public void update(ChosenPositionForConstructRequestMessage chosenPositionForConstructRequestMessage) {
        userInterface.onChosenPositionForConstructRequest(chosenPositionForConstructRequestMessage.getAvailablePositions());
    }

    @Override
    public void update(FirstPlayerRequestMessage firstPlayerRequestMessage) {
        userInterface.onFirstPlayerRequest();
    }

    @Override
    public void update(InGameCardsRequestMessage inGameCardsRequestMessage) {
        //System.out.println("inGameCardsRequestMessage message received!"); //TODO: remove
        for(PlayerView p : inGameCardsRequestMessage.getInGamePlayers()){
            modelView.onPlayerUpdate(p.getName(), p.getPawnList().get(0).getColor(), p.getPawnList().get(0).getId(), p.getPawnList().get(1).getId());
        }
        userInterface.onInGameCardsRequest(inGameCardsRequestMessage.getAvailableCards());
    }

    @Override
    public void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage) {
        userInterface.onInitialPawnPositionRequest(initialPawnPositionRequestMessage.getAvailablePositions());
    }

    @Override
    public void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage) {
        //System.out.println("How many players do you want to play with?"); //TODO: remove
        //Scanner stdin = new Scanner(System.in);
        //int inputLine = stdin.nextInt();
        //this.update(new NumberOfPlayersSetMessage(inputLine));
        userInterface.onNumberOfPlayersRequest();
    }

    @Override
    public void update(SelectPawnRequestMessage selectPawnRequestMessage) {
        userInterface.onSelectPawnRequest(selectPawnRequestMessage.getAvailablePositions());
    }

    @Override
    public void update(CellUpdateMessage cellUpdateMessage) {
        modelView.onCellUpdate(cellUpdateMessage.getPosition(), cellUpdateMessage.getBlockType());
        userInterface.refreshView(modelView.getMatrix()[cellUpdateMessage.getPosition().getX()][cellUpdateMessage.getPosition().getY()]);
    }

    @Override
    public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {
        modelView.onChosenCardUpdate(chosenCardUpdateMessage.getChosenCard(), chosenCardUpdateMessage.getName());
        userInterface.refreshView(chosenCardUpdateMessage.getChosenCard());
    }

    @Override
    public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage) {
        modelView.onDoublePawnPositionUpdate(doublePawnPositionUpdateMessage.getWorkerId1(), doublePawnPositionUpdateMessage.getWorkerPos1(), doublePawnPositionUpdateMessage.getWorkerId2(), doublePawnPositionUpdateMessage.getWorkerPos2());
        userInterface.refreshView(modelView.getPawn(doublePawnPositionUpdateMessage.getWorkerId1()));
        userInterface.refreshView(modelView.getPawn(doublePawnPositionUpdateMessage.getWorkerId2()));
    }

    @Override
    public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage) {
        modelView.onPawnPositionUpdate(pawnPositionUpdateMessage.getWorkerId(), pawnPositionUpdateMessage.getWorkerPos());
        userInterface.refreshView(modelView.getPawn(pawnPositionUpdateMessage.getWorkerId()));
    }

    @Override
    public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage) {
        modelView.onPawnRemoved(pawnRemoveUpdateMessage.getWorkerId());
        userInterface.refreshView();
    }

    @Override
    public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
        modelView.onSelectPawnUpdate(selectedPawnUpdateMessage.getWorkerId());
        userInterface.refreshView(modelView.getPawn(selectedPawnUpdateMessage.getWorkerId()));
    }

    @Override
    public void update(GameStartMessage gameStartMessage) {
        for(PlayerView p : gameStartMessage.getInGamePlayers()){
            modelView.onPlayerUpdate(p.getName(), p.getPawnList().get(0).getColor(), p.getPawnList().get(0).getId(), p.getPawnList().get(1).getId());
        }
        userInterface.refreshView();
    }

    @Override
    public void update(TurnEndedMessage turnEndedMessage) {
        userInterface.refreshView();
    }

    @Override
    public void update(YouLostMessage youLostMessage) {
        modelView.setLooser(name);
        userInterface.refreshView();
    }

    @Override
    public void update(YouWonMessage youWonMessage) {
        modelView.setWinner(name);
        userInterface.onWin();
    }

    @Override
    public void update(gameStartedAndYouAreNotSelectedMessage gameStartedAndYouAreNotSelectedMessage) {
        //TODO
    }

    @Override
    public void update(GameEndedMessage gameEndedMessage) {
        userInterface.onGameEnded("A player disconnected!");
    }

    @Override
    public void update(UndoUpdateMessage undoUpdateMessage) {
        modelView = undoUpdateMessage.getRestoredModelView();
        userInterface.refreshView();
    }

    @Override
    public void update(YouLostAndSomeoneWonMessage youLostAndSomeoneWonMessage) {
        modelView.setWinner(youLostAndSomeoneWonMessage.getWinnerName());
        modelView.setLooser(name);
        userInterface.onYouLostAndSomeOneWon(youLostAndSomeoneWonMessage.getWinnerName());
    }

    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {
        serverConnection.asyncSend(chosenBlockTypeSetMessage);
    }

    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        serverConnection.asyncSend(chosenCardSetMessage);
    }

    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {
        serverConnection.asyncSend(chosenPositionSetMessage);
    }

    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {
        serverConnection.asyncSend(firstPlayerSetMessage);
    }

    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {
        serverConnection.asyncSend(inGameCardsSetMessage);
    }

    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {
        serverConnection.asyncSend(initialPawnPositionSetMessage);
    }

    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {
        this.setName(nicknameSetMessage.getName());
        serverConnection.asyncSend(nicknameSetMessage);
    }

    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {
        serverConnection.asyncSend(numberOfPlayersSetMessage);
    }

    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {
        serverConnection.asyncSend(selectedPawnSetMessage);
    }

    @Override
    public void update(UndoTurnSetMessage undoTurnSetMessage) {
        serverConnection.asyncSend(undoTurnSetMessage);
    }

    @Override
    public void update(UndoActionSetMessage undoActionSetMessage) {
        serverConnection.asyncSend(undoActionSetMessage);
    }
}
