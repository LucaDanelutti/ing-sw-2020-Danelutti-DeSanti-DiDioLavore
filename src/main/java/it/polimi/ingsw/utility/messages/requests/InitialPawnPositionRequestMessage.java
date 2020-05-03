package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CardView;
import javafx.geometry.Pos;

import java.util.ArrayList;

public class InitialPawnPositionRequestMessage extends Message {
    ArrayList<Position> availablePositions=new ArrayList<>();

    public InitialPawnPositionRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }
}
