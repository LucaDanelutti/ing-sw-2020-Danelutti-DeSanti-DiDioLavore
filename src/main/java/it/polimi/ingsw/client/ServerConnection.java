package it.polimi.ingsw.client;

/**
 * ServerConnection interface implemented by SocketServerConnection
 */
public interface ServerConnection {
    /**
     * This method closes the connection to the server
     */
    void closeConnection();

    /**
     * @param message
     * This method sends the provided message through the network
     */
    void asyncSend(Object message);
}
