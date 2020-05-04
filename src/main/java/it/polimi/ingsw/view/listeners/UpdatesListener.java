package it.polimi.ingsw.view.listeners;

import it.polimi.ingsw.utility.messages.updates.GameStartMessage;
import it.polimi.ingsw.utility.messages.updates.TurnEndedMessage;
import it.polimi.ingsw.utility.messages.updates.*;

public interface UpdatesListener extends Listener {
    void update(CellUpdateMessage cellUpdateMessage);
    void update(ChosenCardUpdateMessage chosenCardUpdateMessage);
    void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage);
    void update(PawnPositionUpdateMessage pawnPositionUpdateMessage);
    void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage);
    void update(PlayerUpdateMessage playerUpdateMessage);
    void update (SelectedPawnUpdateMessage selectedPawnUpdateMessage);

    //----
    void update(GameStartMessage gameStartMessage);
    void update(TurnEndedMessage turnEndedMessage);
    //TODO: win? lose? here?
    //----
}
