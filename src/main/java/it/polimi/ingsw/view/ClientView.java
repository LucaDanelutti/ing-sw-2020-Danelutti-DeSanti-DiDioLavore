package it.polimi.ingsw.view;

import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.utility.messages.updates.*;
import it.polimi.ingsw.view.listeners.RequestsListener;
import it.polimi.ingsw.view.listeners.SetsListener;
import it.polimi.ingsw.view.listeners.UpdatesListener;
import it.polimi.ingsw.view.modelview.ModelView;

public class ClientView implements RequestsListener, SetsListener, UpdatesListener {
    private String name;
    ModelView modelView;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void update(NicknameRequestMessage nicknameRequestMessage) {

    }

    @Override
    public void update(ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage) {

    }

    @Override
    public void update(ChosenCardRequestMessage chosenCardRequestMessage) {

    }

    @Override
    public void update(ChosenPositionRequestMessage chosenPositionRequestMessage) {

    }

    @Override
    public void update(FirstPlayerRequestMessage firstPlayerRequestMessage) {

    }

    @Override
    public void update(InGameCardsRequestMessage inGameCardsRequestMessage) {

    }

    @Override
    public void update(InitialPawnPositionRequestMessage initialPawnPositionRequestMessage) {

    }

    @Override
    public void update(NumberOfPlayersRequestMessage numberOfPlayersRequestMessage) {

    }

    @Override
    public void update(SelectPawnRequestMessage selectPawnRequestMessage) {

    }

    @Override
    public void update(ChosenBlockTypeSetMessage chosenBlockTypeSetMessage) {

    }

    @Override
    public void update(ChosenCardSetMessage chosenCardSetMessage) {

    }

    @Override
    public void update(ChosenPositionSetMessage chosenPositionSetMessage) {

    }

    @Override
    public void update(FirstPlayerSetMessage firstPlayerSetMessage) {

    }

    @Override
    public void update(InGameCardsSetMessage inGameCardsSetMessage) {

    }

    @Override
    public void update(InitialPawnPositionSetMessage initialPawnPositionSetMessage) {

    }

    @Override
    public void update(NicknameSetMessage nicknameSetMessage) {

    }

    @Override
    public void update(NumberOfPlayersSetMessage numberOfPlayersSetMessage) {

    }

    @Override
    public void update(SelectedPawnSetMessage selectedPawnSetMessage) {

    }

    @Override
    public void update(CellUpdateMessage cellUpdateMessage) {
        modelView.onCellUpdate(cellUpdateMessage.getPosition(), cellUpdateMessage.getCell());
    }

    @Override
    public void update(ChosenCardUpdateMessage chosenCardUpdateMessage) {
        modelView.onChosenCardUpdate(chosenCardUpdateMessage.getChosenCard(), chosenCardUpdateMessage.getName());
    }

    @Override
    public void update(DoublePawnPositionUpdateMessage doublePawnPositionUpdateMessage) {
        modelView.onDoublePawnPositionUpdate(doublePawnPositionUpdateMessage.getWorkerId1(), doublePawnPositionUpdateMessage.getWorkerPos1(), doublePawnPositionUpdateMessage.getWorkerId2(), doublePawnPositionUpdateMessage.getWorkerPos2());
    }

    @Override
    public void update(PawnPositionUpdateMessage pawnPositionUpdateMessage) {
        modelView.onPawnPositionUpdate(pawnPositionUpdateMessage.getWorkerId(), pawnPositionUpdateMessage.getWorkerPos());
    }

    @Override
    public void update(PawnRemoveUpdateMessage pawnRemoveUpdateMessage) {
        modelView.onPawnRemoved(pawnRemoveUpdateMessage.getWorkerId());
    }

    @Override
    public void update(PlayerUpdateMessage playerUpdateMessage) {
        modelView.onPlayerUpdate(playerUpdateMessage.getName(), playerUpdateMessage.getColor(), playerUpdateMessage.getWorkerId1(), playerUpdateMessage.getWorkerId2());
    }

    @Override
    public void update(SelectedPawnUpdateMessage selectedPawnUpdateMessage) {
        modelView.onSelectPawnUpdate(selectedPawnUpdateMessage.getWorkerId());
    }

    @Override
    public void update(GameStartMessage gameStartMessage) {

    }

    @Override
    public void update(TurnEndedMessage turnEndedMessage) {

    }

    //TODO: find out whether this method can be removed
    @Override
    public void update(Object o) {

    }
}
