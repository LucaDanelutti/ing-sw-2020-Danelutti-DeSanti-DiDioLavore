package it.polimi.ingsw.view.listeners;

import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;

public interface RequestsAndUpdateListener{
    void update(NicknameRequestMessage nicknameRequestMessage);
    void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage);
    void update(ChosenCardRequestMessage chosenCardRequestMessage);
    void update(ChosenPositionRequestMessage chosenPositionRequestMessage);
    void update(FirstPlayerRequestMessage firstPlayerRequestMessage);
    void update(InGameCardsRequestMessage inGameCardsRequestMessage);
    void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage);
    void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage);
    void update(SelectPawnRequestMessage selectPawnRequestMessage);
    void update(CellUpdateMessage cellUpdateMessage);
    void update(ChosenCardUpdateMessage chosenCardUpdateMessage);
    void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage);
    void update(PawnPositionUpdateMessage pawnPositionUpdateMessage);
    void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage);
    void update (SelectedPawnUpdateMessage selectedPawnUpdateMessage);


    void update(GameStartMessage gameStartMessage);
    void update(TurnEndedMessage turnEndedMessage);
    void update(YouLostMessage youLostMessage);
    void update(YouLostAndSomeoneWonMessage youLostAndSomeoneWonMessage);
    void update(YouWonMessage youWonMessage);
    void update(gameStartedAndYouAreNotSelectedMessage gameStartedAndYouAreNotSelectedMessage);

    void update(GameEndedMessage gameEndedMessage);

    void update(UndoUpdateMessage m);
}
