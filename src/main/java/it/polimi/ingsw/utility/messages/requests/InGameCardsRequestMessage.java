package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CardView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class InGameCardsRequestMessage extends Message implements Serializable {
    private static final long serialVersionUID = 8445633687187033739L;

    ArrayList<CardView> availableCards=new ArrayList<>();

    public InGameCardsRequestMessage(ArrayList<String> recipients, ArrayList<CardView> availableCards) {
        super(recipients);
        this.availableCards.addAll(availableCards);
    }
}
