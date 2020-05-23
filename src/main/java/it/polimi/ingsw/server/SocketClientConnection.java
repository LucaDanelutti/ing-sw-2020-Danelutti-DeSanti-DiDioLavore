package it.polimi.ingsw.server;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utility.MyLogger;
import it.polimi.ingsw.utility.messages.PingMessage;
import it.polimi.ingsw.utility.messages.PongMessage;
import it.polimi.ingsw.utility.messages.SetMessage;
import it.polimi.ingsw.utility.messages.requests.NicknameRequestMessage;
import it.polimi.ingsw.utility.messages.sets.NicknameSetMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class SocketClientConnection extends SetObservable implements ClientConnection, Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private Server server;

    private int timerFrequency = 10;
    private boolean pong = true;

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
            MyLogger.log(Level.WARNING, this.getClass().getName(), "send()",message.toString() + ": error sending message");
        }

    }

    @Override
    public synchronized void closeConnection() {
        //send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            MyLogger.log(Level.WARNING, this.getClass().getName(), "closeConnection()",this.toString() + ": error closing socket");
        }
        active = false;
    }

    private void close() {
        if (active) {
            closeConnection();
            server.removeConnection(this);
        }
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
        if (inputObject instanceof SetMessage) {
            SetMessage message = (SetMessage) inputObject;
            message.accept(this);
        } else if (inputObject instanceof PongMessage) {
            pong = true;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void run() {
        ObjectInputStream in;
        Timer pingTimer = new Timer();
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            pingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (pong) {
                        asyncSend(new PingMessage());
                        pong = false;
                    } else {
                        System.out.println("No pong received!");
                        close();
                        pingTimer.cancel();
                    }
                }
            }, timerFrequency * 1000, timerFrequency * 1000);
            MyLogger.log(Level.INFO, this.getClass().getName(), "run()",this.toString() + ": timer set");
            NicknameSetMessage nicknameSetMessage;
            do {
                NicknameRequestMessage nicknameRequestMessage = new NicknameRequestMessage(new ArrayList<>());
                MyLogger.log(Level.INFO, this.getClass().getName(), "run()",this.toString() + ": NicknameRequestMessage sent");
                send(nicknameRequestMessage);
                Object input;
                do {
                    input = in.readObject();
                    if (input instanceof PongMessage) {
                        pong = true;
                    }
                } while (input instanceof PongMessage);
                if (input instanceof NicknameSetMessage) {
                    nicknameSetMessage = (NicknameSetMessage) input;
                    MyLogger.log(Level.INFO, this.getClass().getName(), "run()",this.toString() + ": NicknameSetMessage received");
                } else {
                    throw new IllegalArgumentException();
                }
            } while (!server.addConnection(this, nicknameSetMessage.getName()));
            while(isActive()){
                Object inputObject = in.readObject();
                handleMessage(inputObject);
            }
        } catch (IOException e) {
            MyLogger.log(Level.WARNING, this.getClass().getName(), "run()",this.toString() + ": IOException " + e.getMessage());
        } catch (ClassNotFoundException e) {
            MyLogger.log(Level.WARNING, this.getClass().getName(), "run()",this.toString() + ": ClassNotFoundException, the server received an unknown sequence of bytes!");
        } catch (IllegalArgumentException e) {
            MyLogger.log(Level.WARNING, this.getClass().getName(), "run()",this.toString() + ": IllegalArgumentException, the server received an unknown message!");
        } catch (Exception e) {
            MyLogger.log(Level.WARNING, this.getClass().getName(), "run()",this.toString() + ": general server Exception!");
        } finally {
            pingTimer.cancel();
            MyLogger.log(Level.INFO, this.getClass().getName(), "run()",this.toString() + ": timer cancelled");
            close();
            MyLogger.log(Level.INFO, this.getClass().getName(), "run()",this.toString() + ": connection closed");
        }
    }
}
