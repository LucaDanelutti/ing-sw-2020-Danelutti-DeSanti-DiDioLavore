package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenPositionForMoveRequestMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = 8891676114903493827L;

    public ArrayList<Position> getAvailablePositions() {
        return availablePositions;
    }

    ArrayList<Position> availablePositions=new ArrayList<>();

    public ChosenPositionForMoveRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}
