package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class YouLostAndSomeoneWonMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -2617379979906241243L;

    String winnerName;

    public YouLostAndSomeoneWonMessage(ArrayList<String> recipients, String winnerName) {
        super(recipients);
        this.winnerName = winnerName;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
