package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.utility.messages.requests.ChosenCardRequestMessage;
import it.polimi.ingsw.utility.messages.requests.FirstPlayerRequestMessage;
import it.polimi.ingsw.utility.messages.requests.InGameCardsRequestMessage;
import it.polimi.ingsw.utility.messages.requests.NumberOfPlayersRequestMessage;
import it.polimi.ingsw.utility.messages.updates.ChosenCardUpdateMessage;
import it.polimi.ingsw.utility.messages.updates.GameStartMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MockViewTest {
    Game game;
    GameLogicExecutor gameLogicExecutor;
    ArrayList<MockView> mockViews;

    private MockView getCurrentPlayerMockView(){
        String currentPlayerName=gameLogicExecutor.getCurrentPlayerName();
        for(MockView mock : mockViews){
            if(mock.getName().equals(currentPlayerName)){
                return mock;
            }
        }
        return null;
    }

    @BeforeEach  void init_of_3_players(){
        //create the mock views
        mockViews=new ArrayList<>();
        mockViews.add(new MockView("p1"));
        mockViews.add(new MockView("p2"));
        mockViews.add(new MockView("p3"));

        //create the game accordingly adding the mock views
        game= new Game();
        gameLogicExecutor=new GameLogicExecutor(game);

    }



    @Test  void receiveGameStartedAndInGameCardsRequest(){
        //the first player is added to the lobby, so he should receive the request for the number of players in the game
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        assertTrue(mockViews.get(0).getLastReceivedMessage() instanceof NumberOfPlayersRequestMessage);
        assertEquals(mockViews.get(0).getName(),mockViews.get(0).getLastReceivedMessage().getRecipients().get(0));

        //other players are added to the lobby, check that their message is set to null
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");
        assertEquals(0,mockViews.get(1).getReceivedMessages().size());
        assertEquals(0,mockViews.get(2).getReceivedMessages().size());

        //we set the number of players to 3 so the game will start immediately
        gameLogicExecutor.setNumberOfPlayers(3);

        //at this point the startGame function is called and one player will receive the message to set the cards
        String currentPlayerName=gameLogicExecutor.getCurrentPlayerName();
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(currentPlayerName)){
                assertTrue(mockView.getLastReceivedMessage() instanceof InGameCardsRequestMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof GameStartMessage);
            }
        }
    }

    @Test void everyOneReceiveChosenCardRequest(){
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");
        gameLogicExecutor.setNumberOfPlayers(3);
        ArrayList<Integer> inGameCards=new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(2);
        inGameCards.add(3);
        gameLogicExecutor.setInGameCards(inGameCards);

        MockView currentMockView=getCurrentPlayerMockView(); //the mockView of the user who set the ChosenCard
        assert currentMockView != null;
        gameLogicExecutor.setChosenCard(1);
        //everyone should have received a cardUpdate
        for(MockView mockView : mockViews){
            if(!mockView.getName().equals(gameLogicExecutor.getCurrentPlayerName())) {
                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) received).getName());
                assertEquals(1, ((ChosenCardUpdateMessage) received).getChosenCard().getId());
            }
            else{
                //if i am the current player mock i have received both the update and the request for my chosen card
                RequestAndUpdateMessage beforeTheLastMessage = mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2);
                assertTrue(beforeTheLastMessage instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) beforeTheLastMessage).getName());
                assertEquals(1, ((ChosenCardUpdateMessage) beforeTheLastMessage).getChosenCard().getId());

                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardRequestMessage);
                assertEquals(mockView.getName(), ((ChosenCardRequestMessage) received).getRecipients().get(0));
                assertEquals(2, ((ChosenCardRequestMessage) received).getAvailableCards().size());
            }
        }


        currentMockView=getCurrentPlayerMockView();
        assert currentMockView != null;
        gameLogicExecutor.setChosenCard(2);
        //everyone should have received a cardUpdate
        for(MockView mockView : mockViews){
            if(!mockView.getName().equals(gameLogicExecutor.getCurrentPlayerName())) {
                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) received).getName());
                assertEquals(2, ((ChosenCardUpdateMessage) received).getChosenCard().getId());
            }
            else{
                //if i am the current player mock i have received both the update and the request for my chosen card
                RequestAndUpdateMessage beforeTheLastMessage = mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2);
                assertTrue(beforeTheLastMessage instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) beforeTheLastMessage).getName());
                assertEquals(2, ((ChosenCardUpdateMessage) beforeTheLastMessage).getChosenCard().getId());

                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardRequestMessage);
                assertEquals(mockView.getName(), ((ChosenCardRequestMessage) received).getRecipients().get(0));
                assertEquals(1, ((ChosenCardRequestMessage) received).getAvailableCards().size());
            }
        }


        currentMockView=getCurrentPlayerMockView();
        assert currentMockView != null;
        gameLogicExecutor.setChosenCard(3);
        //everyone should have received a cardUpdate
        for(MockView mockView : mockViews){
            if(!mockView.getName().equals(gameLogicExecutor.getCurrentPlayerName())) {
                //everyone else a part of the last player to choose the card received only the ChosenCardUpdate
                RequestAndUpdateMessage received = mockView.getLastReceivedMessage();
                assertTrue(received instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) received).getName());
                assertEquals(3, ((ChosenCardUpdateMessage) received).getChosenCard().getId());
            }
            else{
                //if this is the last player to have chosen the card, the message before the lastMessage should be the ChosenCardUpdate
                RequestAndUpdateMessage beforeTheLastMessage = mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2);
                assertTrue(beforeTheLastMessage instanceof ChosenCardUpdateMessage);
                assertEquals(currentMockView.getName(), ((ChosenCardUpdateMessage) beforeTheLastMessage).getName());
                assertEquals(3, ((ChosenCardUpdateMessage) beforeTheLastMessage).getChosenCard().getId());

                //the last message should be FirstPlayerRequestMessage
                RequestAndUpdateMessage lastMessage = mockView.getLastReceivedMessage();
                assertTrue(lastMessage instanceof FirstPlayerRequestMessage);
                assertEquals(currentMockView.getName(), lastMessage.getRecipients().get(0));
            }
        }

    }




}