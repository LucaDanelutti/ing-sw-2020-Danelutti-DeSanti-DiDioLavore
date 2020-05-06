package it.polimi.ingsw.server;

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

    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public synchronized void removeConnection(ClientConnection c) {
        playingConnection.remove(c);
    }

    public synchronized boolean addConnection(ClientConnection c, String name) {
        if (playingConnection.containsValue(name)) {
            return false;
        } else {
            playingConnection.put(c, name);
            VirtualView playerView = new VirtualView(c);
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
