package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;

public interface RequestsAndUpdatesMessageVisitor {
    void notifyListeners(ChosenBlockTypeRequestMessage m);
    void notifyListeners(ChosenCardRequestMessage m);
    void notifyListeners(ChosenPositionRequestMessage m);
    void notifyListeners(FirstPlayerRequestMessage m);
    void notifyListeners(InGameCardsRequestMessage m);
    void notifyListeners(InitialPawnPositionRequestMessage m);
    void notifyListeners(NicknameRequestMessage m);
    void notifyListeners(NumberOfPlayersRequestMessage m);
    void notifyListeners(SelectPawnRequestMessage m);


    void notifyListeners(CellUpdateMessage m);
    void notifyListeners(ChosenCardUpdateMessage m);
    void notifyListeners(DoublePawnPositionUpdateMessage m);
    void notifyListeners(PawnRemoveUpdateMessage m);
    void notifyListeners(PawnPositionUpdateMessage m);
    void notifyListeners(PlayerUpdateMessage m);
    void notifyListeners(SelectedPawnUpdateMessage m);


    void notifyListeners(TurnEndedMessage m);
    void notifyListeners(YouLostAndSomeoneWonMessage m);
    void notifyListeners(YouLostMessage m);
    void notifyListeners(YouWonMessage m);
    void notifyListeners(GameStartMessage m);

}
