package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenPositionRequestMessage extends Message implements Serializable {
    private static final long serialVersionUID = 8891676114903493827L;

    ArrayList<Position> availablePositions=new ArrayList<>();

    public ChosenPositionRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }
}
