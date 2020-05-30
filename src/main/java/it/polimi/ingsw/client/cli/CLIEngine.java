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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;

/**
 * This is the CLI engine class that implements the functions called by the clientView to display or request
 */
public class CLIEngine implements UserInterface {
    private ClientView clientView;

                                                //CONSTRUCTORS

    /**
     * This is the default constructor for the CLIEngine
     */
    public CLIEngine() {
    }



                                                //SETUP FUNCTIONS

    /**
     * This function is called to ask the user for information about the server (port and address)
     */
    @Override public void initialize() {
        clientView = new ClientView();
        clientView.setUserInterface(this);

        String serverIP;
        int serverPORT;
        Scanner n= new Scanner(System.in);

        printWelcome();
        System.out.println("SERVER CONNECTION SETUP:");
        System.out.print("Server IP (xxx.xxx.xxx.xxx): ");
        serverIP=n.nextLine();
        System.out.print("Server PORT (0-65535): ");
        serverPORT=n.nextInt();
        System.out.println("CONNECTING...");
        //call function of ClientView
        if (!clientView.startServerConnection(serverIP, serverPORT)) {
            do {
                System.out.println("WRONG SERVER HOSTNAME OR PORT");
                System.out.println("PLEASE TRY AGAIN:");
                n.nextLine();
                System.out.print("Server IP (xxx.xxx.xxx.xxx): ");
                serverIP=n.nextLine();
                System.out.print("Server PORT (0-65535): ");
                serverPORT=n.nextInt();
                System.out.println("CONNECTING...");
            } while (!clientView.startServerConnection(serverIP, serverPORT));
        }
    }
    /**
     * This function is called for a quick start of the connection to the server giving directly the port and the address
     * @param hostname the address of the server
     * @param port the port of the server
     */
    @Override public void quickInitialize(String hostname, int port) {
        clientView = new ClientView();
        clientView.setUserInterface(this);
        clientView.startServerConnection(hostname, port);
    }





                                         //TO BE CALLED WHEN AN UPDATE ARRIVES


    @Override public void onLost(String playerName) {

    }

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
    @Override public void onGameStartedAndYouAreNotSelected() {
        clearScreen();
        printEqualsRow();
        System.out.println("There is already a game started on the server, try later!");
        printEqualsRow();
    }
    public void onServerDisconnection(){
        clearScreen();
        printEqualsRow();
        System.out.println("Server disconnected, please restart the server!");
        printEqualsRow();
    }


    //TO BE CALLED WHEN A REQUEST ARRIVES

    /**
     * This function is called when the user has to select the blockType for a construct action
     * @param availableBlockTypes the available block types to chose from
     */
    @Override public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes) {
        if(availableBlockTypes.size()==0){
            long startingTime=System.currentTimeMillis();
            int secondsPassed;
            int timeToPass=5;
            do{
                secondsPassed=(int)(System.currentTimeMillis()-startingTime)/1000;
                if(secondsPassed<5){
                    System.out.print("The pawn is not able to construct in the selected position -> RESTARTING TURN in "+(timeToPass-secondsPassed)+"s\r");
                }
            }while(secondsPassed<5);
            clientView.update(new UndoTurnSetMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }

        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("Select the block type to construct from the ones below:");
        for(int i=0; i<availableBlockTypes.size(); i++){
            System.out.print(i+") ");
            if(availableBlockTypes.get(i).equals(BlockType.LEVEL1)){
                System.out.println("LEVEL 1");
            }
            else if(availableBlockTypes.get(i).equals(BlockType.LEVEL2)){
                System.out.println("LEVEL 2");
            }
            else if(availableBlockTypes.get(i).equals(BlockType.LEVEL3)){
                System.out.println("LEVEL 3");
            }
            else if(availableBlockTypes.get(i).equals(BlockType.DOME)){
                System.out.println("DOME");
            }
        }
        printSingleScoreRow();
        System.out.print("Choice (0->"+(availableBlockTypes.size()-1)+"): ");

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availableBlockTypes.size(); i++){
            options.add(i);
        }

        int input;
        do {
            String string = scanner.nextLine();
            try {
                input = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                input = -1;
            }
            if(isTheOptionNotValid(options, input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (isTheOptionNotValid(options, input));

        int undoChoice = undoOrConfirmHandler(5);
        if(undoChoice==0){
            clientView.update(new ChosenBlockTypeSetMessage(availableBlockTypes.get(input)));
        }
        else if(undoChoice==1){
            clientView.update(new UndoActionSetMessage());
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
        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        clearScreen();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
        printSingleScoreRow();
        System.out.println("Select your card from the ones below:");
        for(int i=0; i<availableCards.size(); i++){
            System.out.println(String.format("%-5s",i+")")+String.format("%-10s",availableCards.get(i).getName()) + " | "+availableCards.get(i).getDescription());
        }
        printSingleScoreRow();
        System.out.print("Choice (0->"+(availableCards.size()-1)+"): ");

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availableCards.size(); i++){
            options.add(i);
        }

        int input;
        do {
            String string = scanner.nextLine();
            try {
                input = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                input = -1;
            }
            if(isTheOptionNotValid(options, input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (isTheOptionNotValid(options, input));

        clientView.update(new ChosenCardSetMessage(availableCards.get(input).getId()));
    }
    /**
     * This function is called when the user has to select the position for a move action
     * @param availablePositions the positions to chose from
     */
    @Override public void onChosenPositionForMoveRequest(ArrayList<Position> availablePositions) {
        if(availablePositions.size()==0){
            long startingTime=System.currentTimeMillis();
            int secondsPassed;
            int timeToPass=5;
            do{
                secondsPassed=(int)(System.currentTimeMillis()-startingTime)/1000;
                if(secondsPassed<5){
                    System.out.println("No available move positions for the selected pawn -> RESTARTING TURN in "+(timeToPass-secondsPassed)+"s");
                }
            }while(secondsPassed<5);
            clientView.update(new UndoTurnSetMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("MOVE -> Select the position [r,c] from the ones below:");
        printListOfPositions(availablePositions);
        System.out.println();
        printSingleScoreRow();
        System.out.print("Choice (0->"+(availablePositions.size()-1)+"): ");

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availablePositions.size(); i++){
            options.add(i);
        }

        int input;
        do {
            String string = scanner.nextLine();
            try {
                input = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                input = -1;
            }
            if(isTheOptionNotValid(options, input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (isTheOptionNotValid(options, input));

        int undoChoice = undoOrConfirmHandler(5);
        if(undoChoice==0){
            clientView.update(new ChosenPositionSetMessage(availablePositions.get(input)));
        }
        else if(undoChoice==1){
            clientView.update(new UndoActionSetMessage());
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
        if(availablePositions.size()==0){
            long startingTime=System.currentTimeMillis();
            int secondsPassed;
            int timeToPass=5;
            do{
                secondsPassed=(int)(System.currentTimeMillis()-startingTime)/1000;
                if(secondsPassed<5){
                    System.out.print("No available construct positions  for the selected pawn -> RESTARTING TURN in "+(timeToPass-secondsPassed)+"s\r");
                }
            }while(secondsPassed<5);
            clientView.update(new UndoTurnSetMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("CONSTRUCT -> Select the position [r,c] from the ones below:");
        printListOfPositions(availablePositions);
        System.out.println();
        printSingleScoreRow();
        System.out.print("Choice (0->"+(availablePositions.size()-1)+"): ");

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availablePositions.size(); i++){
            options.add(i);
        }

        int input;
        do {
            String string = scanner.nextLine();
            try {
                input = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                input = -1;
            }
            if(isTheOptionNotValid(options, input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (isTheOptionNotValid(options, input));

        clientView.update(new ChosenPositionSetMessage(availablePositions.get(input)));
    }
    /**
     * This function is called to select the first player to start
     */
    @Override public void onFirstPlayerRequest() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        clearScreen();

        printSingleScoreRow();
        System.out.println("Select the first player from the ones below:");
        int i=0;
        for(PlayerView playerView : clientView.getModelView().getPlayerList()){
            System.out.println(i+") "+playerView.getName());
            i++;
        }
        printSingleScoreRow();
        System.out.print("Choice (0->"+(clientView.getModelView().getPlayerList().size()-1)+"): ");

        ArrayList<Integer> options = new ArrayList<>();
        for(int j=0; j<clientView.getModelView().getPlayerList().size(); j++){
            options.add(j);
        }

        int input;
        do {
            String string = scanner.nextLine();
            try {
                input = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                input = -1;
            }
            if(isTheOptionNotValid(options, input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (isTheOptionNotValid(options, input));

        clientView.update(new FirstPlayerSetMessage(clientView.getModelView().getPlayerList().get(input).getName()));
    }
    /**
     * This function is called when the user has to select the cards to for every player
     * @param availableCards the list of all the available cards
     */
    @Override public void onInGameCardsRequest(ArrayList<CardView> availableCards) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("Select "+clientView.getModelView().getPlayerList().size()+" cards from the ones below:");
        for(int i=0; i<availableCards.size(); i++){
            System.out.println(String.format("%-5s",i+")")+String.format("%-10s",availableCards.get(i).getName()) +" | "+availableCards.get(i).getDescription());
        }
        printSingleScoreRow();

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availableCards.size(); i++){
            options.add(i);
        }

        ArrayList<Integer> chosenCards;
        ArrayList<Integer> chosenOptions;
        int input;
        do {
            chosenCards = new ArrayList<>();
            chosenOptions = new ArrayList<>();
            for (int i = 0; i < clientView.getModelView().getPlayerList().size(); i++) {
                System.out.print("Card " + (i + 1) + " of " + clientView.getModelView().getPlayerList().size() + " | Choice (0->" + (availableCards.size() - 1) + "): ");
                String string = scanner.nextLine();
                try {
                    input = Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    input = -i-1;
                }
                chosenOptions.add(input);
            }
            if(areThereAnyDuplicates(chosenOptions)){
                System.out.println("No duplicates allowed, retry: ");
            }
            else if(areTheOptionsNotValid(options, chosenOptions)){
                System.out.println("Not a valid sequence, retry: ");
            }
        }while (areThereAnyDuplicates(chosenOptions)|| areTheOptionsNotValid(options, chosenOptions));
        for (int i : chosenOptions) {
            chosenCards.add(availableCards.get(i).getId());
        }
        clientView.update(new InGameCardsSetMessage(chosenCards));
    }
    /**
     * This functions is called when the user has to chose the initial position for the pawns
     * @param availablePositions the available positions to chose from
     */
    @Override public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        clearScreen();
        printCompleteGameStatus();

        System.out.println("Select the positions [r,c] for your pawns from the ones below:");
        printListOfPositions(availablePositions);
        System.out.println();
        printSingleScoreRow();

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availablePositions.size(); i++){
            options.add(i);
        }

        Position one;
        Position two;
        int choice1;
        int choice2;
        ArrayList<Position> pawnsPositions;
        do {
            pawnsPositions = new ArrayList<>();
            System.out.print("First pawn position choice (0->" + (availablePositions.size() - 1) + "): ");
            String string = scanner.nextLine();
            try {
                choice1 = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                choice1 = -1;
            }
            System.out.print("Second pawn position choice (0->" + (availablePositions.size() - 1) + "): ");
            string = scanner.nextLine();
            try {
                choice2 = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                choice2 = -1;
            }
            if(isTheOptionNotValid(options, choice1) || isTheOptionNotValid(options, choice2) || choice1==choice2){
                System.out.println("Not valid options, retry: ");
            }
        }while(isTheOptionNotValid(options, choice1) || isTheOptionNotValid(options, choice2) || (choice1==choice2));


        one=availablePositions.get(choice1);
        two=availablePositions.get(choice2);

        pawnsPositions.add(one);
        pawnsPositions.add(two);

        ArrayList<Integer> pawnsId=new ArrayList<>();
        for(PlayerView playerView : clientView.getModelView().getPlayerList()){
            if(playerView.getName().equals(clientView.getName())){
                for(PawnView pawnView : playerView.getPawnList()){
                    pawnsId.add(pawnView.getId());
                }
            }
        }

        clientView.update(new InitialPawnPositionSetMessage(pawnsId.get(0),pawnsId.get(1),pawnsPositions.get(0),pawnsPositions.get(1)));
    }
    /**
     * This function is called when the user has to select his nickname
     */
    @Override public void onNicknameRequest() {
        clearScreen();
        printWelcome();
        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        System.out.print("Select your nickname: ");
        String name;
        do{
             name = scanner.nextLine();
             if(name.length()<1){
                 System.out.print("Empty nickname is not allowed, please retry: ");
             }
            else if(name.length()>10){
                System.out.print("Nickname too long (max is 10 characters), please retry: ");
            }
        }while(name.length()<1 || name.length()>10);

        clientView.update(new NicknameSetMessage(name));
        clearScreen();
        printEqualsRow();
        System.out.println("            Waiting for the game to start...");
        printEqualsRow();
    }
    /**
     * This function is called when  the user has to select the number of players in the game
     */
    @Override public void onNumberOfPlayersRequest() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }

        System.out.print("Select the number of players in the game (2 or 3): ");
        int number;
        do {
            String string = scanner.nextLine();
            try {
                number = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if(number!=3 && number!=2){
                System.out.print("Not a valid option, retry: ");
            }
        }while (number!=3 && number!=2);

        clientView.update(new NumberOfPlayersSetMessage(number));
        clearScreen();
        printEqualsRow();
        System.out.println("            Waiting for players to join...");
        printEqualsRow();
    }
    /**
     * This function is called when the user has to select the pawn for the current turn
     * @param availablePositions the positions of his pawns, from where to chose from
     */
    @Override public void onSelectPawnRequest(ArrayList<Position> availablePositions) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException ignored){
        }
        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("Select the pawn from the ones below:");
        ArrayList<PawnView> pawnViews = new ArrayList<>();
        for(PlayerView playerView : clientView.getModelView().getPlayerList()){
            if(playerView.getName().equals(clientView.getName())){
                pawnViews=playerView.getPawnList();
            }
        }

        for(Position position : availablePositions) {
            for (PawnView pawnView : pawnViews) {
                if (pawnView.getPawnPosition().equals(position)) {
                    System.out.println("Pawn: "+pawnView.getId());
                }
            }
        }
        printSingleScoreRow();

        if(pawnViews.size()==2) {
            System.out.print("Choice ("+pawnViews.get(0).getId()+" or "+ pawnViews.get(1).getId() + "): ");
        }
        else if(pawnViews.size()==1){
            System.out.print("Choice ("+pawnViews.get(0).getId()+"): ");
        }

        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for (PawnView view : pawnViews) {
            options.add(view.getId());
        }

        do {
            String string = scanner.nextLine();
            try {
                input = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                input = -1;
            }
            if(isTheOptionNotValid(options, input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (isTheOptionNotValid(options, input));

        Position selectedPawnPosition=null;
        for(PawnView pawnView : pawnViews){
            if(pawnView.getId()==input){
                selectedPawnPosition=pawnView.getPawnPosition();
            }
        }

        clientView.update(new SelectedPawnSetMessage(selectedPawnPosition));
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
    private boolean isTheOptionNotValid(ArrayList<Integer> options, Integer option){
        return !options.contains(option);
    }
    /**
     * This function checks that the options selected are within the options passed
     * @param options the list of available options
     * @param chosenOptions the chosen options
     * @return the result of the operation
     */
    private boolean areTheOptionsNotValid(ArrayList<Integer> options, ArrayList<Integer> chosenOptions){
        for(Integer a : chosenOptions){
            if(!options.contains(a)){
                return true;
            }
        }
        return false;
    }
    /**
     * This function is used to check if there are duplicates in the list of integer passed
     * @param integers the list to be checked
     * @return the result of the operation
     */
    private boolean areThereAnyDuplicates(ArrayList<Integer> integers){
        Set<Integer> a = new HashSet<>(integers);
        return a.size()<integers.size();
    }
    /**
     * This function is used to give the user an option to confirm, undo the action or undo the current turn for X seconds where X is specified by a parameter
     * @param timeToWait the time that the user has to choose (default is CONFIRM)
     * @return the result of the operation
     */
    public int undoOrConfirmHandler(int timeToWait) {
        int input;

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        ConsoleInputReadTask inputHandler = new ConsoleInputReadTask();
        Future<String> future = executorService.submit(inputHandler);
        String in = "0";

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int count = 0;
            private void clearConsole(){
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
            @Override
            public void run() {
                if (count < timeToWait) {
                    clearConsole();
                    System.out.println("Press: 0 -> CONFIRM  |  1 -> UNDO ACTION  |  2 -> UNDO TURN");
                    System.out.println("If no key is pressed, the action will be CONFIRMED  |  # " + (timeToWait-count) + "s remaining #");
                    System.out.print("Choice: ");
                    count++;
                } else {
                    timer.cancel();
                }
            }
        }, 0,  1000);

        try {
            in = future.get(timeToWait, TimeUnit.SECONDS);
        }
        catch (InterruptedException | ExecutionException ignored) {
        } catch (TimeoutException e) {
            future.cancel(true);
            timer.cancel();
        }

        timer.cancel();
        input = Integer.parseInt(in);
        executorService.shutdown();
        return input;
    }
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
     * This function is used to print a list of positions
     * @param arrayList the list to be printed
     */
    public void printListOfPositions(ArrayList<Position> arrayList){
        for(int i=0; i<arrayList.size(); i++){
            if(i%4==0 && i!=0){
                System.out.println();
            }
            if(arrayList.get(i)!=null) {
                System.out.print(String.format("%-2s", i) + "-> " + String.format("%-10s", "[" + arrayList.get(i).getX() + "," + arrayList.get(i).getY() + "]"));
            }
            else{
                System.out.print(String.format("%-2s", i) + "-> " + String.format("%-10s", "SKIP"));

            }

        }
    }
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
        System.out.println("         c:0     c:1     c:2     c:3     c:4   ");
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
                p = isThereAPawnHere(i,j);
                level= matrix[i][j].getPeek().getLevel();
                StringBuilder a = new StringBuilder();
                if(j==0){
                    a.append("r:").append(i).append("  #|");
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
        System.out.println( "========================================================================");
        System.out.println( "  ███████╗ █████╗ ███╗   ██╗████████╗ ██████╗ ██████╗ ██╗███╗   ██╗██╗");
        System.out.println( "  ██╔════╝██╔══██╗████╗  ██║╚══██╔══╝██╔═══██╗██╔══██╗██║████╗  ██║██║");
        System.out.println( "  ███████╗███████║██╔██╗ ██║   ██║   ██║   ██║██████╔╝██║██╔██╗ ██║██║");
        System.out.println( "  ╚════██║██╔══██║██║╚██╗██║   ██║   ██║   ██║██╔══██╗██║██║╚██╗██║██║");
        System.out.println( "  ███████║██║  ██║██║ ╚████║   ██║   ╚██████╔╝██║  ██║██║██║ ╚████║██║");
        System.out.println( "  ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝");
        System.out.println( "========================================================================");
        System.out.println( "");
    }





}


/**
 * This class is used to handle the console input for the "undo" implementation to interrupt the user request for an input after X seconds passed
 */
class ConsoleInputReadTask implements Callable<String> {
    public String call() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String input;
        do {
            try {
                // wait until we have data to complete a readLine()
                while (!br.ready()) {
                    Thread.sleep(200);
                }
                input = br.readLine();
            } catch (InterruptedException e) {
                return "0";
            }
        } while (!(input.equals("0") || input.equals("1") || input.equals("2")));
        return input;
    }
}