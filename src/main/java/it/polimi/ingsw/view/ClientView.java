package it.polimi.ingsw.view;

import it.polimi.ingsw.client.SocketServerConnection;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;
import it.polimi.ingsw.view.listeners.SetsListener;
import it.polimi.ingsw.view.modelview.ModelView;
import it.polimi.ingsw.view.modelview.PlayerView;

/**
 * ClientView class: main client object. It contains the modelView that has the actual data,
 * the userInterface and the reference to the network handler.
 * It observes the network handler for requests and updates and the userInterface for sets.
 */
public class ClientView implements SetsListener, RequestsAndUpdateListener {
    private String name;
    private ModelView modelView;
    private UserInterface userInterface;
    private ServerConnection serverConnection;

    /**
     * Default constructor
     * It creates an empty modelView
     */
    public ClientView() {
        this.name = null;
        this.modelView = new ModelView();
        this.userInterface = null;
        this.serverConnection = null;
    }

    /**
     * @param hostname server hostname
     * @param port server port
     * This method starts the connection to the server
     */
    public boolean startServerConnection(String hostname, int port) {
        if (port < 1 || port > 65535) return false;
        SocketServerConnection socketServerConnection = new SocketServerConnection(hostname, port);
        serverConnection = socketServerConnection;
        return socketServerConnection.run(this);
    }

    public void stopServerConnection() {
        serverConnection.closeConnection();
    }

    /**
     * @return userInterface
     * Default get method for userInterface
     */
    public UserInterface getUserInterface() {
        return userInterface;
    }

    /**
     * @return modelView
     * Default get method for modelView
     */
    public ModelView getModelView() {
        return modelView;
    }

    /**
     * @param userInterface
     * Default set method for userInterface
     */
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    /**
     * @return name
     * Default get method for name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     * Default set method for name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param nicknameRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(NicknameRequestMessage nicknameRequestMessage) {
        userInterface.onNicknameRequest();
    }

    /**
     * @param chosenBlockTypeRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage) {
        userInterface.onChosenBlockTypeRequest(chosenBlockTypeRequestMessage.getAvailableBlockTypes());
    }

    /**
     * @param chosenCardRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(ChosenCardRequestMessage chosenCardRequestMessage) {
        userInterface.onChosenCardRequest(chosenCardRequestMessage.getAvailableCards());
    }

    /**
     * @param chosenPositionForMoveRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage) {
        userInterface.onChosenPositionForMoveRequest(chosenPositionForMoveRequestMessage.getAvailablePositions());
    }

    /**
     * @param chosenPositionForConstructRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(ChosenPositionForConstructRequestMessage chosenPositionForConstructRequestMessage) {
        userInterface.onChosenPositionForConstructRequest(chosenPositionForConstructRequestMessage.getAvailablePositions());
    }

    /**
     * @param firstPlayerRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(FirstPlayerRequestMessage firstPlayerRequestMessage) {
        userInterface.onFirstPlayerRequest();
    }

    /**
     * @param inGameCardsRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(InGameCardsRequestMessage inGameCardsRequestMessage) {
        for(PlayerView p : inGameCardsRequestMessage.getInGamePlayers()){
            modelView.onPlayerUpdate(p.getName(), p.getPawnList().get(0).getColor(), p.getPawnList().get(0).getId(), p.getPawnList().get(1).getId());
        }
        userInterface.onInGameCardsRequest(inGameCardsRequestMessage.getAvailableCards());
    }

    /**
     * @param initialPawnPositionRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage) {
        userInterface.onInitialPawnPositionRequest(initialPawnPositionRequestMessage.getAvailablePositions());
    }

    /**
     * @param numberOfPlayersRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage) {
        userInterface.onNumberOfPlayersRequest();
    }

    /**
     * @param selectPawnRequestMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(SelectPawnRequestMessage selectPawnRequestMessage) {
        userInterface.onSelectPawnRequest(selectPawnRequestMessage.getAvailablePositions());
    }

    /**
     * @param cellUpdateMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(CellUpdateMessage cellUpdateMessage) {
        modelView.onCellUpdate(cellUpdateMessage.getPosition(), cellUpdateMessage.getBlockType());
        userInterface.refreshView();
    }

    /**
     * @param chosenCardUpdateMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {
        modelView.onChosenCardUpdate(chosenCardUpdateMessage.getChosenCard(), chosenCardUpdateMessage.getName());
        userInterface.refreshViewOnlyGameInfo();
    }

    /**
     * @param doublePawnPositionUpdateMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage) {
        modelView.onDoublePawnPositionUpdate(doublePawnPositionUpdateMessage.getWorkerId1(), doublePawnPositionUpdateMessage.getWorkerPos1(), doublePawnPositionUpdateMessage.getWorkerId2(), doublePawnPositionUpdateMessage.getWorkerPos2());
        userInterface.refreshView();
    }

    /**
     * @param pawnPositionUpdateMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage) {
        modelView.onPawnPositionUpdate(pawnPositionUpdateMessage.getWorkerId(), pawnPositionUpdateMessage.getWorkerPos());
        userInterface.refreshView();
    }

    /**
     * @param pawnRemoveUpdateMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage) {
        modelView.onPawnRemoved(pawnRemoveUpdateMessage.getWorkerId());
        userInterface.refreshView();
    }

    /**
     * @param selectedPawnUpdateMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
        modelView.onSelectPawnUpdate(selectedPawnUpdateMessage.getWorkerId());
        userInterface.refreshView();
    }

    /**
     * @param gameStartMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(GameStartMessage gameStartMessage) {
        for(PlayerView p : gameStartMessage.getInGamePlayers()){
            modelView.onPlayerUpdate(p.getName(), p.getPawnList().get(0).getColor(), p.getPawnList().get(0).getId(), p.getPawnList().get(1).getId());
        }
        userInterface.refreshViewOnlyGameInfo();
    }

    /**
     * @param turnEndedMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(TurnEndedMessage turnEndedMessage) {
        userInterface.refreshView();
    }

    /**
     * @param youLostMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(YouLostMessage youLostMessage) {
        modelView.setLooser(youLostMessage.getLoserName());
        userInterface.refreshView();
        userInterface.onLost(youLostMessage.getLoserName());
    }

    /**
     * @param youWonMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(YouWonMessage youWonMessage) {
        modelView.setWinner(name);
        userInterface.onWin();
        serverConnection.closeConnection();
    }

    @Override
    public void update(GameStartedAndYouAreNotSelectedMessage gameStartedAndYouAreNotSelectedMessage) {
        userInterface.onGameStartedAndYouAreNotSelected();
        serverConnection.closeConnection();
    }

    /**
     * @param gameEndedMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     */
    @Override
    public void update(GameEndedMessage gameEndedMessage) {
        userInterface.onGameEnded("A player disconnected!");
        serverConnection.closeConnection();
    }

    /**
     * @param undoUpdateMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(UndoUpdateMessage undoUpdateMessage) {
        modelView = undoUpdateMessage.getRestoredModelView();
        userInterface.refreshView();
    }

    /**
     * @param youLostAndSomeoneWonMessage
     * Update method to observe the network handler. It forwards the request to the userInterface
     * and updates the modelView with the received data
     */
    @Override
    public void update(YouLostAndSomeoneWonMessage youLostAndSomeoneWonMessage) {
        modelView.setWinner(youLostAndSomeoneWonMessage.getWinnerName());
        userInterface.onYouLostAndSomeOneWon(youLostAndSomeoneWonMessage.getWinnerName());
        serverConnection.closeConnection();
    }

    /**
     * @param chosenBlockTypeSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {
        serverConnection.asyncSend(chosenBlockTypeSetMessage);
    }

    /**
     * @param chosenCardSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        serverConnection.asyncSend(chosenCardSetMessage);
    }

    /**
     * @param chosenPositionSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {
        serverConnection.asyncSend(chosenPositionSetMessage);
    }

    /**
     * @param firstPlayerSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {
        serverConnection.asyncSend(firstPlayerSetMessage);
    }

    /**
     * @param inGameCardsSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {
        serverConnection.asyncSend(inGameCardsSetMessage);
    }

    /**
     * @param initialPawnPositionSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {
        serverConnection.asyncSend(initialPawnPositionSetMessage);
    }

    /**
     * @param nicknameSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     * and sets the clientView name
     */
    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {
        this.setName(nicknameSetMessage.getName());
        serverConnection.asyncSend(nicknameSetMessage);
    }

    /**
     * @param numberOfPlayersSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {
        serverConnection.asyncSend(numberOfPlayersSetMessage);
    }

    /**
     * @param selectedPawnSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {
        serverConnection.asyncSend(selectedPawnSetMessage);
    }

    /**
     * @param undoTurnSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(UndoTurnSetMessage undoTurnSetMessage) {
        serverConnection.asyncSend(undoTurnSetMessage);
    }

    /**
     * @param undoActionSetMessage
     * Update method to observe the userInterface. It forwards the request to the network handler
     */
    @Override
    public void update(UndoActionSetMessage undoActionSetMessage) {
        serverConnection.asyncSend(undoActionSetMessage);
    }
}
