package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.SetObservable;
import it.polimi.ingsw.utility.messages.SetMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class InGameCardsSetMessage extends SetMessage implements Serializable {
    private static final long serialVersionUID = 559837490721849033L;

    ArrayList<Integer> cardsId=new ArrayList<>();

    public InGameCardsSetMessage(ArrayList<Integer> cardsId) {
        this.cardsId.addAll(cardsId);
    }

    public ArrayList<Integer> getCardsId() {
        return cardsId;
    }

    public void accept(SetObservable visitor) {
        visitor.notifyListeners(this);
    }
}
