package it.polimi.ingsw.view;

import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;

public interface UserInterface {

    /**
     * Initializes the selected user interface
     */
    void initialize();
    /**
     * Refreshes the user interface after an update within the ModelView instance.
     * @param pawnView is the updated object.
     */
    void refreshView(PawnView pawnView);

    /**
     * Refreshes the user interface after an update within the ModelView instance.
     * @param cardView is the updated object.
     */
    void refreshView(CardView cardView);

    /**
     * Refreshes the user interface after an update within the ModelView instance.
     * @param playerView is the updated object.
     */
    void refreshView(PlayerView playerView);

    /**
     * Refreshes the user interface after an update within the ModelView instance.
     * @param cellView is the updated object.
     */
    void refreshView(CellView cellView);

}
