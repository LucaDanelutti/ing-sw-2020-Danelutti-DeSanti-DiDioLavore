package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class BOTEngine implements UserInterface {
    private ClientView clientView;

    //CONSTRUCTORS

    /**
     * This is the default constructor for the CLIEngine
     */
    public BOTEngine() {
    }



    //SETUP FUNCTIONS

    /**
     * This function is called to ask the user for information about the server (port and address)
     */
    @Override public void initialize() {
        clientView = new ClientView();
        clientView.setUserInterface(this);
        clearScreen();
        printWelcome();
        System.out.println("SERVER CONNECTION SETUP:");
        Scanner n= new Scanner(System.in);
        System.out.print("Server IP (xxx.xxx.xxx.xxx): ");
        String serverIP=n.nextLine();
        System.out.print("Server PORT (0-65535): ");
        int serverPORT=n.nextInt();
        System.out.println("CONNECTING...");
        //call function of ClientView
        clientView.startServerConnection(serverIP, serverPORT);
    }
    /**
     * This function is called for a quick start of the connection to the server giving directly the port and the address
     * @param hostname the address of the server
     * @param port the port of the server
     */
    @Override public void quickInitialize(String hostname, int port) {
        clientView = new ClientView();
        clientView.setUserInterface(this);
        clearScreen();
        clientView.startServerConnection(hostname, port);
    }





    //TO BE CALLED WHEN AN UPDATE ARRIVES

    /**
     * This function refreshes all the screen with the information about the board and the user table
     */
    @Override public void refreshView() {
        clearScreen();
        if(noWinnerInModel() && haveILost()){
            System.out.println("YOU HAVE LOST, JUST WATCH THE GAME TILL THE END!");
            printSingleScoreRow();
            System.out.println();
        }
        printCompleteGameStatus();
    }
    /**
     * This function refreshes the screen with the information about the users
     */
    @Override public void refreshViewOnlyGameInfo() {
        clearScreen();
        if(noWinnerInModel() && haveILost()){
            System.out.println("YOU HAVE LOST, JUST WATCH THE GAME TILL THE END!");
            printSingleScoreRow();
            System.out.println();
        }
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }
    /**
     * This function is called when the user win the game
     */
    @Override public void onWin() {
        clearScreen();
        printEqualsRow();
        System.out.println("                           YOU WON!");
        printEqualsRow();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }
    /**
     * This function is called when the user lost and we have a winner in the game
     * @param winnerName the winner name
     */
    @Override public void onYouLostAndSomeOneWon(String winnerName) {
        clearScreen();
        printEqualsRow();
        System.out.println("                          YOU LOST!");
        printEqualsRow();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }
    /**
     * This function is called when the game ends for an unexpected reason, for example a user disconnection
     * @param reason the string containing the reason
     */
    @Override public void onGameEnded(String reason) {
        clearScreen();
        System.out.println("GAME ENDED: "+reason);
    }

    @Override
    public void onGameStartedAndYouAreNotSelected() {

    }


    //TO BE CALLED WHEN A REQUEST ARRIVES

    /**
     * This function is called when the user has to select the blockType for a construct action
     * @param availableBlockTypes the available block types to chose from
     */
    @Override public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes) {

        if(availableBlockTypes.size()!=0){
            int randomNum1= ThreadLocalRandom.current().nextInt(0,availableBlockTypes.size());
            clientView.update(new ChosenBlockTypeSetMessage(availableBlockTypes.get(randomNum1)));
        }
        else{
            clientView.update(new UndoTurnSetMessage());
        }
    }
    /**
     * This function is called when the user has to select his card
     * @param availableCards the available cards to chose from
     */
    @Override public void onChosenCardRequest(ArrayList<CardView> availableCards) {
        int randomNum1= ThreadLocalRandom.current().nextInt(0,availableCards.size());
        clientView.update(new ChosenCardSetMessage(availableCards.get(randomNum1).getId()));
    }
    /**
     * This function is called when the user has to select the position for a move action
     * @param availablePositions the positions to chose from
     */
    @Override public void onChosenPositionForMoveRequest(ArrayList<Position> availablePositions) {

        if(availablePositions.size()!=0){
            int randomNum1= ThreadLocalRandom.current().nextInt(0,availablePositions.size());
            clientView.update(new ChosenPositionSetMessage(availablePositions.get(randomNum1)));
        }
        else{
            clientView.update(new UndoTurnSetMessage());
        }

    }
    /**
     * This function is called when the user has to selec the position for a construct action
     * @param availablePositions the positions to chose from
     */
    @Override public void onChosenPositionForConstructRequest(ArrayList<Position> availablePositions) {

        if(availablePositions.size()!=0){
            int randomNum1= ThreadLocalRandom.current().nextInt(0,availablePositions.size());
            clientView.update(new ChosenPositionSetMessage(availablePositions.get(randomNum1)));
        }
        else{
            clientView.update(new UndoTurnSetMessage());
        }

    }
    /**
     * This function is called to select the first player to start
     */
    @Override public void onFirstPlayerRequest() {

        ArrayList<Integer> options = new ArrayList<>();
        for(int j=0; j<clientView.getModelView().getPlayerList().size(); j++){
            options.add(j);
        }

        int randomNum1= ThreadLocalRandom.current().nextInt(0,options.size());

        clientView.update(new FirstPlayerSetMessage(clientView.getModelView().getPlayerList().get(randomNum1).getName()));
    }
    /**
     * This function is called when the user has to select the cards to for every player
     * @param availableCards the list of all the available cards
     */
    @Override public void onInGameCardsRequest(ArrayList<CardView> availableCards) {

        int randomNum1;
        int randomNum2;
        int randomNum3;
        do {
            randomNum1 = ThreadLocalRandom.current().nextInt(0, availableCards.size());
            randomNum2 = ThreadLocalRandom.current().nextInt(0, availableCards.size());
            randomNum3 = ThreadLocalRandom.current().nextInt(0, availableCards.size());
        }while(randomNum1==randomNum2 || randomNum1 == randomNum3 || randomNum2 == randomNum3);

        ArrayList<Integer> chosenCards = new ArrayList<>();

        if(clientView.getModelView().getPlayerList().size()==2){
            chosenCards.add(availableCards.get(randomNum1).getId());
            chosenCards.add(availableCards.get(randomNum2).getId());
        }
        else{
            chosenCards.add(availableCards.get(randomNum1).getId());
            chosenCards.add(availableCards.get(randomNum2).getId());
            chosenCards.add(availableCards.get(randomNum3).getId());
        }
        

        clientView.update(new InGameCardsSetMessage(chosenCards));
    }
    /**
     * This functions is called when the user has to chose the initial position for the pawns
     * @param availablePositions the available positions to chose from
     */
    @Override public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions) {
        int randomNum1;
        int randomNum2;
        do {
            randomNum1 = ThreadLocalRandom.current().nextInt(0, availablePositions.size());
            randomNum2 = ThreadLocalRandom.current().nextInt(0, availablePositions.size());
        }while(randomNum1==randomNum2 );


        Position one=availablePositions.get(randomNum1);
        Position two=availablePositions.get(randomNum2);



        ArrayList<Integer> pawnsId=new ArrayList<>();
        for(PlayerView playerView : clientView.getModelView().getPlayerList()){
            if(playerView.getName().equals(clientView.getName())){
                for(PawnView pawnView : playerView.getPawnList()){
                    pawnsId.add(pawnView.getId());
                }
            }
        }

        clientView.update(new InitialPawnPositionSetMessage(pawnsId.get(0),pawnsId.get(1),one,two));
    }
    /**
     * This function is called when the user has to select his nickname
     */
    @Override public void onNicknameRequest() {
        clearScreen();
        printWelcome();
        byte[] array = new byte[4]; // length is bounded by 4
        new Random().nextBytes(array);
        String generatedString = Integer.toString((int)(Math.random()*(10000000))+10000000);
        String playerName = "Agent" + generatedString;
        clientView.update(new NicknameSetMessage(playerName));
    }
    /**
     * This function is called when  the user has to select the number of players in the game
     */
    @Override public void onNumberOfPlayersRequest() {
        int randomNum1 = ThreadLocalRandom.current().nextInt(2, 4);

        clientView.update(new NumberOfPlayersSetMessage(randomNum1));
    }
    /**
     * This function is called when the user has to select the pawn for the current turn
     * @param availablePositions the positions of his pawns, from where to chose from
     */
    @Override public void onSelectPawnRequest(ArrayList<Position> availablePositions) {
        int randomNum1 = ThreadLocalRandom.current().nextInt(0, availablePositions.size());


        clientView.update(new SelectedPawnSetMessage(availablePositions.get(randomNum1)));
    }






    //COMMODITY FUNCTIONS

    /**
     * This function is used to check if there are any pawns on the board
     * @return the boolean response
     */
    private boolean isThereAnyPawnOnTheBoard(){
        CellView[][] matrix=clientView.getModelView().getMatrix();
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[0].length; j++){
                if(isThereAPawnHere(i,j)!=null){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * This function is used to check if there is a pawn in that specific location
     * @param x the x of the position to be checked
     * @param y the y of the position to be checked
     * @return the result of the operation
     */
    private PawnView isThereAPawnHere(int x, int y){
        for(PlayerView p : clientView.getModelView().getPlayerList()){
            for(PawnView pawnView : p.getPawnList()){
                if(pawnView.getPawnPosition() != null && pawnView.getPawnPosition().getY()==y && pawnView.getPawnPosition().getX()==x){
                    return pawnView;
                }
            }
        }
        return null;
    }
    /**
     * This function checks that the option selected is within the options passed
     * @param options the list of the available options
     * @param option the option chosen
     * @return the result of the operation
     */
    /**
     * This is the getter for the clientView
     * @return the client view
     */
    public ClientView getClientView() {
        return clientView;
    }
    /**
     * This is the setter for the clientView
     * @param clientView the client view to set
     */
    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }
    /**
     * This function checks if there is any winner in the modelView
     * @return the result of the operation
     */
    private boolean noWinnerInModel(){
        for(PlayerView  p : clientView.getModelView().getPlayerList()){
            if(p.getWinner()){
                return false;
            }
        }
        return true;
    }
    /**
     * This function checks if the current clientView has lost
     * @return the result of the operation
     */
    private boolean haveILost(){
        for(PlayerView p : clientView.getModelView().getPlayerList()){
            if(p.getName().equals(clientView.getName())){
                if(p.getLoser()){
                    return true;
                }
            }
        }
        return false;
    }





    //COMMODITY PRINT FUNCTIONS

    /**
     * This function is called to clear the screen
     */
    private void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    /**
     * This function prints the horizontal coordinates
     */
    private void printHorizontalCoordinates(){
        System.out.println("         x:0     x:1     x:2     x:3     x:4   ");
    }
    /**
     * This function prints a row of equals signs
     */
    private void printEqualsRow(){
        System.out.println("==============================================================");

    }
    /**
     * This function prints a row of single score sign
     */
    private void printSingleScoreRow(){
        System.out.println("--------------------------------------------------------------");
    }
    /**
     * This function prints the board
     */
    public void printBoard(){
        CellView[][] matrix=clientView.getModelView().getMatrix();
        PawnView p;
        int level;

        System.out.println();
        for(int i=0; i<matrix.length; i++){

            if(i==0) {
                //if this is the first line we should write the first row
                printHorizontalCoordinates();
                System.out.println("    ===========================================");
            }

            for(int j=0; j<matrix[0].length; j++){
                //let's get the pawn (or null) in the current position to be printed
                p = isThereAPawnHere(j,i);
                level= matrix[j][i].getPeek().getLevel();
                StringBuilder a = new StringBuilder();
                if(j==0){
                    a.append("y:").append(i).append("  #|");
                }
                if(level==4){
                    a.append("   X   |");
                }
                else {
                    if(p==null && level==0){
                        a.append("       |");
                    }
                    else {
                        if (p == null) {
                            a.append("   ");
                        }
                        else {
                            if(level!=0) {
                                a.append(" ").append(p.getId()).append(",");
                            }else{
                                a.append(" ").append(p.getId()).append(" ");
                            }
                        }
                        if (level != 0) {
                            a.append("l:").append(level).append(" |");
                        } else {
                            a.append("    |");
                        }
                    }
                }
                System.out.print(a.toString());


                /*if(p==null) {
                    if(level!=0) {
                        System.out.print(String.format("l:" + level + "   |"));
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
                }*/
                if(j==matrix[0].length-1){
                    System.out.print("#\n");
                }
            }

            //this is the row below the printed things
            System.out.println("    ===========================================");
        }
        System.out.println(" ");
    }
    /**
     * This function prints the player table with corresponding pawns, card and winner status
     */
    private void printPlayersWith_Cards_WinnerStatus_PawnsIds(){
        printEqualsRow();
        int maxNickLength=0;
        for(PlayerView p : clientView.getModelView().getPlayerList()){
            if(p.getName().length()>=maxNickLength){
                maxNickLength=p.getName().length();
            }
        }


        for(PlayerView p : clientView.getModelView().getPlayerList()){
            CardView currentCard =p.getCard();
            String cardName;
            if(currentCard==null){
                cardName="---------";
            }
            else{
                cardName=currentCard.getName();
            }

            StringBuilder a= new StringBuilder();
            String str="|%-15s|";
            a.append(String.format(str," "+p.getName()));

            StringBuilder b = new StringBuilder();
            b.append(" Pawns: ");
            for(PawnView pawnView : p.getPawnList()){
                b.append(pawnView.getId()).append(" ");
            }
            str=String.format("%-15s|",b.toString());
            a.append(str);
            a.append(String.format("%-17s|"," Card: "+cardName));
            if(p.getWinner()){
                a.append(String.format("%-10s|"," Winner"));
            }
            else if(p.getLoser()){
                a.append(String.format("%-10s|"," Loser"));
            }
            else{
                a.append(String.format("%-10s|"," "));
            }

            System.out.println(a.toString());



        }
        printEqualsRow();
    }
    /**
     * This function prints the complete game status
     */
    private void printCompleteGameStatus(){
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
        printBoard();
    }
    /**
     * This function prints the welcome screen for Santorini
     */
    private void printWelcome(){
        System.out.println("==============================================================");
        System.out.println("                         SANTORINI                            ");
        System.out.println("==============================================================");
    }



}