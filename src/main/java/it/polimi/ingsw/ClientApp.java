package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLIEngine;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.view.UserInterface;

import java.io.IOException;

public class ClientApp
{
    public static void main(String[] args) {

        String selectedUserInterface = args.length > 0 ? args[0] : "";

        UserInterface userInterface;
        if (selectedUserInterface.equals("gui")) {
            userInterface = new GUIEngine();
        } else {
            userInterface = new CLIEngine();
        }

        userInterface.initialize();
    }
}
