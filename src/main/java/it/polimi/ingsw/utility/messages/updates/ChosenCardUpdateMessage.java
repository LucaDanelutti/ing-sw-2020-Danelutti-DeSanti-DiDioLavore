package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CardView;

import java.util.ArrayList;

public class ChosenCardUpdateMessage extends Message {
    CardView chosenCard;

    public ChosenCardUpdateMessage(ArrayList<String> recipients, CardView chosenCard) {
        super(recipients);
        this.chosenCard = chosenCard;
    }
}
