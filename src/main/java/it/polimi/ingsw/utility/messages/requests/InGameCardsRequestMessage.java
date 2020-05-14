package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.PlayerView;

import java.io.Serializable;
import java.util.ArrayList;

public class InGameCardsRequestMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = 8445633687187033739L;

    ArrayList<CardView> availableCards=new ArrayList<>();

    ArrayList<PlayerView> inGamePlayers;

    public ArrayList<PlayerView> getInGamePlayers() {
        return inGamePlayers;
    }

    public InGameCardsRequestMessage(ArrayList<String> recipients, ArrayList<PlayerView> inGamePlayers,ArrayList<CardView> availableCards) {
        super(recipients);
        this.inGamePlayers=inGamePlayers;
        this.availableCards.addAll(availableCards);
    }

    public ArrayList<CardView> getAvailableCards() {
        return availableCards;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
