package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class ChosenPositionRequestMessage extends Message {
    ArrayList<Position> availablePositions=new ArrayList<>();

    public ChosenPositionRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }
}
