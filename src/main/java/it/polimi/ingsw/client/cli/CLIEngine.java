package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.requests.ChosenPositionForMoveRequestMessage;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;

public class CLIEngine implements UserInterface {
    private ClientView clientView;

                                                //CONSTRUCTORS

    /**
     * This is the default constructor for the CLIEngine
     */
    public CLIEngine() {
    }

    /**
     * This is the parametric constructor for the CLIEngine
     * @param clientView the clientView to be assigned
     */
    public CLIEngine(ClientView clientView) {
        this.clientView = clientView;
    }




                                                //SETUP FUNCTIONS

    /**
     * This function is called to ask the user for information about the server (port and address)
     */
    @Override public void initialize() {
        clientView = new ClientView();
        clientView.setUserInterface(this);
        printWelcome();
        System.out.println("SERVER CONNECTION SETUP:");
        Scanner n= new Scanner(System.in);
        System.out.print("Server IP (xxx.xxx.xxx.xxx): ");
        String serverIP=n.nextLine();
        System.out.print("Server PORT (0-65535): ");
        Integer serverPORT=n.nextInt();
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
        clientView.startServerConnection(hostname, port);
    }



                                         //TO BE CALLED WHEN AN UPDATE ARRIVES

    /**
     * This function refreshes all the screen with the information about the board and the user table
     */
    @Override public void refreshView() {
        clearScreen();
        if(!isThereAnyWinner() && haveILost()){
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
        if(!isThereAnyWinner() && haveILost()){
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



                                        //TO BE CALLED WHEN A REQUEST ARRIVES

    /**
     * This function is called when the user has to select the blockType for a construct action
     * @param availableBlockTypes the available block types to chose from
     */
    @Override public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes) {
        Scanner scanner = new Scanner(System.in);

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
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));

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

        clearScreen();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
        printSingleScoreRow();
        System.out.println("Select your card from the ones below:");
        for(int i=0; i<availableCards.size(); i++){
            System.out.println(String.format("%-4s",i+")")+String.format("%-10s",availableCards.get(i).getName()) + " | "+availableCards.get(i).getDescription());
        }
        printSingleScoreRow();
        System.out.print("Choice (0->"+(availableCards.size()-1)+"): ");

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availableCards.size(); i++){
            options.add(i);
        }

        int input;
        do {
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));

        clientView.update(new ChosenCardSetMessage(availableCards.get(input).getId()));
    }

    /**
     * This function is called when the user has to select the position for a move action
     * @param availablePositions the positions to chose from
     */
    @Override public void onChosenPositionForMoveRequest(ArrayList<Position> availablePositions) {
        Scanner scanner = new Scanner(System.in);

        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("MOVE -> Select the position [x,y] from the ones below:");
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
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));

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
        Scanner scanner = new Scanner(System.in);

        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("CONSTRUCT -> Select the position [x,y] from the ones below:");
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
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));

        clientView.update(new ChosenPositionSetMessage(availablePositions.get(input)));
    }

    /**
     * This function is called to select the first player to start
     */
    @Override public void onFirstPlayerRequest() {
        Scanner scanner = new Scanner(System.in);

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
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));

        clientView.update(new FirstPlayerSetMessage(clientView.getModelView().getPlayerList().get(input).getName()));
    }

    /**
     * This function is called when the user has to select the cards to for every player
     * @param availableCards the list of all the available cards
     */
    @Override public void onInGameCardsRequest(ArrayList<CardView> availableCards) {
        Scanner scanner = new Scanner(System.in);

        clearScreen();
        printCompleteGameStatus();
        printSingleScoreRow();
        System.out.println("Select "+clientView.getModelView().getPlayerList().size()+" cards from the ones below:");
        for(int i=0; i<availableCards.size(); i++){
            System.out.println(String.format("%-4s",i+")")+String.format("%-10s",availableCards.get(i).getName()) +" | "+availableCards.get(i).getDescription());
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
                input=scanner.nextInt();
                chosenOptions.add(input);
                chosenCards.add(availableCards.get(input).getId());
            }
            if(areThereAnyDuplicates(chosenOptions)){
                System.out.println("No duplicates allowed, retry: ");
            }
            else if(!areTheOptionsValid(options,chosenOptions)){
                System.out.println("Not a valid sequence, retry: ");
            }
        }while (areThereAnyDuplicates(chosenOptions)||!areTheOptionsValid(options,chosenOptions));

        clientView.update(new InGameCardsSetMessage(chosenCards));
    }

    /**
     * This functions is called when the user has to chose the initial position for the pawns
     * @param availablePositions the available positions to chose from
     */
    @Override public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions) {
        Scanner scanner = new Scanner(System.in);

        clearScreen();
        printCompleteGameStatus();

        System.out.println("Select the positions [x,y] for your pawns from the ones below:");
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
            choice1=scanner.nextInt();
            System.out.print("Second pawn position choice (0->" + (availablePositions.size() - 1) + "): ");
            choice2=scanner.nextInt();
            if(!isTheOptionValid(options,choice1)||!isTheOptionValid(options,choice2) || choice1==choice2){
                System.out.println("Not valid options, retry: ");
            }
        }while(!isTheOptionValid(options,choice1)||!isTheOptionValid(options,choice2)|| (choice1==choice2));


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
        System.out.print("Select your nickname: ");
        String name = scanner.nextLine();
        clientView.update(new NicknameSetMessage(name));
    }

    /**
     * This function is called when  the user has to select the number of players in the game
     */
    @Override public void onNumberOfPlayersRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select the number of players in the game (2 or 3): ");
        int number;
        do {
            number = scanner.nextInt();
            if(number!=3 && number!=2){
                System.out.print("Not a valid option, retry: ");
            }
        }while (number!=3 && number!=2);

        clientView.update(new NumberOfPlayersSetMessage(number));
    }

    /**
     * This function is called when the user has to select the pawn for the current turn
     * @param availablePositions the positions of his pawns, from where to chose from
     */
    @Override public void onSelectPawnRequest(ArrayList<Position> availablePositions) {
        Scanner scanner = new Scanner(System.in);

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

        //TODO: place the number of the pawn not the number of the option
        System.out.print("Choice (0->"+(availablePositions.size()-1)+"): ");

        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for(int j=0; j<pawnViews.size(); j++){
            options.add(pawnViews.get(j).getId());
        }

        do {
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));

        Position selectedPawnPosition=null;
        for(PawnView pawnView : pawnViews){
            if(pawnView.getId()==input){
                selectedPawnPosition=pawnView.getPawnPosition();
            }
        }

        clientView.update(new SelectedPawnSetMessage(selectedPawnPosition));
    }


    //COMMODITY FUNCTIONS
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

    private boolean isTheOptionValid(ArrayList<Integer> options, Integer option){
        return options.contains(option);
    }

    private boolean areTheOptionsValid(ArrayList<Integer> options, ArrayList<Integer> chosenOptions){
        for(Integer a : chosenOptions){
            if(!options.contains(a)){
                return false;
            }
        }
        return true;
    }

    private boolean areThereAnyDuplicates(ArrayList<Integer> integers){
        Set<Integer> a = new HashSet<>(integers);
        return a.size()<integers.size();
    }

    public int undoOrConfirmHandler(int timeToWait) {
        int input;

        Scanner s = new Scanner(System.in);
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
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
            future.cancel(true);
            timer.cancel();
        }

        timer.cancel();
        input = Integer.parseInt(in);
        executorService.shutdown();
        return input;
    }

    private class ConsoleInputReadTask implements Callable<String> {
        private boolean enabled = true;

        public void disable() {
            enabled = false;
        }

        public String call() throws IOException {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(System.in));
            String input;
            do {
                //System.out.println("Please type something: ");
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


    //COMMODITY PRINT FUNCTIONS
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
    private void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    private void printSingleUnderscoreRow(){
        System.out.println("_________________________________________");
    }
    private void printHorizontalCoordinates(){
        System.out.println("         x:0     x:1     x:2     x:3     x:4   ");
    }
    private void printEqualsRow(){
        System.out.println("==============================================================");

    }
    private void printSingleScoreRow(){
        System.out.println("--------------------------------------------------------------");
    }
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
    private void printCompleteGameStatus(){
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
        printBoard();
    }
    private void printWelcome(){
        System.out.println("==============================================================");
        System.out.println("                         SANTORINI                            ");
        System.out.println("==============================================================");
    }


    public ClientView getClientView() {
        return clientView;
    }
    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }
    public static void main(String[] args) {
        ClientView clientView = new ClientView();
        clientView.getModelView().onPlayerUpdate("Ian","009900",0,1);
        clientView.getModelView().onPlayerUpdate("Luca","990000",2,3);
        clientView.getModelView().onPlayerUpdate("Riccardo","000099",4,5);
        clientView.getModelView().onPawnPositionUpdate(0,new Position(0,0));
        clientView.getModelView().onPawnPositionUpdate(1,new Position(0,1));
        clientView.getModelView().onPawnPositionUpdate(2,new Position(1,0));
        clientView.getModelView().onPawnRemoved(2);
        clientView.getModelView().onPawnPositionUpdate(3,new Position(1,1));
        clientView.getModelView().onPawnPositionUpdate(4,new Position(0,2));
        clientView.getModelView().onPawnPositionUpdate(5,new Position(2,0));
        clientView.getModelView().onCellUpdate(new Position(3,3), BlockType.LEVEL1);
        clientView.getModelView().onCellUpdate(new Position(0,0), BlockType.LEVEL2);
        clientView.getModelView().onCellUpdate(new Position(4,4), BlockType.DOME);

        clientView.getModelView().onChosenCardUpdate(new CardView(1,"Apollo","do as he wishes"),"Ian");
        //clientView.getModelView().onChosenCardUpdate(new CardView(2,"Medusa","do as he wishes"),"Luca");
        clientView.getModelView().onChosenCardUpdate(new CardView(3,"Dimetrio","do as he wishes"),"Riccardo");
        CLIEngine cliEngine = new CLIEngine();
        cliEngine.setClientView(clientView);

        /*ArrayList<BlockType> arrayList = new ArrayList<>();
        arrayList.add(BlockType.LEVEL1);
        arrayList.add(BlockType.DOME);
        cliEngine.onChosenBlockTypeRequest(arrayList);*/

        /*ArrayList<CardView> arrayList = new ArrayList<>();
        arrayList.add(new CardView(1,"Apollo","ciao ciao ciao"));
        arrayList.add(new CardView(2,"Dimetrio","cdsfsdfsdf fevdsvsf sfdv"));
        arrayList.add(new CardView(3,"Medusa","fsdfggfdgfds  sgfd sfdg "));
        arrayList.add(new CardView(4,"LALA","fsdfggfdgfds  sgfd sfdg "));
        cliEngine.onInGameCardsRequest(arrayList);*/

        ArrayList<Position> arrayList = new ArrayList<>();
        arrayList.add(new Position(0,0));
        arrayList.add(new Position(0,1));
        arrayList.add(new Position(0,2));
        arrayList.add(new Position(0,3));
        arrayList.add(new Position(0,4));
        arrayList.add(new Position(0,5));
        arrayList.add(new Position(1,5));
        arrayList.add(new Position(2,5));
        arrayList.add(new Position(3,5));
        arrayList.add(new Position(0,0));
        arrayList.add(new Position(0,1));
        arrayList.add(new Position(0,2));
        arrayList.add(null);
        arrayList.add(new Position(0,3));
        arrayList.add(new Position(0,4));
        arrayList.add(new Position(0,5));
        arrayList.add(new Position(1,5));
        arrayList.add(new Position(2,5));
        arrayList.add(new Position(3,5));
        cliEngine.onFirstPlayerRequest();
    }
    private boolean isThereAnyWinner(){
        for(PlayerView  p : clientView.getModelView().getPlayerList()){
            if(p.getWinner()){
                return true;
            }
        }
        return false;
    }
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


    //FOR NOW, NOT NEEDED
    @Override public void refreshView(PawnView pawnView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if(!isThereAnyWinner() && haveILost()){
            System.out.println("YOU HAVE LOST, JUST WATCH THE GAME TILL THE END!");
            printSingleScoreRow();
            System.out.println();
        }
        printCompleteGameStatus();
    }
    @Override public void refreshView(CardView cardView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if(!isThereAnyWinner() && haveILost()){
            System.out.println("YOU HAVE LOST, JUST WATCH THE GAME TILL THE END!");
            printSingleScoreRow();
            System.out.println();
        }
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }
    @Override public void refreshView(PlayerView playerView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if(!isThereAnyWinner() && haveILost()){
            System.out.println("YOU HAVE LOST, JUST WATCH THE GAME TILL THE END!");
            printSingleScoreRow();
            System.out.println();
        }
        if(isThereAnyPawnOnTheBoard()){
            printCompleteGameStatus();
        }else{
            printPlayersWith_Cards_WinnerStatus_PawnsIds();
        }
    }
    @Override public void refreshView(CellView cellView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if(!isThereAnyWinner() && haveILost()){
            System.out.println("YOU HAVE LOST, JUST WATCH THE GAME TILL THE END!");
            printSingleScoreRow();
            System.out.println();
        }
        printCompleteGameStatus();
    }



}

