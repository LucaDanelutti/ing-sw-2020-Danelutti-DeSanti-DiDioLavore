package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.BOTEngine;
import it.polimi.ingsw.view.UserInterface;

public class BotClientApp {
    public static void main(String[] args) {

        String selectedUserInterface = args.length > 0 ? args[0] : null;
        String hostname = args.length > 1 ? args[1] : null;
        String port = args.length > 2 ? args[2] : null;

        UserInterface userInterface = new BOTEngine();

        if (hostname == null && port == null) {
            userInterface.initialize();
        } else {
            userInterface.quickInitialize(hostname, Integer.parseInt(port));
        }
    }
}
