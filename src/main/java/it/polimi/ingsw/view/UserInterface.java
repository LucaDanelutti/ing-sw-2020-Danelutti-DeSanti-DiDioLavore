package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;

import java.util.ArrayList;

public interface UserInterface {

    /**
     * Initializes the selected user interface
     */
    void initialize();
    void quickInitialize(String hostname, int port);


    /**
     * Refreshes the user interface after an update within the ModelView instance.
     */
    void refreshView();

    void refreshViewOnlyGameInfo();

    void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes);
    void onChosenCardRequest(ArrayList<CardView> availableCards);
    void onChosenPositionForMoveRequest(ArrayList<Position> availablePositions);
    void onChosenPositionForConstructRequest(ArrayList<Position> availablePositions);
    void onFirstPlayerRequest();
    void onInGameCardsRequest(ArrayList<CardView> availableCards);
    void onInitialPawnPositionRequest(ArrayList<Position> availablePositions);
    void onNicknameRequest();
    void onNumberOfPlayersRequest();
    void onSelectPawnRequest(ArrayList<Position> availablePositions);
    void onWin();
    void onYouLostAndSomeOneWon(String winnerName);
    void onGameEnded(String reason);
    void onGameStartedAndYouAreNotSelected();
    void onLost(String playerName);
}
