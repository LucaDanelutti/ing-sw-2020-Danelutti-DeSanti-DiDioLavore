package it.polimi.ingsw.model;

import it.polimi.ingsw.view.listeners.Listener;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public abstract class Observable{
    ArrayList<Listener> listeners = new ArrayList<>();
    
    public void addListener(Listener listener){
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener){
        this.listeners.remove(listener);
    }

    public void notifyListeners(Message message){
        for(Listener l : this.listeners){
            l.update(message);
        }
    }

}
