package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.Listener;
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
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(ChosenCardRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(ChosenPositionRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(FirstPlayerRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(InGameCardsRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(InitialPawnPositionRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(NicknameRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(NumberOfPlayersRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(SelectPawnRequestMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(CellUpdateMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(ChosenCardUpdateMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(DoublePawnPositionUpdateMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(PawnRemoveUpdateMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(PawnPositionUpdateMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(PlayerUpdateMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(SelectedPawnUpdateMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(TurnEndedMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(YouLostAndSomeoneWonMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(YouLostMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(YouWonMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    public void notifyListeners(GameStartMessage m) {
        for(RequestsAndUpdateListener l : this.listeners){
            l.update(m);
        }
    }
    /* ------------------------------------------------------------------------------------------------------------------------------------ */



}
