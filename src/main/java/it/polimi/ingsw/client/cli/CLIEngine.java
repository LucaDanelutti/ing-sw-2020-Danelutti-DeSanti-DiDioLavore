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

public class CLIEngine implements UserInterface {
    private ClientView clientView;

    public CLIEngine() {
    }
    public CLIEngine(ClientView clientView) {
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

        cliEngine.onYouLostAndSomeOneWon("ian");
    }


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
    @Override public void quickInitialize(String hostname, int port) {
        clientView = new ClientView();
        clientView.setUserInterface(this);
        clientView.startServerConnection(hostname, port);
    }


    @Override public void refreshView() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();
    }
    @Override public void refreshView(PawnView pawnView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();
    }
    @Override public void refreshView(CardView cardView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }
    @Override public void refreshView(PlayerView playerView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if(isThereAnyPawnOnTheBoard()){
            printCompleteGameStatus();
        }else{
            printPlayersWith_Cards_WinnerStatus_PawnsIds();
        }
    }
    @Override public void refreshView(CellView cellView) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();
    }

    @Override
    public void refreshViewOnlyGameInfo() {
        clearScreen();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }

    @Override public void onWin() {
        clearScreen();
        printEqualsRow();
        System.out.println("                           YOU WON!");
        printEqualsRow();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }
    @Override public void onYouLostAndSomeOneWon(String winnerName) {
        clearScreen();
        printEqualsRow();
        System.out.println("                          YOU LOST!");
        printEqualsRow();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
    }
    @Override public void onGameEnded(String reason) {
        clearScreen();
        System.out.println("GAME ENDED: "+reason);
    }

    @Override public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();

        Scanner scanner = new Scanner(System.in);
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
        System.out.print("Choice (0->"+(availableBlockTypes.size()-1)+"): ");
        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for(int i=0; i<availableBlockTypes.size(); i++){
            options.add(i);
        }
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
    @Override public void onChosenCardRequest(ArrayList<CardView> availableCards) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printPlayersWith_Cards_WinnerStatus_PawnsIds();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select your card from the ones below:");
        for(int i=0; i<availableCards.size(); i++){
            System.out.println(i+") "+String.format("%-10s",availableCards.get(i).getName()) + " | "+availableCards.get(i).getDescription());
        }
        System.out.print("Choice (0->"+(availableCards.size()-1)+"): ");
        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for(int i=0; i<availableCards.size(); i++){
            options.add(i);
        }
        do {
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));
        clientView.update(new ChosenCardSetMessage(availableCards.get(input).getId()));
    }
    @Override public void onChosenPositionForMoveRequest(ArrayList<Position> availablePositions) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();
        Scanner scanner = new Scanner(System.in);
        System.out.println("MOVE -> Select the position from the ones below:");
        for(int i=0; i<availablePositions.size(); i++){
            if(i%5==0&&i>0){
                System.out.println();
            }
            if(availablePositions.get(i)==null){
                System.out.print(i + ") SKIP ");
            }else {
                System.out.print(i + ") x:" + availablePositions.get(i).getX() + " y:" + availablePositions.get(i).getY()+"   ");
            }
        }
        System.out.println();
        System.out.print("Choice (0->"+(availablePositions.size()-1)+"): ");
        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for(int i=0; i<availablePositions.size(); i++){
            options.add(i);
        }
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
    @Override public void onChosenPositionForConstructRequest(ArrayList<Position> availablePositions) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();
        Scanner scanner = new Scanner(System.in);
        System.out.println("CONSTRUCT -> Select the position from the ones below:");
        for(int i=0; i<availablePositions.size(); i++){
            if(i%5==0&&i>0){
                System.out.println();
            }
            if(availablePositions.get(i)==null){
                System.out.print(i + ") SKIP ");
            }else {
                System.out.print(i + ") x:" + availablePositions.get(i).getX() + " y:" + availablePositions.get(i).getY()+"   ");
            }
        }
        System.out.println();
        System.out.print("Choice (0->"+(availablePositions.size()-1)+"): ");
        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for(int i=0; i<availablePositions.size(); i++){
            options.add(i);
        }
        do {
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));
        clientView.update(new ChosenPositionSetMessage(availablePositions.get(input)));
    }
    @Override public void onFirstPlayerRequest() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the first player from the ones below:");
        int i=0;
        for(PlayerView playerView : clientView.getModelView().getPlayerList()){
            System.out.println(i+") "+playerView.getName());
            i++;
        }
        System.out.print("Choice (0->"+(clientView.getModelView().getPlayerList().size()-1)+"): ");
        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for(int j=0; j<clientView.getModelView().getPlayerList().size(); j++){
            options.add(j);
        }
        do {
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));
        clientView.update(new FirstPlayerSetMessage(clientView.getModelView().getPlayerList().get(input).getName()));
    }
    @Override public void onInGameCardsRequest(ArrayList<CardView> availableCards) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();

        printEqualsRow();
        ArrayList<Integer> chosenCards;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select "+clientView.getModelView().getPlayerList().size()+" cards from the ones below:");

        for(int i=0; i<availableCards.size(); i++){
            System.out.println(i+") "+String.format("%-10s",availableCards.get(i).getName()) +" | "+availableCards.get(i).getDescription());
        }
        printSingleScoreRow();

        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availableCards.size(); i++){
            options.add(i);
        }
        ArrayList<Integer> chosenOptions = new ArrayList<>();

        int input;
        do {
            chosenCards = new ArrayList<>();
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

        printEqualsRow();

        clientView.update(new InGameCardsSetMessage(chosenCards));
    }
    @Override public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();

        ArrayList<Position> pawnsPositions;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the positions for your pawns from the ones below:");

        for(int i=0; i<availablePositions.size(); i++){
            System.out.print(i+") x:"+availablePositions.get(i).getX()+" y:"+availablePositions.get(i).getY() + " | ");
            if(i%5==0 && i!=0){
                System.out.println();
            }
        }
        System.out.println();
        printSingleScoreRow();
        Position one;
        Position two;
        int choice1;
        int choice2;
        ArrayList<Integer> options = new ArrayList<>();
        for(int i=0; i<availablePositions.size(); i++){
            options.add(i);
        }

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
    @Override public void onNicknameRequest() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printWelcome();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select your nickname: ");
        String name = scanner.nextLine();
        clientView.update(new NicknameSetMessage(name));
    }
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
    @Override public void onSelectPawnRequest(ArrayList<Position> availablePositions) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printCompleteGameStatus();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the pawns from the ones below:");
        ArrayList<PawnView> pawnViews = new ArrayList<>();
        for(PlayerView playerView : clientView.getModelView().getPlayerList()){
            if(playerView.getName().equals(clientView.getName())){
                pawnViews=playerView.getPawnList();
            }
        }
        int i=0;
        for(Position position : availablePositions) {
            for (PawnView pawnView : pawnViews) {
                if (pawnView.getPawnPosition().equals(position)) {
                    System.out.println(i + ") pawn: " + pawnView.getId());
                    i++;
                }
            }
        }
        System.out.print("Choice (0->"+(availablePositions.size()-1)+"): ");
        ArrayList<Integer> options = new ArrayList<>();
        int input;
        for(int j=0; j<availablePositions.size(); j++){
            options.add(j);
        }
        do {
            input = scanner.nextInt();
            if(!isTheOptionValid(options,input)){
                System.out.print("Not a valid option, retry: ");
            }
        }while (!isTheOptionValid(options,input));
        clientView.update(new SelectedPawnSetMessage(availablePositions.get(input)));
    }



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
        System.out.println("---------------------------------------------------");
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

    public ClientView getClientView() {
        return clientView;
    }
    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }

}

