package it.polimi.ingsw.model;


import it.polimi.ingsw.utility.messages.sets.*;

public interface SetMessageVisitor {
    void notifyListener(ChosenBlockTypeSetMessage m);
    void notifyListener(ChosenCardSetMessage m);
    void notifyListener(ChosenPositionSetMessage m);
    void notifyListener(FirstPlayerSetMessage m);
    void notifyListener(InGameCardsSetMessage m);
    void notifyListener(InitialPawnPositionSetMessage m);
    void notifyListener(NicknameSetMessage m);
    void notifyListener(NumberOfPlayersSetMessage m);
    void notifyListener(SelectedPawnSetMessage m);
}
