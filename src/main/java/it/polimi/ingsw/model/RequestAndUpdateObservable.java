package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsAndUpdateListener;

import java.util.ArrayList;

public class RequestAndUpdateObservable {
    ArrayList<RequestsAndUpdateListener> listeners = new ArrayList<>();
    public void addListener(RequestsAndUpdateListener listener){
        this.listeners.add(listener);
    }
    public void removeListener(RequestsAndUpdateListener listener){
        this.listeners.remove(listener);
    }


    /* ------------------------------------------------------------------------------------------------------------------------------------ */
    //IMPLEMENTATION OF REQUEST_AND_UPDATE_MESSAGE_VISITOR
    /* ------------------------------------------------------------------------------------------------------------------------------------ */
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
    public void notifyListeners(ChosenPositionRequestMessage m) {
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
    public void notifyListeners(gameStartedAndYouAreNotSelectedMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }

    public void notifyListeners(GameEndedMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }

    public void notifyListeners(UndoUpdateMessage m) {
        synchronized (listeners) {
            for(RequestsAndUpdateListener l : this.listeners){
                l.update(m);
            }
        }
    }

    /* ------------------------------------------------------------------------------------------------------------------------------------ */



}
