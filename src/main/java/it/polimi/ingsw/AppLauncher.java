package it.polimi.ingsw;


import it.polimi.ingsw.client.cli.CLIEngine;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utility.MyLogger;
import it.polimi.ingsw.view.UserInterface;

import java.io.IOException;
import java.util.logging.Level;

public class AppLauncher {
    public static void main(String[] args) {
        int i = 0, j;
        String arg;
        boolean error = false;
        boolean server = false;
        boolean cli = false;
        String hostname = "";
        int port = 0;

        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            if (arg.equals("-server")) {
                server = true;
            } else if (arg.equals("-cli")) {
                cli = true;
            }

            else if (arg.equals("-hostname")) {
                if (i < args.length)
                    hostname = args[i++];
                else {
                    System.err.println("-hostname requires a string");
                    error = true;
                }
            }

            else if (arg.equals("-port")) {
                if (i < args.length) {
                    try {
                        port = Integer.parseInt(args[i++]);
                        if (port < 1 || port > 65535) {
                            System.err.println("-port requires an integer between 1 and 65535");
                            error = true;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("-port requires an integer");
                        error = true;
                    }
                } else {
                    System.err.println("-port requires an integer");
                    error = true;
                }
            }

        }
        if (error) {
            System.err.println("Usage: java -jar santorini.jar [-server|cli] [-hostname hostnameString] [-port portNumber]");
        } else {
            if (server) {
                Server serverApp;
                try {
                    if (port == 0) port = 12345;
                    serverApp = new Server(port);
                    serverApp.run();
                } catch (IOException e) {
                    MyLogger.log(Level.INFO, "AppLauncher", "main()", "Unable to initialize the server " + e.getMessage());
                }
            }

            UserInterface userInterface;

            if (cli) {
                userInterface = new CLIEngine();
                if (hostname != "" && port != 0) {
                    userInterface.quickInitialize(hostname, port);
                } else  if (hostname == "" && port == 0) {
                    userInterface.initialize();
                } else {
                    System.err.println("You must specify both server hostname and port!");
                }
            } else {
                userInterface = new GUIEngine();
                userInterface.initialize();
            }
        }
    }
}
