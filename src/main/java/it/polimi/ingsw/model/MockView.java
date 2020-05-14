package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;

import java.util.ArrayList;
import java.util.Stack;

public class MockView implements RequestsAndUpdateListener {
    private String name;

    public ArrayList<RequestAndUpdateMessage> getReceivedMessages() {
        return receivedMessages;
    }

    private ArrayList<RequestAndUpdateMessage> receivedMessages;

    public MockView(String name) {
        this.name=name;
        this.receivedMessages = new ArrayList<>();
    }

    public boolean isThisMessageForMe(RequestAndUpdateMessage receivedMessage){
        for(String name : receivedMessage.getRecipients()){
            if(name.equals(this.name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(NicknameRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(ChosenBlockTypeRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(ChosenCardRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(ChosenPositionRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(FirstPlayerRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(InGameCardsRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(InitialPawnPositionRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(NumberOfPlayersRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(SelectPawnRequestMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(CellUpdateMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(ChosenCardUpdateMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(DoublePawnPositionUpdateMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(PawnPositionUpdateMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(PawnRemoveUpdateMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(SelectedPawnUpdateMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(GameStartMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(TurnEndedMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(YouLostMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(YouLostAndSomeoneWonMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(YouWonMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(gameStartedAndYouAreNotSelectedMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    @Override
    public void update(GameEndedMessage m) {
        if(!isThisMessageForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }

    public String getName() {
        return name;
    }

    public RequestAndUpdateMessage getLastReceivedMessage() {
        return receivedMessages.get(receivedMessages.size()-1);
    }
}
