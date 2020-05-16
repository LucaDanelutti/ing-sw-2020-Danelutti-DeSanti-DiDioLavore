package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.sets.*;
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

    public void notifyListeners(ChosenBlockTypeSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(ChosenCardSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(ChosenPositionSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(FirstPlayerSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(InGameCardsSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(InitialPawnPositionSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(NicknameSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(NumberOfPlayersSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(SelectedPawnSetMessage message){
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(UndoTurnSetMessage message) {
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }

    public void notifyListeners(UndoActionSetMessage message) {
        for(SetsListener l : this.listeners){
            l.update(message);
        }
    }
}
