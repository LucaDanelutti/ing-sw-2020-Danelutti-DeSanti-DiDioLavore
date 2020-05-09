package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.util.ArrayList;


public class gameStartedAndYouAreNotSelectedMessage extends RequestAndUpdateMessage {
    private static final long serialVersionUID = -7436329154575487370L;

    public gameStartedAndYouAreNotSelectedMessage(ArrayList<String> recipients) {
        super(recipients);
    }


    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
