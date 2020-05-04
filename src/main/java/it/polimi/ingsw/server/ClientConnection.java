package it.polimi.ingsw.server;

import it.polimi.ingsw.view.listeners.Listener;

public interface ClientConnection {
    void closeConnection();

    void addListener(Listener listener);

    void asyncSend(Object message);
}
