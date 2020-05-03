package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class NumberOfPlayersSetMessage extends Message {
    int numberOfPlayers;
    public NumberOfPlayersSetMessage(ArrayList<String> recipients, int numberOfPlayers) {
        super(recipients);
        this.numberOfPlayers=numberOfPlayers;
    }
}
