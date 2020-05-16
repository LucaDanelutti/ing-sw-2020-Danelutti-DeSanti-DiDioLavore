package it.polimi.ingsw.view.listeners;

import it.polimi.ingsw.utility.messages.sets.*;

public interface SetsListener {
    void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage);
    void update(ChosenCardSetMessage chosenCardSetMessage);
    void update(ChosenPositionSetMessage chosenPositionSetMessage);
    void update(FirstPlayerSetMessage firstPlayerSetMessage);
    void update(InGameCardsSetMessage inGameCardsSetMessage);
    void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage);
    void update(NicknameSetMessage nicknameSetMessage);
    void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage);
    void update(SelectedPawnSetMessage selectedPawnSetMessage);

    void update(UndoTurnSetMessage message);

    void update(UndoActionSetMessage message);
}
