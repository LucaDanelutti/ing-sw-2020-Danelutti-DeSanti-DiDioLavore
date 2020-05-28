package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLIEngine;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.view.UserInterface;

import java.io.IOException;

public class ClientApp
{
    public static void main(String[] args) {

        String selectedUserInterface = args.length > 0 ? args[0] : null;
        String hostname = args.length > 1 ? args[1] : null;
        String port = args.length > 2 ? args[2] : null;

        UserInterface userInterface;

        if(selectedUserInterface==null){
            userInterface = new GUIEngine();
        }
        else if (selectedUserInterface.equals("gui")) {
            userInterface = new GUIEngine();
        } else {
            userInterface = new CLIEngine();
        }

        if (hostname == null && port == null) {
            userInterface.initialize();
        } else {
            userInterface.quickInitialize(hostname, Integer.parseInt(port));
        }
    }
}
