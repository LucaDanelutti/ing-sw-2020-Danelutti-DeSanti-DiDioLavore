package it.polimi.ingsw.server;

import it.polimi.ingsw.view.listeners.SetsListener;

/**
 * ClientConnection interface implemented by SocketClientConnection
 */
public interface ClientConnection {
    /**
     * This method closes the connection to the client
     */
    void close();

    /**
     * Observable method
     */
    void addListener(SetsListener listener);

    /**
     * @param message
     * This method sends the provided message through the network
     */
    void asyncSend(Object message);
}
