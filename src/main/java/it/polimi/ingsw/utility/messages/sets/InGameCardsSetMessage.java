package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class InGameCardsSetMessage extends Message implements Serializable {
    private static final long serialVersionUID = 559837490721849033L;

    ArrayList<Integer> cardsId=new ArrayList<>();

    public InGameCardsSetMessage(ArrayList<String> recipients, ArrayList<Integer> cardsId) {
        super(recipients);
        this.cardsId.addAll(cardsId);
    }
}
