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
    private ObjectInputStream socketIn;
    private boolean active = true;

    private int timerFrequency = 10;
    private Timestamp lastPing;
    private Timer pingTimer = new Timer();

    /**
     * @param hostname server hostname
     * @param port server port
     * Default constructor
     */
    public SocketServerConnection(String hostname, int port){
        this.ip = hostname;
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
    private synchronized void setActiveToFalse(){
        this.active = false;
    }

    /**
     * This function closes the socket
     */
    @Override
    public synchronized void closeConnection() {
        try {
            socket.close();
            socketIn.close();
            socketOut.close();
            pingTimer.cancel();
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
        new Thread(() -> send(message)).start();
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
            new Thread(() -> message.accept(visitor)).start();
        } else if (inputObject instanceof PingMessage) {
            lastPing = new Timestamp(System.currentTimeMillis());
            asyncSend(new PongMessage());
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Main connection function
     * It handles the socket connection and the timer initialization, it then listens the socket connection for new messages
     * Finally it closes the network socket
     */
    public boolean run(ClientView clientView) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connection established"); //TODO: logging
            socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return false;
        }

        new Thread(() -> {
            try {
                pingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (System.currentTimeMillis() - lastPing.getTime() > timerFrequency * 1.5 * 1000) {
                            System.out.println("No ping received!"); //TODO: logging
                            closeConnection();
                            pingTimer.cancel();
                        }
                    }
                }, 1000, timerFrequency * 1000);

                addListener(clientView);
                while (isActive()) {
                    Object inputObject = socketIn.readObject();
                    handleMessage(inputObject);
                }
            }
            catch (Exception e){
                setActiveToFalse();
            }
            finally {
                closeConnection();
            }
        }).start();
        return true;
    }
}
