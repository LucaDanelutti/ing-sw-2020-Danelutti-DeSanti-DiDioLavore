package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.view.modelview.CardView;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenCardRequestMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -4196349432013669336L;

    ArrayList<CardView> availableCards=new ArrayList<>();

    public ChosenCardRequestMessage(ArrayList<String> recipients, ArrayList<CardView> availableCards) {
        super(recipients);
        this.availableCards.addAll(availableCards);
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
