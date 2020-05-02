package it.polimi.ingsw.view.listeners;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;

import java.util.ArrayList;

public interface RequestsListener {
    public void onChosenCardRequest();
    public void onChosenPositionRequest(ArrayList<Position> availablePositions);
    public void onFirstPlayerRequest();
    public void onInGameCardsRequest();
    public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions);
    public void onNicknameRequest();
    public void onNumberOfPlayersRequest();
    public void onSelectPawnRequest(ArrayList<Position> availablePawns);
    public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes);
}
