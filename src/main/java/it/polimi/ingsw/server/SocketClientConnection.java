package it.polimi.ingsw.server;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utility.messages.requests.ChosenCardRequestMessage;
import it.polimi.ingsw.utility.messages.sets.ChosenCardSetMessage;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.modelview.CardView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class SocketClientConnection extends SetObservable implements ClientConnection, Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private Server server;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    @Override
    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        //System.out.println("Deregistering client...");
        //server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void asyncSend(final Object message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    private void handleMessage(Object inputObject) {
        if(inputObject instanceof ChosenCardSetMessage) {
            ChosenCardSetMessage message = (ChosenCardSetMessage)inputObject;
            notifyListeners(message);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void run() {
        ObjectInputStream in;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            //name = read;
            //server.lobby(this, name);
            VirtualView player1View = new VirtualView(this);
            Game game = new Game();
            GameLogicExecutor gameLogicExecutor = new GameLogicExecutor(game);
            gameLogicExecutor.addListener(player1View);
            gameLogicExecutor.startGame();
            //TODO
            while(isActive()){
                Object inputObject = in.readObject();
                handleMessage(inputObject);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error!");
        } finally {
            close();
        }
    }
}
