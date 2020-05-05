package it.polimi.ingsw.client;

import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;

public interface ServerConnection {
    void closeConnection();

    void addListener(RequestsAndUpdateListener listener);

    void asyncSend(Object message);
}
