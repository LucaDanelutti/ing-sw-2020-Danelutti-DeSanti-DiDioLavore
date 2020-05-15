package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameLogicExecutor;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<ClientConnection, String> playingConnection = new HashMap<>();
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
        gameLogicExecutor.removePlayer(playingConnection.get(c));
        playingConnection.remove(c);
    }

    public synchronized boolean addConnection(ClientConnection c, String name) {
        if (playingConnection.isEmpty()) {
            newGame();
        }
        if (playingConnection.containsValue(name)) {
            return false;
        } else {
            playingConnection.put(c, name);
            VirtualView playerView = new VirtualView(c, name);
            playerView.addListener(controller);
            gameLogicExecutor.addListener(playerView);
            gameLogicExecutor.addPlayerToLobby(name);
            return true;
        }
    }

    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                System.out.println("New client connected!");
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }
}
