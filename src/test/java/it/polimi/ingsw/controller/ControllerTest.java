package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ModelInterface;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.sets.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    MockGameLogic mockGameLogic;
    Controller controller;

    @BeforeEach
    void init() {
        Game game = new Game();
        mockGameLogic = new MockGameLogic(game, "Player1");
        ModelInterface modelInterface = mockGameLogic;
        controller = new Controller(modelInterface);
    }

    @Test
    void updateChosenBlockTypeSetMessage() {
        ChosenBlockTypeSetMessage chosenBlockTypeSetMessage = new ChosenBlockTypeSetMessage(BlockType.LEVEL1);
        chosenBlockTypeSetMessage.setNameOfTheSender("Player2");
        controller.update(chosenBlockTypeSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        chosenBlockTypeSetMessage.setNameOfTheSender("Player1");
        controller.update(chosenBlockTypeSetMessage);
        assertEquals("setChosenBlockType", mockGameLogic.getLastCall());
    }

    @Test
    void updateChosenCardSetMessage() {
        ChosenCardSetMessage chosenCardSetMessage = new ChosenCardSetMessage(1);
        chosenCardSetMessage.setNameOfTheSender("Player2");
        controller.update(chosenCardSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        chosenCardSetMessage.setNameOfTheSender("Player1");
        controller.update(chosenCardSetMessage);
        assertEquals("setChosenCard", mockGameLogic.getLastCall());
    }

    @Test
    void updateChosenPositionSetMessage() {
        ChosenPositionSetMessage chosenPositionSetMessage = new ChosenPositionSetMessage(new Position(1,1));
        chosenPositionSetMessage.setNameOfTheSender("Player2");
        controller.update(chosenPositionSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        chosenPositionSetMessage.setNameOfTheSender("Player1");
        controller.update(chosenPositionSetMessage);
        assertEquals("setChosenPosition", mockGameLogic.getLastCall());
    }

    @Test
    void updateFirstPlayerSetMessage() {
        FirstPlayerSetMessage firstPlayerSetMessage = new FirstPlayerSetMessage("Player3");
        firstPlayerSetMessage.setNameOfTheSender("Player2");
        controller.update(firstPlayerSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        firstPlayerSetMessage.setNameOfTheSender("Player1");
        controller.update(firstPlayerSetMessage);
        assertEquals("setStartPlayer", mockGameLogic.getLastCall());
    }

    @Test
    void updateInGameCardsSetMessage() {
        InGameCardsSetMessage inGameCardsSetMessage = new InGameCardsSetMessage(new ArrayList<>());
        inGameCardsSetMessage.setNameOfTheSender("Player2");
        controller.update(inGameCardsSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        inGameCardsSetMessage.setNameOfTheSender("Player1");
        controller.update(inGameCardsSetMessage);
        assertEquals("setInGameCards", mockGameLogic.getLastCall());
    }

    @Test
    void updateInitialPawnPositionSetMessage() {
        InitialPawnPositionSetMessage initialPawnPositionSetMessage = new InitialPawnPositionSetMessage(0, 1, new Position(1,1), new Position(2,2));
        initialPawnPositionSetMessage.setNameOfTheSender("Player2");
        controller.update(initialPawnPositionSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        initialPawnPositionSetMessage.setNameOfTheSender("Player1");
        controller.update(initialPawnPositionSetMessage);
        assertEquals("setPawnsPositions", mockGameLogic.getLastCall());
    }

    @Test
    void updateNumberOfPlayersSetMessage() {
        NumberOfPlayersSetMessage numberOfPlayersSetMessage = new NumberOfPlayersSetMessage(3);
        numberOfPlayersSetMessage.setNameOfTheSender("Player1");
        controller.update(numberOfPlayersSetMessage);
        assertEquals("setNumberOfPlayers", mockGameLogic.getLastCall());
    }

    @Test
    void updateSelectedPawnSetMessage() {
        SelectedPawnSetMessage selectedPawnSetMessage = new SelectedPawnSetMessage(new Position(1,1));
        selectedPawnSetMessage.setNameOfTheSender("Player2");
        controller.update(selectedPawnSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        selectedPawnSetMessage.setNameOfTheSender("Player1");
        controller.update(selectedPawnSetMessage);
        assertEquals("setSelectedPawn", mockGameLogic.getLastCall());
    }

    @Test
    void updateUndoTurnSetMessage() {
        UndoTurnSetMessage undoTurnSetMessage = new UndoTurnSetMessage();
        undoTurnSetMessage.setNameOfTheSender("Player2");
        controller.update(undoTurnSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        undoTurnSetMessage.setNameOfTheSender("Player1");
        controller.update(undoTurnSetMessage);
        assertEquals("undoTurn", mockGameLogic.getLastCall());
    }

    @Test
    void updateUndoActionSetMessage() {
        UndoActionSetMessage undoActionSetMessage = new UndoActionSetMessage();
        undoActionSetMessage.setNameOfTheSender("Player2");
        controller.update(undoActionSetMessage);
        assertEquals("", mockGameLogic.getLastCall());
        undoActionSetMessage.setNameOfTheSender("Player1");
        controller.update(undoActionSetMessage);
        assertEquals("undoCurrentAction", mockGameLogic.getLastCall());
    }
}