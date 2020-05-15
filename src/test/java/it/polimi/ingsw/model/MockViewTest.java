package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.ConstructAction;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;
import it.polimi.ingsw.utility.messages.requests.*;
import it.polimi.ingsw.utility.messages.updates.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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

    private void simpleBoardPrintWithoutBlocks(){
        Cell[][] matrix=game.getBoard().getMatrixCopy();
        for(int i=0; i<matrix.length; i++){
            if(i==0){
                System.out.println("_____________________");
            }
            for(int j=0; j<matrix[0].length; j++){
                Pawn p =matrix[i][j].getPawn();
                if(j==0){
                    System.out.print("|");
                }
                if(p==null) {
                    System.out.print("   |");
                }
                else{
                    System.out.print(" "+p.getId()+" |");
                }
                if(j==matrix[0].length-1){
                    System.out.print("\n");
                }
            }
            System.out.println("_____________________");
        }

    }

    private boolean someOneWon(){
        for(MockView mockView : mockViews){
            for(RequestAndUpdateMessage m : mockView.getReceivedMessages()){
                if(m instanceof YouWonMessage){
                    return true;
                }
            }
        }
        return false;
    }

    private void simpleCompleteBoardPrint(){
        Cell[][] matrix=game.getBoard().getMatrixCopy();
        for(int i=0; i<matrix.length; i++){
            if(i==0){
                System.out.println("___________________________________");
            }
            for(int j=0; j<matrix[0].length; j++){
                Pawn p =matrix[i][j].getPawn();
                BlockType blockType = matrix[i][j].peekBlock();
                int level = blockType.getLevel();
                if(j==0){
                    System.out.print("|");
                }
                if(p==null) {
                    if(level!=0) {
                        System.out.print("l:" + level + "   |");
                    }else{
                        System.out.print("     |");
                    }
                }
                else {
                    if(level!=0) {
                        System.out.print("l:" + level + " "+p.getId()+"|");
                    }else{
                        System.out.print("    "+p.getId()+"|");
                    }
                }
                if(j==matrix[0].length-1){
                    System.out.print("\n");
                }
            }
            System.out.println("___________________________________");
        }
        System.out.println(" ");
    }

    private void printCurrentActionInfo(Game currentGame,Position chosen,BlockType blockType){
        String playerName=currentGame.getCurrentPlayer().getName();
        int selectedPawnId=currentGame.getCurrentAction().getSelectedPawn().getId();
        int fromX=currentGame.getCurrentAction().getSelectedPawn().getPosition().getX();
        int fromY=currentGame.getCurrentAction().getSelectedPawn().getPosition().getY();
        int toX;
        int toY;
        if(chosen!=null) {
            toX = chosen.getX();
            toY = chosen.getY();
            if(currentGame.getCurrentAction() instanceof MoveAction){
                System.out.println("Player: "+playerName+" MOVE with: "+selectedPawnId+"from: "+fromX+","+fromY+" to: "+toX+","+toY);
            }
            else if(currentGame.getCurrentAction() instanceof ConstructAction){
                if(((ConstructAction) currentGame.getCurrentAction()).getSelectedBlockType()!=null) {
                    int level=blockType.getLevel();
                    System.out.println("Player: " + playerName + " CONSTRUCT with: " + selectedPawnId +" lvl: "+level+ " from: " + fromX + "," + fromY + " to: " + toX + "," + toY);
                }
            }
        }
        else{
            if(currentGame.getCurrentAction() instanceof MoveAction){
                System.out.println("Player: "+playerName+" MOVE SKIPPED");
            }
            else if(currentGame.getCurrentAction() instanceof ConstructAction){
                System.out.println("Player: " + playerName + " CONSTRUCT SKIPPED");
            }
        }

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

    @Test void ReceiveInitialPawnsPositionsRequest(){
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
        gameLogicExecutor.setChosenCard(1);
        gameLogicExecutor.setChosenCard(2);
        gameLogicExecutor.setChosenCard(3);


        assertTrue(getCurrentPlayerMockView().getLastReceivedMessage() instanceof FirstPlayerRequestMessage);
        gameLogicExecutor.setStartPlayer("p1");

        //at this point "p1" should have received a request for the initial pawn positions containing the allowed positions
        assertTrue(getCurrentPlayerMockView().getLastReceivedMessage() instanceof InitialPawnPositionRequestMessage);


        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        //at this point all the mock views should have received an double pawn position update, and one player should have received an Initial pawn position
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                //the current player should have both the update and the initial pawn position request
                assertTrue(mockView.getLastReceivedMessage() instanceof InitialPawnPositionRequestMessage);
                assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof DoublePawnPositionUpdateMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
            }
        }

        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        //at this point all the mock views should have received an double pawn position update, and one player should have received an Initial pawn position
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                //the current player should have both the update and the initial pawn position request
                assertTrue(mockView.getLastReceivedMessage() instanceof InitialPawnPositionRequestMessage);
                assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof DoublePawnPositionUpdateMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
            }
        }

        //this is the last one who should set the initial pawn positions
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        //at this point all the mock views should have received an double pawn position update, and one player should have received an Initial pawn position
        for(MockView mockView : mockViews){
            if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                //the current player should have both the update and the select pawn message
                assertTrue(mockView.getLastReceivedMessage() instanceof SelectPawnRequestMessage);
                assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof DoublePawnPositionUpdateMessage);
            }
            else{
                assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
            }
        }

        //UNCOMMENT TO SEE THE BOARD SETUP
        //simpleBoardPrint();
    }

    @Test void CompleteTurnOfFirstPlayer(){
        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(3);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards=new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(2);
        inGameCards.add(3);
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(1);
        gameLogicExecutor.setChosenCard(2);
        gameLogicExecutor.setChosenCard(3);

        //SET THE FIRST PLAYER
        gameLogicExecutor.setStartPlayer("p1");

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);

        int maxTurns=10;
        int numberOfTurns=0;

        MockView currentP=getCurrentPlayerMockView();
        SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        randomNum1= ThreadLocalRandom.current().nextInt(0,selectPawnRequestMessage.getAvailablePositions().size());
        gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));
        //At this point we should have a ChosenPositionRequestMessage and SelectPawnUpdateMessage in the current mock and in the other a doublePawnPositionUpdate
           for(MockView mockView : mockViews){
                if(mockView.getName().equals(getCurrentPlayerMockView().getName())){
                    //the current player should have both the update and the select pawn message
                    assertTrue(mockView.getLastReceivedMessage() instanceof ChosenPositionRequestMessage);
                    assertTrue(mockView.getReceivedMessages().get(mockView.getReceivedMessages().size()-2) instanceof SelectedPawnUpdateMessage);
                }
                else{
                    assertTrue(mockView.getLastReceivedMessage() instanceof DoublePawnPositionUpdateMessage);
                }
            }
        while(!(currentP.getLastReceivedMessage() instanceof TurnEndedMessage)){

            if(currentP.getLastReceivedMessage() instanceof ChosenPositionRequestMessage){
                ChosenPositionRequestMessage chosenPositionRequestMessage = (ChosenPositionRequestMessage) currentP.getLastReceivedMessage();
                if(m.getAvailablePositions().size()<1){
                    System.out.println("BOT INCASTRATO player:"+currentP.getName()+"selectedPawn:"+game.getCurrentAction().getSelectedPawn().getId());
                    simpleCompleteBoardPrint();
                    return;
                }
                randomNum1= ThreadLocalRandom.current().nextInt(0,chosenPositionRequestMessage.getAvailablePositions().size());
                gameLogicExecutor.setChosenPosition(chosenPositionRequestMessage.getAvailablePositions().get(randomNum1));
            }
            else if(currentP.getLastReceivedMessage() instanceof ChosenBlockTypeRequestMessage){
                ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage = (ChosenBlockTypeRequestMessage) currentP.getLastReceivedMessage();
                randomNum1= ThreadLocalRandom.current().nextInt(0,chosenBlockTypeRequestMessage.getAvailableBlockTypes().size());
                gameLogicExecutor.setChosenBlockType(chosenBlockTypeRequestMessage.getAvailableBlockTypes().get(randomNum1));
            }

        }


    }

    void CompleteGameWithBots(){
        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(3);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards=new ArrayList<>();
        inGameCards.add(1);
        inGameCards.add(4);
        inGameCards.add(6);
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(1);
        gameLogicExecutor.setChosenCard(4);
        gameLogicExecutor.setChosenCard(6);

        //SET THE FIRST PLAYER
        gameLogicExecutor.setStartPlayer("p1");

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);

        int maxTurns=200;
        int numberOfTurns=0;
        while (!someOneWon() && numberOfTurns!=maxTurns){
            MockView currentP=getCurrentPlayerMockView();
            SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
            randomNum1= ThreadLocalRandom.current().nextInt(0,selectPawnRequestMessage.getAvailablePositions().size());
            gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));

            System.out.println("----------------------------------------------------------------------------");
            System.out.println("---- PLAYER: "+currentP.getName()+" Pawn: "+game.getSelectedPawnCopy().getId()+" GOD: "+game.getPlayer(currentP.getName()).getCurrentCard().getName()+" ----");
            System.out.println("BEFORE TURN:");
            simpleCompleteBoardPrint();

            while(!(currentP.getLastReceivedMessage() instanceof TurnEndedMessage) && !someOneWon()){

                if(currentP.getLastReceivedMessage() instanceof ChosenPositionRequestMessage){

                    ChosenPositionRequestMessage chosenPositionRequestMessage = (ChosenPositionRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionRequestMessage.getAvailablePositions().size()==0){
                        System.out.println("BOT INCASTRATO player:"+currentP.getName()+" Card:"+game.getCurrentPlayer().getCurrentCard().getName()+" selectedPawn:"+game.getCurrentAction().getSelectedPawn().getId());
                        simpleCompleteBoardPrint();
                        return;
                    }
                    randomNum1= ThreadLocalRandom.current().nextInt(0,chosenPositionRequestMessage.getAvailablePositions().size());
                    gameLogicExecutor.setChosenPosition(chosenPositionRequestMessage.getAvailablePositions().get(randomNum1));
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenBlockTypeRequestMessage){
                    ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage = (ChosenBlockTypeRequestMessage) currentP.getLastReceivedMessage();
                    randomNum1= ThreadLocalRandom.current().nextInt(0,chosenBlockTypeRequestMessage.getAvailableBlockTypes().size());
                    gameLogicExecutor.setChosenBlockType(chosenBlockTypeRequestMessage.getAvailableBlockTypes().get(randomNum1));
                }


            }
            numberOfTurns++;
            System.out.println("AFTER TURN");
            simpleCompleteBoardPrint();
            System.out.println("----------------------------------------------------------------------------");

        }
        System.out.println("VINCITAAA");


    }

    private int threeBotsGame(int indexCard1,int indexCard2,int indexCard3, int maxTurns){
        //create the mock views
        mockViews=new ArrayList<>();
        mockViews.add(new MockView("p1"));
        mockViews.add(new MockView("p2"));
        mockViews.add(new MockView("p3"));

        //create the game accordingly adding the mock views
        game= new Game();
        gameLogicExecutor=new GameLogicExecutor(game);

        //LOAD PLAYERS TO THE LOBBY AND ADD THE CORRESPONDING LISTENER (NO REAL VIRTUAL VIEW BUT USING MOCKS)
        gameLogicExecutor.addListener(mockViews.get(0));
        gameLogicExecutor.addPlayerToLobby("p1");
        gameLogicExecutor.addListener(mockViews.get(1));
        gameLogicExecutor.addPlayerToLobby("p2");
        gameLogicExecutor.addListener(mockViews.get(2));
        gameLogicExecutor.addPlayerToLobby("p3");

        //SETUP THE NUMBER OF PLAYERS
        gameLogicExecutor.setNumberOfPlayers(3);

        //CHOOSE IN-GAME CARDS
        ArrayList<Integer> inGameCards=new ArrayList<>();

        inGameCards.add(game.getLoadedCards().get(indexCard1).getId());
        inGameCards.add(game.getLoadedCards().get(indexCard2).getId());
        inGameCards.add(game.getLoadedCards().get(indexCard3).getId());
        //System.out.println("Cards: "+inGameCards.get(0)+", "+inGameCards.get(1)+", "+inGameCards.get(2));
        gameLogicExecutor.setInGameCards(inGameCards);

        //SET THE CARDS FOR EACH PLAYER
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard1).getId());
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard2).getId());
        gameLogicExecutor.setChosenCard(game.getLoadedCards().get(indexCard3).getId());

        //SET THE FIRST PLAYER
        gameLogicExecutor.setStartPlayer("p1");

        //SET THE INITIAL PAWN POSITIONS FOR ALL THE PLAYER (RANDOMLY)
        InitialPawnPositionRequestMessage m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        ArrayList<Position> positions = new ArrayList<>();
        int randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        int randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);
        m = (InitialPawnPositionRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
        positions = new ArrayList<>();
        randomNum1= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        randomNum2= ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        while(randomNum2==randomNum1){
            randomNum2=ThreadLocalRandom.current().nextInt(0,m.getAvailablePositions().size());
        }
        positions.add(m.getAvailablePositions().get(randomNum1));
        positions.add(m.getAvailablePositions().get(randomNum2));
        gameLogicExecutor.setPawnsPositions(positions);

        int numberOfTurns=0;
        while (!someOneWon() && numberOfTurns!=maxTurns){
            MockView currentP=getCurrentPlayerMockView();
            SelectPawnRequestMessage selectPawnRequestMessage = (SelectPawnRequestMessage) getCurrentPlayerMockView().getLastReceivedMessage();
            randomNum1= ThreadLocalRandom.current().nextInt(0,selectPawnRequestMessage.getAvailablePositions().size());
            gameLogicExecutor.setSelectedPawn(selectPawnRequestMessage.getAvailablePositions().get(randomNum1));

            while(!(currentP.getLastReceivedMessage() instanceof TurnEndedMessage) && !someOneWon()){

                if(currentP.getLastReceivedMessage() instanceof ChosenPositionRequestMessage){
                    ChosenPositionRequestMessage chosenPositionRequestMessage = (ChosenPositionRequestMessage) currentP.getLastReceivedMessage();
                    if(chosenPositionRequestMessage.getAvailablePositions().size()==0){
                        return 0;
                    }
                    randomNum1= ThreadLocalRandom.current().nextInt(0,chosenPositionRequestMessage.getAvailablePositions().size());
                    gameLogicExecutor.setChosenPosition(chosenPositionRequestMessage.getAvailablePositions().get(randomNum1));
                    /*if(game.getCurrentPlayer().getCurrentCard().getName().equals("Triton")) {
                        if (chosenPositionRequestMessage.getAvailablePositions().get(randomNum1) == null) {
                            System.out.println("SKIPPO");
                        }
                        else{
                            System.out.println("MI MUOVO A: " + chosenPositionRequestMessage.getAvailablePositions().get(randomNum1).getX() + ", " + chosenPositionRequestMessage.getAvailablePositions().get(randomNum1).getY());
                        }
                    }*/
                }
                else if(currentP.getLastReceivedMessage() instanceof ChosenBlockTypeRequestMessage){
                    ChosenBlockTypeRequestMessage chosenBlockTypeRequestMessage = (ChosenBlockTypeRequestMessage) currentP.getLastReceivedMessage();
                    randomNum1= ThreadLocalRandom.current().nextInt(0,chosenBlockTypeRequestMessage.getAvailableBlockTypes().size());
                    gameLogicExecutor.setChosenBlockType(chosenBlockTypeRequestMessage.getAvailableBlockTypes().get(randomNum1));
                }
            }
            numberOfTurns++;
        }

        if(numberOfTurns==maxTurns){
            return -1;
        }
        else if(someOneWon()){
            return 1;
        }
        else{
            return 2;
        }

    }

    private ArrayList<Integer> randomCards(){
        ArrayList<Integer> cardIds = new ArrayList<>();
        //cardIds.add(11); //WE WANT MEDUSA
        //cardIds.add(12); //WE WANT TRITON
        int randomId;
        while(cardIds.size()<3){
            randomId=ThreadLocalRandom.current().nextInt(0,14);
            if(!cardIds.contains(randomId)){
                cardIds.add(randomId);
            }
        }
        return cardIds;
    }

    @Test void loopBotsGame(){
        //SETUP VARIABLES
        int counter=0;
        int numberOfCardsRandomCombinationSwitch=100;
        int numberOfGamesToRunWithSameCards=100;
        int maxTurns=100;

        //COMMODITY VARIABLES
        int blocked,winner,unknown,maxNumberOfTurns;
        int totalBlocked=0, totalWinner=0,totalUnknown=0,totalMaxNumberOfTurns=0;
        int res,numberOfGamesTriedWithSameCards=0,numberOfCardsRandomCombinationSwitchTried=0;
        ArrayList<Integer> cardIds;

        do {
            blocked=0;
            winner=0;
            unknown=0;
            maxNumberOfTurns=0;
            numberOfGamesTriedWithSameCards=0;

            do {
                cardIds = randomCards();
                res = threeBotsGame(cardIds.get(0), cardIds.get(1), cardIds.get(2), maxTurns);
                numberOfGamesTriedWithSameCards++;
                counter++;
                if (res == 0) {
                    blocked++;
                }
                else if (res == -1) {
                    maxNumberOfTurns++;
                }
                else if (res == 2) {
                    unknown++;
                }
                else if (res == 1) {
                    winner++;
                }
                else{
                    System.out.println("==========================ERROR=================================");
                }
            } while (numberOfGamesTriedWithSameCards < numberOfGamesToRunWithSameCards);

            System.out.println(String.format("%-18s","CARDS: " + cardIds.get(0) + ", " + cardIds.get(1) + ", " + cardIds.get(2)) + " === NUMBER OF GAMES RUNNED: " + numberOfGamesTriedWithSameCards + " RESULTS -> winner:" + winner + " | blocked: " + blocked + " | maxNumberOfTurns: " + maxNumberOfTurns + " | unknown: " + unknown);

            totalBlocked+=blocked;
            totalWinner+=winner;
            totalMaxNumberOfTurns+=maxNumberOfTurns;
            totalUnknown+=unknown;

            numberOfCardsRandomCombinationSwitchTried++;
        }while(numberOfCardsRandomCombinationSwitchTried<numberOfCardsRandomCombinationSwitch);

        System.out.println("-----------------------------------------------------------------------------------------------------");
        int tot=totalBlocked+totalMaxNumberOfTurns+totalUnknown+totalWinner;
        System.out.println("CURRENT SETTINGS -> numberOfPermutations: "+numberOfCardsRandomCombinationSwitch+" numberOfGamesWithThatPermutation: "+numberOfGamesToRunWithSameCards+" maxGameTurns: "+maxTurns);
        System.out.println("TOTAL("+counter+")"+" -> winner: "+totalWinner+ " maxNumberOfTurns: "+totalMaxNumberOfTurns+" blocked:"+totalBlocked+ " unknown: "+totalUnknown);
        System.out.println("-----------------------------------------------------------------------------------------------------");


    }


}