package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;

import java.util.ArrayList;

/**
 * This class is merely used to act as a simulated View for test purpose
 * This class implements the RequestsAndUpdates listener and each time a message arrives from the GameLogicExecutor it stores it in the receivedMessages variable
 */
public class MockView implements RequestsAndUpdateListener {
    private final String name;
    private final ArrayList<RequestAndUpdateMessage> receivedMessages;


                                        //CONSTRUCTOR
    public MockView(String name) {
        this.name=name;
        this.receivedMessages = new ArrayList<>();
    }


                                        //COMMODITY FUNCTIONS
    public boolean isThisMessageNotForMe(RequestAndUpdateMessage receivedMessage){
        for(String name : receivedMessage.getRecipients()){
            if(name.equals(this.name)){
                return false;
            }
        }
        return true;
    }
    public RequestAndUpdateMessage getLastReceivedMessage() {
        return receivedMessages.get(receivedMessages.size()-1);
    }


                                            //GETTERS
    public ArrayList<RequestAndUpdateMessage> getReceivedMessages() {
        return receivedMessages;
    }
    public String getName() {
        return name;
    }



                                        //REQUEST MESSAGES FUNCTIONS
    @Override public void update(NicknameRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(ChosenBlockTypeRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(ChosenCardRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(ChosenPositionForMoveRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(FirstPlayerRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(InGameCardsRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(InitialPawnPositionRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(NumberOfPlayersRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(SelectPawnRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(ChosenPositionForConstructRequestMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }




                                    //UPDATE MESSAGES FUNCTIONS
    @Override public void update(CellUpdateMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(ChosenCardUpdateMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(DoublePawnPositionUpdateMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(PawnPositionUpdateMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(PawnRemoveUpdateMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(SelectedPawnUpdateMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(GameStartMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(TurnEndedMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(YouLostMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(YouLostAndSomeoneWonMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(YouWonMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(GameStartedAndYouAreNotSelectedMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(GameEndedMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
    @Override public void update(UndoUpdateMessage m) {
        if(isThisMessageNotForMe(m)){
            return;
        }
        receivedMessages.add(m);
    }
}
