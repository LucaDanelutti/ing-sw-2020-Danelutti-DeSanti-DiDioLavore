package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utility.MyLogger;

import java.io.IOException;
import java.util.logging.Level;

public class ServerApp {
    public static void main( String[] args )
    {
        int port = 12345;
        Server server;
        try {
            server = new Server(port);
            server.run();
        } catch (IOException e) {
            MyLogger.log(Level.INFO, "ServerApp", "main()","Unable to initialize the server " + e.getMessage());
        }
    }
}
