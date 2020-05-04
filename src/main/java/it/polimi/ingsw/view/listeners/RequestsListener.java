package it.polimi.ingsw.view.listeners;

import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.utility.messages.requests.*;

public interface RequestsListener extends Listener {
    void update(NicknameRequestMessage nicknameRequestMessage);
    void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage);
    void update(ChosenCardRequestMessage chosenCardRequestMessage);
    void update(ChosenPositionRequestMessage chosenPositionRequestMessage);
    void update(FirstPlayerRequestMessage firstPlayerRequestMessage);
    void update(InGameCardsRequestMessage inGameCardsRequestMessage);
    void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage);
    void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage);
    void update(SelectPawnRequestMessage selectPawnRequestMessage);







    /*public void onChosenCardRequest();
    public void onChosenPositionRequest(ArrayList<Position> availablePositions);
    public void onFirstPlayerRequest();
    public void onInGameCardsRequest();
    public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions);
    public void onNicknameRequest();
    public void onNumberOfPlayersRequest();
    public void onSelectPawnRequest(ArrayList<Position> availablePawns);
    public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes);*/
}
