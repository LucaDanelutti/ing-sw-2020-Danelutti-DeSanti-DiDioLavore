package it.polimi.ingsw.client;

import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.PingMessage;
import it.polimi.ingsw.utility.messages.PongMessage;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends RequestAndUpdateObservable implements ServerConnection {
    Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream out;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
        lastPing = new Timestamp(System.currentTimeMillis());
    }

    private boolean active = true;

    private Timestamp lastPing;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    @Override
    public synchronized void closeConnection() {
        //send("Connection closed!");
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

    private void handleMessage(Object inputObject) {
        if (inputObject instanceof RequestAndUpdateMessage) {
            RequestAndUpdateMessage message = (RequestAndUpdateMessage) inputObject;
            RequestAndUpdateObservable visitor = this;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    message.accept(visitor);
                }
            }).start();
        } else if (inputObject instanceof PingMessage) {
            lastPing = new Timestamp(System.currentTimeMillis());
            asyncSend(new PongMessage());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void run() throws IOException {
        socket = new Socket(ip, port);
        System.out.println("Connection established"); //TODO: logging
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        try{
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (System.currentTimeMillis() - lastPing.getTime() > 15*1000) {
                        //System.out.println("No ping received!");
                        closeConnection();
                        timer.cancel();
                    }
                }
            }, 1000, 10*1000);

            Thread t0 = asyncReadFromSocket(socketIn);
            ClientView clientView = new ClientView(this);
            t0.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            socketIn.close();
            out.close();
            socket.close();
        }
    }
}
