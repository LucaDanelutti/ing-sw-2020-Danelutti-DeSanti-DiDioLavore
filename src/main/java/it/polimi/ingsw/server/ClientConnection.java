package it.polimi.ingsw.server;

import it.polimi.ingsw.view.listeners.SetsListener;

public interface ClientConnection {
    void closeConnection();

    void addListener(SetsListener listener);

    void asyncSend(Object message);
}
