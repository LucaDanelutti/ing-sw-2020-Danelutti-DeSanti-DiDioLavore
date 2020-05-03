package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class InGameCardsSetMessage extends Message {
    ArrayList<Integer> cardsId=new ArrayList<>();
    public InGameCardsSetMessage(ArrayList<String> recipients, ArrayList<Integer> cardsId) {
        super(recipients);
        this.cardsId.addAll(cardsId);
    }
}
