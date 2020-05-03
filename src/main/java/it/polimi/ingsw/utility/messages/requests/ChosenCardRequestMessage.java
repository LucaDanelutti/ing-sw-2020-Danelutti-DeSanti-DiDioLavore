package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CardView;

import java.util.ArrayList;

public class ChosenCardRequestMessage extends Message {
    ArrayList<CardView> availableCards=new ArrayList<>();

    public ChosenCardRequestMessage(ArrayList<String> recipients, ArrayList<CardView> availableCards) {
        super(recipients);
        this.availableCards.addAll(availableCards);
    }
}
