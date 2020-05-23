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

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<ClientConnection, String> playingConnection = new HashMap<>();
    private Map<ClientConnection, VirtualView> playingVirtualViews = new HashMap<>();
    private GameLogicExecutor gameLogicExecutor;
    private Controller controller;

    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    private void newGame() {
        Game game = new Game();
        gameLogicExecutor = new GameLogicExecutor(game);
        controller = new Controller(gameLogicExecutor);
    }

    public synchronized void removeConnection(ClientConnection c) {
        String name = playingConnection.get(c);
        gameLogicExecutor.removeListener(playingVirtualViews.get(c));
        gameLogicExecutor.removePlayer(playingConnection.get(c));
        playingConnection.remove(c);
        MyLogger.log(Level.INFO, this.getClass().getName(), "removeConnection()",c.toString() + ": removed player " + name);
    }

    public synchronized boolean addConnection(ClientConnection c, String name) {
        if (playingConnection.isEmpty()) {
            newGame();
            MyLogger.log(Level.INFO, this.getClass().getName(), "addConnection()","New game created");
        }
        if (playingConnection.containsValue(name)) {
            MyLogger.log(Level.INFO, this.getClass().getName(), "addConnection()",c.toString() + ": player already exists: " + name);
            return false;
        } else {
            playingConnection.put(c, name);
            VirtualView playerView = new VirtualView(c, name);
            playingVirtualViews.put(c, playerView);
            playerView.addListener(controller);
            gameLogicExecutor.addListener(playerView);
            gameLogicExecutor.addPlayerToLobby(name);
            MyLogger.log(Level.INFO, this.getClass().getName(), "addConnection()",c.toString() + ": added user " + name);
            return true;
        }
    }

    public void run(){
        MyLogger.log(Level.INFO, this.getClass().getName(), "run()","Server ready");
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
