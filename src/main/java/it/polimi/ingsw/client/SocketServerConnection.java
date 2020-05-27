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
import java.util.Timer;
import java.util.TimerTask;

/**
 * SocketServerConnection class: it handles the network communications on the client side
 * It notifies the ClientView for RequestAndUpdateMessages arrived from the network connection
 * It handles a timer to detect a dead network connection
 */
public class SocketServerConnection extends RequestAndUpdateObservable implements ServerConnection {
    Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;
    private boolean active = true;

    private int timerFrequency = 10;
    private Timestamp lastPing;

    /**
     * @param ip
     * @param port
     * Default constructor
     */
    public SocketServerConnection(String ip, int port){
        this.ip = ip;
        this.port = port;
        lastPing = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Private utility get method
     */
    private synchronized boolean isActive(){
        return active;
    }

    /**
     * Private utility set method
     */
    private synchronized void setActive(boolean active){
        this.active = active;
    }

    /**
     * This function closes the socket
     */
    @Override
    public synchronized void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    /**
     * Private utility function to send a message over the network
     */
    private synchronized void send(Object message) {
        try {
            socketOut.reset();
            socketOut.writeObject(message);
            socketOut.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    /**
     * @param message
     * This function sends the provided message over the network in a new thread
     */
    public void asyncSend(final Object message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    /**
     * This function launches a new thread that will listen the socket connection for new messages
     */
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

    /**
     * @param inputObject
     * Private utility function to recognise a RequestAndUpdateMessage from a PingMessage
     * To distinguish between RequestAndUpdateMessage types a visitor pattern has been implemented
     */
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

    /**
     * Main connection function
     * It handles the socket connection and the timer initialization
     * Finally it closes the network socket
     */
    public boolean run(ClientView clientView) {
        ObjectInputStream socketIn;
        try {
            socket = new Socket(ip, port);
            System.out.println("Connection established"); //TODO: logging
            socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - lastPing.getTime() > timerFrequency * 1.5 * 1000) {
                                //System.out.println("No ping received!");
                                closeConnection();
                                timer.cancel();
                            }
                        }
                    }, 1000, timerFrequency * 1000);

                    addListener(clientView);
                    Thread t0 = asyncReadFromSocket(socketIn);
                    t0.join();
                } catch(InterruptedException e){
                    System.out.println("Connection closed"); //TODO: logging
                } finally {
                    try {
                        socketIn.close();
                        socketOut.close();
                        socket.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }).start();

        return true;
    }
}
