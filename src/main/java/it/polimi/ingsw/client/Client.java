package it.polimi.ingsw.client;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.requests.ChosenCardRequestMessage;
import it.polimi.ingsw.utility.messages.updates.GameStartMessage;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Client extends RequestAndUpdateObservable implements ServerConnection {
    Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream out;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    private boolean active = true;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
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

    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    public void asyncSend(final Object message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        handleMessage(inputObject);
                    }
                } catch (Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    /* DEPRECATED
    public Thread asyncWriteToSocket(final ObjectOutputStream socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {

                    }
                }catch(Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }
    */

    private void handleMessage(Object inputObject) {
        if (inputObject instanceof ChosenCardRequestMessage) {
            ChosenCardRequestMessage message = (ChosenCardRequestMessage) inputObject;
            notifyListeners(message);
        } else if (inputObject instanceof GameStartMessage) {
            GameStartMessage message = (GameStartMessage) inputObject;
            notifyListeners(message);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void run() throws IOException {
        socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            // DEPRECATED Thread t1 = asyncWriteToSocket(out);
            ClientView clientView = new ClientView(this);
            t0.join();
            // DEPRECATED t1.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            socketIn.close();
            out.close();
            socket.close();
        }
    }
}
