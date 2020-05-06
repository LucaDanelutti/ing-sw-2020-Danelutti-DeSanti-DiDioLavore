package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class NumberOfPlayersSetMessage extends SetMessage implements Serializable {
    private static final long serialVersionUID = 4608885219135594928L;

    int numberOfPlayers;

    public NumberOfPlayersSetMessage(ArrayList<String> recipients, int numberOfPlayers) {
        super(recipients);
        this.numberOfPlayers=numberOfPlayers;
    }

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
