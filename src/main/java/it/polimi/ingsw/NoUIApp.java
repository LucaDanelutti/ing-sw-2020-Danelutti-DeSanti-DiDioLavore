package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;

public class NoUIApp {
    public static void main(String[] args){
        Client client = new Client("127.0.0.1", 12345);
        client.run(new ClientView());
    }
}
