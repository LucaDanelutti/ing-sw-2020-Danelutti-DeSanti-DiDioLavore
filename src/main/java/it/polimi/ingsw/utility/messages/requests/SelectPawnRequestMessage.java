package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectPawnRequestMessage extends Message implements Serializable {
    private static final long serialVersionUID = 2417769558217583111L;

    ArrayList<Position> availablePositions=new ArrayList<>();

    public SelectPawnRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }
}
