package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class SelectPawnRequestMessage extends Message {
    ArrayList<Position> availablePositions=new ArrayList<>();

    public SelectPawnRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }
}
