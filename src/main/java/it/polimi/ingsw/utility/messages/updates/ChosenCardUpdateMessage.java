package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.view.modelview.CardView;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenCardUpdateMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -2565099215092280831L;

    CardView chosenCard;
    String name;

    public ChosenCardUpdateMessage(ArrayList<String> recipients, CardView chosenCard, String name) {
        super(recipients);
        this.chosenCard = chosenCard;
        this.name = name;
    }

    public CardView getChosenCard() {
        return chosenCard;
    }

    public String getName() {
        return name;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
