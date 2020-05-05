package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.modelview.CardView;
import javafx.geometry.Pos;

import java.io.Serializable;
import java.util.ArrayList;

public class InitialPawnPositionRequestMessage extends Message implements Serializable {
    private static final long serialVersionUID = -3993116837959045757L;

    ArrayList<Position> availablePositions=new ArrayList<>();

    public InitialPawnPositionRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }
}
