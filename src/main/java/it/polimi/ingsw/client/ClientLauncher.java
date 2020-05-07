package it.polimi.ingsw.client;

import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.view.UserInterface;

public class ClientLauncher {

    public static void main(String[] args) {

        String selectedUserInterface = args.length > 0 ? args[0] : "";

        UserInterface userInterface;
        if (selectedUserInterface.equals("gui")) {
            userInterface = new GUIEngine();
        } else {
            //TODO: replace with CLI counterpart
            userInterface = new GUIEngine();
        }

        userInterface.initialize();
    }
}
