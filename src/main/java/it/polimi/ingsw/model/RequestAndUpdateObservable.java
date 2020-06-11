package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;

import java.util.ArrayList;

/**
 * This class handles sending out the messages from the model its listeners
 */
public class RequestAndUpdateObservable {
    final ArrayList<RequestsAndUpdateListener> listeners = new ArrayList<>();
    public void addListener(RequestsAndUpdateListener listener){
        this.listeners.add(listener);
    }
    public void removeListener(RequestsAndUpdateListener listener){
        this.listeners.remove(listener);
    }



                                    //NOTIFY FOR REQUEST MESSAGES

    public void notifyListeners(ChosenBlockTypeRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(ChosenCardRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(ChosenPositionForMoveRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(FirstPlayerRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(InGameCardsRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(InitialPawnPositionRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(NicknameRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(NumberOfPlayersRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(SelectPawnRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(ChosenPositionForConstructRequestMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }



                                    //NOTIFY FOR UPDATE MESSAGES

    public void notifyListeners(CellUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(ChosenCardUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(DoublePawnPositionUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(PawnRemoveUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(PawnPositionUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(SelectedPawnUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(TurnEndedMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(YouLostAndSomeoneWonMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(YouLostMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(YouWonMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(GameStartMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(GameStartedAndYouAreNotSelectedMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }
    public void notifyListeners(GameEndedMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(UndoUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }


}
