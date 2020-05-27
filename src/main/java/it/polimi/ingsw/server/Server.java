package it.polimi.ingsw.server;

import it.polimi.ingsw.utility.MyLogger;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameLogicExecutor;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Server class: main server object. It contains the serverSocket, the controller and the model.
 * There are two maps that contains a reference to the clientConnection linked the player name and to the reference of the player VirtualView
 */
public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<ClientConnection, String> playingConnection = new HashMap<>();
    private Map<ClientConnection, VirtualView> playingVirtualViews = new HashMap<>();
    private GameLogicExecutor gameLogicExecutor;
    private Controller controller;

    /**
     * @param port
     * Default constructor
     * It creates a new server socket
     */
    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * This method creates an empty model and an empty controller
     */
    private void newGame() {
        Game game = new Game();
        gameLogicExecutor = new GameLogicExecutor(game);
        controller = new Controller(gameLogicExecutor);
    }

    /**
     * @param clientConnection
     * This method removes a clientConnection from the server
     * It also removes the player and the player VirtualView from the model
     */
    public synchronized void removeConnection(ClientConnection clientConnection) {
        String name = playingConnection.get(clientConnection);
        gameLogicExecutor.removeListener(playingVirtualViews.get(clientConnection));
        gameLogicExecutor.removePlayer(playingConnection.get(clientConnection));
        playingConnection.remove(clientConnection);
        MyLogger.log(Level.INFO, this.getClass().getName(), "removeConnection()",clientConnection.toString() + ": removed player " + name);
    }

    /**
     * @param clientConnection clientConnection
     * @param name Player name
     * This method adds a clientConnection to the server
     * It checks if a player already exists
     * It creates a new virtualView and sets up the necessary observer/observable associations
     */
    public synchronized boolean addConnection(ClientConnection clientConnection, String name) {
        if (playingConnection.isEmpty()) {
            newGame();
            MyLogger.log(Level.INFO, this.getClass().getName(), "addConnection()","New game created");
        }
        if (playingConnection.containsValue(name)) {
            MyLogger.log(Level.INFO, this.getClass().getName(), "addConnection()",clientConnection.toString() + ": player already exists: " + name);
            return false;
        } else {
            playingConnection.put(clientConnection, name);
            VirtualView playerView = new VirtualView(clientConnection, name);
            playingVirtualViews.put(clientConnection, playerView);
            playerView.addListener(controller);
            gameLogicExecutor.addListener(playerView);
            gameLogicExecutor.addPlayerToLobby(name);
            MyLogger.log(Level.INFO, this.getClass().getName(), "addConnection()",clientConnection.toString() + ": added user " + name);
            return true;
        }
    }

    /**
     * Main method called by ServerApp
     * This method waits for new connections
     */
    public void run(){
        MyLogger.log(Level.INFO, this.getClass().getName(), "run()","Server ready on port " + port);
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                MyLogger.log(Level.INFO, this.getClass().getName(), "run()",socketConnection.toString() + ": new client connected");
                executor.submit(socketConnection);
            } catch (IOException e) {
                MyLogger.log(Level.SEVERE, this.getClass().getName(), "run()","Connection error");
            }
        }
    }
}
