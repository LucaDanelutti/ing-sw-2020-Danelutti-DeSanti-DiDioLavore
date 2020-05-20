package it.polimi.ingsw.view;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;
import it.polimi.ingsw.view.listeners.SetsListener;

/**
 * VirtualView class: each player has his own virtualView on the server.
 * Every virtualView is observed by the controller and it observes the model for updates and the network handler for sets
 */
public class VirtualView extends SetObservable implements RequestsAndUpdateListener, SetsListener{
    final private ClientConnection clientConnection;
    final private String name;

    /**
     * @param chosenBlockTypeSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {
        chosenBlockTypeSetMessage.setNameOfTheSender(name);
        notifyListeners(chosenBlockTypeSetMessage);
    }

    /**
     * @param chosenCardSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {
        chosenCardSetMessage.setNameOfTheSender(name);
        notifyListeners(chosenCardSetMessage);
    }

    /**
     * @param chosenPositionSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {
        chosenPositionSetMessage.setNameOfTheSender(name);
        notifyListeners(chosenPositionSetMessage);
    }

    /**
     * @param firstPlayerSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {
        firstPlayerSetMessage.setNameOfTheSender(name);
        notifyListeners(firstPlayerSetMessage);
    }

    /**
     * @param inGameCardsSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {
        inGameCardsSetMessage.setNameOfTheSender(name);
        notifyListeners(inGameCardsSetMessage);
    }

    /**
     * @param initialPawnPositionSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {
        initialPawnPositionSetMessage.setNameOfTheSender(name);
        notifyListeners(initialPawnPositionSetMessage);
    }

    /**
     * @param nicknameSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {
        nicknameSetMessage.setNameOfTheSender(name);
        notifyListeners(nicknameSetMessage);
    }

    /**
     * @param numberOfPlayersSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {
        numberOfPlayersSetMessage.setNameOfTheSender(name);
        notifyListeners(numberOfPlayersSetMessage);
    }

    /**
     * @param selectedPawnSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {
        selectedPawnSetMessage.setNameOfTheSender(name);
        notifyListeners(selectedPawnSetMessage);
    }

    /**
     * @param undoTurnSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(UndoTurnSetMessage undoTurnSetMessage) {
        undoTurnSetMessage.setNameOfTheSender(name);
        notifyListeners(undoTurnSetMessage);
    }

    /**
     * @param undoActionSetMessage
     * Update method to observe the network handler. The method sets the name of the sender
     * and it forwards the request to the controller with observable notifyListeners method
     */
    @Override
    public void update(UndoActionSetMessage undoActionSetMessage) {
        undoActionSetMessage.setNameOfTheSender(name);
        notifyListeners(undoActionSetMessage);
    }

    /**
     * @param clientConnection: reference to the network handler object to send data
     * @param name: name of the player linked to this virtualView
     * Default constructor for the VirtualView class
     */
    public VirtualView(ClientConnection clientConnection, String name) {
        this.clientConnection = clientConnection;
        this.name = name;
        clientConnection.addListener(this);
        System.out.println(name + ": virtualView created!");    //TODO: logging
    }

    /**
     * @param nicknameRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(NicknameRequestMessage nicknameRequestMessage) {
        if (nicknameRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(nicknameRequestMessage);
        }
    }

    /**
     * @param chosenBlockTypeRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage) {
        if (chosenBlockTypeRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenBlockTypeRequestMessage);
        }
    }

    /**
     * @param chosenCardRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(ChosenCardRequestMessage chosenCardRequestMessage) {
        if (chosenCardRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenCardRequestMessage);
        }
    }

    /**
     * @param chosenPositionForMoveRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(ChosenPositionForMoveRequestMessage chosenPositionForMoveRequestMessage) {
        if (chosenPositionForMoveRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenPositionForMoveRequestMessage);
        }
    }

    /**
     * @param chosenPositionForConstructRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(ChosenPositionForConstructRequestMessage chosenPositionForConstructRequestMessage) {
        if (chosenPositionForConstructRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenPositionForConstructRequestMessage);
        }
    }

    /**
     * @param firstPlayerRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(FirstPlayerRequestMessage firstPlayerRequestMessage) {
        if (firstPlayerRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(firstPlayerRequestMessage);
        }
    }

    /**
     * @param inGameCardsRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(InGameCardsRequestMessage inGameCardsRequestMessage) {
        if (inGameCardsRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(inGameCardsRequestMessage);
        }
    }

    /**
     * @param initialPawnPositionRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage) {
        if (initialPawnPositionRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(initialPawnPositionRequestMessage);
        }
    }

    /**
     * @param numberOfPlayersRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage) {
        if (numberOfPlayersRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(numberOfPlayersRequestMessage);
        }
    }

    /**
     * @param selectPawnRequestMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(SelectPawnRequestMessage selectPawnRequestMessage) {
        if (selectPawnRequestMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(selectPawnRequestMessage);
        }
    }

    /**
     * @param cellUpdateMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(CellUpdateMessage cellUpdateMessage) {
        if (cellUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(cellUpdateMessage);
        }
    }

    /**
     * @param chosenCardUpdateMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {
        if (chosenCardUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(chosenCardUpdateMessage);
        }
    }

    /**
     * @param doublePawnPositionUpdateMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage) {
        if (doublePawnPositionUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(doublePawnPositionUpdateMessage);
        }
    }

    /**
     * @param pawnPositionUpdateMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage) {
        if (pawnPositionUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(pawnPositionUpdateMessage);
        }
    }

    /**
     * @param pawnRemoveUpdateMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage) {
        if (pawnRemoveUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(pawnRemoveUpdateMessage);
        }
    }

    /**
     * @param selectedPawnUpdateMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
        if (selectedPawnUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(selectedPawnUpdateMessage);
        }
    }

    /**
     * @param gameStartMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(GameStartMessage gameStartMessage) {
        if (gameStartMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(gameStartMessage);
        }
    }

    /**
     * @param gameEndedMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(GameEndedMessage gameEndedMessage) {
        if (gameEndedMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(gameEndedMessage);
        }
    }

    /**
     * @param turnEndedMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(TurnEndedMessage turnEndedMessage) {
        if (turnEndedMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(turnEndedMessage);
        }
    }

    /**
     * @param youLostMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(YouLostMessage youLostMessage) {
        if (youLostMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(youLostMessage);
        }
    }

    /**
     * @param youLostAndSomeoneWonMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(YouLostAndSomeoneWonMessage youLostAndSomeoneWonMessage) {
        if (youLostAndSomeoneWonMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(youLostAndSomeoneWonMessage);
        }
    }

    /**
     * @param youWonMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(YouWonMessage youWonMessage) {
        if (youWonMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(youWonMessage);
        }
    }

    /**
     * @param gameStartedAndYouAreNotSelectedMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(gameStartedAndYouAreNotSelectedMessage gameStartedAndYouAreNotSelectedMessage) {
        if (gameStartedAndYouAreNotSelectedMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(gameStartedAndYouAreNotSelectedMessage);
        }
    }

    /**
     * @param undoUpdateMessage
     * Update method to observe the model. The method checks that the message has to be sent to the
     * player linked to this virtualView and then it forwards the request to the network handler
     */
    @Override
    public void update(UndoUpdateMessage undoUpdateMessage) {
        if (undoUpdateMessage.getRecipients().contains(name)) {
            clientConnection.asyncSend(undoUpdateMessage);
        }
    }
}
