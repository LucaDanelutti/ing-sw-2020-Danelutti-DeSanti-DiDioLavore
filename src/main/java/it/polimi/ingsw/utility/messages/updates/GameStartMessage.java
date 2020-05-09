package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.view.modelview.PlayerView;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStartMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = 797836970633075160L;


    public GameStartMessage(ArrayList<String> recipients,ArrayList<PlayerView> inGamePlayers) {
        super(recipients);
        this.inGamePlayers= inGamePlayers;
    }


    ArrayList<PlayerView> inGamePlayers;

    public ArrayList<PlayerView> getInGamePlayers() {
        return inGamePlayers;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
