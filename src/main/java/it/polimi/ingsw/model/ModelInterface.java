package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.BlockType;

import java.util.ArrayList;

public interface ModelInterface {
    String getCurrentPlayerName();

    Boolean setChosenBlockType(BlockType blockType);

    Boolean setChosenCard(int cardID);

    Boolean setChosenPosition(Position chosenPos);

    Boolean setStartPlayer(String player);

    Boolean setInGameCards(ArrayList<Integer> cards);

    Boolean setPawnsPositions(int idWorker1, Position workerPos1, int idWorker2, Position workerPos2);

    Boolean setNumberOfPlayers(int numberOfPlayers);

    Boolean setSelectedPawn(Position selectedPawnPosition);

    Boolean undoTurn();

    Boolean undoCurrentAction();
}
