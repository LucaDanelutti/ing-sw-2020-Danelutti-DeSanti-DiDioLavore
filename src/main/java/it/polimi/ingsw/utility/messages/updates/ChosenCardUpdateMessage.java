package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CardView;

import java.util.ArrayList;

public class ChosenCardUpdateMessage extends Message {
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
}
