package it.polimi.ingsw.server;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utility.messages.PingMessage;
import it.polimi.ingsw.utility.messages.PongMessage;
import it.polimi.ingsw.utility.messages.SetMessage;
import it.polimi.ingsw.utility.messages.requests.InGameCardsRequestMessage;
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

public class SocketClientConnection extends SetObservable implements ClientConnection, Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private Server server;

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
            System.err.println(e.getMessage());
        }

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

    private void close() {
        if (active) {
            closeConnection();
            System.out.print("Removing client... ");
            server.removeConnection(this);
            System.out.println("done!");
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
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (pong) {
                        //asyncSend(new PingMessage());
                        pong = false;
                    } else {
                        System.out.println("No pong received!");
                        close();
                        timer.cancel();
                    }
                }
            }, 10000, 10000);

            NicknameSetMessage nicknameSetMessage;
            do {
                NicknameRequestMessage nicknameRequestMessage = new NicknameRequestMessage(new ArrayList<>());
                send(nicknameRequestMessage);
                Object input = in.readObject();
                if (input instanceof NicknameSetMessage) {
                    nicknameSetMessage = (NicknameSetMessage) input;
                } else {
                    throw new IllegalArgumentException();
                }
            } while (!server.addConnection(this, nicknameSetMessage.getName()));

            System.out.println(nicknameSetMessage.getName() + ": nickname set!");
            while(isActive()){
                Object inputObject = in.readObject();
                handleMessage(inputObject);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException!");
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException!");
        } finally {
            close();
        }
    }
}
