package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.sets.ChosenCardSetMessage;
import it.polimi.ingsw.view.listeners.SetsListener;

import java.util.ArrayList;

public class SetObservable {
    ArrayList<SetsListener> listeners = new ArrayList<>();

    public void addListener(SetsListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(SetsListener listener){
        this.listeners.remove(listener);
    }

    public void notifyListeners(ChosenCardSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }
}
