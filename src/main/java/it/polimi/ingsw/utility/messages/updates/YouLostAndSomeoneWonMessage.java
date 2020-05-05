package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestsAndUpdatesMessageVisitor;
import it.polimi.ingsw.model.SetMessageVisitor;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class YouLostAndSomeoneWonMessage extends Message {
    String winnerName;

    public YouLostAndSomeoneWonMessage(ArrayList<String> recipients, String winnerName) {
        super(recipients);
        this.winnerName = winnerName;
    }

    public String getWinnerName() {
        return winnerName;
    }


}
