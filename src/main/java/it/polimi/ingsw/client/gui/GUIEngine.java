package it.polimi.ingsw.client.gui;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.client.gui.controllers.ChosenCardRequestSceneController;
import it.polimi.ingsw.client.gui.controllers.GameCardsRequestSceneController;
import it.polimi.ingsw.client.gui.controllers.MainSceneController;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUIEngine extends Application implements UserInterface {

    private Stage stage;
    private ClientView clientView;
    private GUIController currentController;
    private static final String SANTORINI_STAGE_TITLE = "Santorini";

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        clientView = new ClientView();
        clientView.setUserInterface(this);

        setUpTest();

        //TODO: ("/fxml/loginScene.fxml", false) should be passed below, other scenes are passed as a test
        showScene("/fxml/waitingScene.fxml");
    }

    //test function
    private void setUpTest() {
        clientView.getModelView().onPlayerUpdate("Ian","blue",0,1);
        clientView.getModelView().onPlayerUpdate("Luca","green",2,3);
        clientView.getModelView().onPlayerUpdate("Riccardo","orange",4,5);
        clientView.getModelView().onPawnPositionUpdate(0,new Position(0,2));
        clientView.getModelView().onPawnPositionUpdate(1,new Position(0,1));
        clientView.getModelView().onPawnPositionUpdate(2,new Position(1,0));
        clientView.getModelView().onPawnPositionUpdate(3,new Position(1,1));
        clientView.getModelView().onPawnPositionUpdate(4,new Position(4,1));
        clientView.getModelView().onPawnPositionUpdate(5,new Position(2,3));
        clientView.getModelView().onCellUpdate(new Position(3,3), BlockType.LEVEL1);
        clientView.getModelView().onCellUpdate(new Position(1,1), BlockType.LEVEL2);
        clientView.getModelView().onCellUpdate(new Position(4,0), BlockType.LEVEL3);
        clientView.getModelView().onCellUpdate(new Position(3,2), BlockType.DOME);
        clientView.getModelView().onChosenCardUpdate(new CardView(1,"Apollo","do as he wishes"),"Ian");
        clientView.getModelView().onChosenCardUpdate(new CardView(2,"Arthemis","do as he wishes"),"Luca");
        clientView.getModelView().onChosenCardUpdate(new CardView(3,"Athena","do as he wishes"),"Riccardo");
        clientView.setName("Riccardo");
    }

    //test function
    public void updateModelView() {
        clientView.getModelView().onPlayerUpdate("Ian","blue",0,1);
        clientView.getModelView().onPlayerUpdate("Luca","green",2,3);
        clientView.getModelView().onPlayerUpdate("Riccardo","orange",4,5);
        clientView.getModelView().onPawnPositionUpdate(0,new Position(0,0));
        clientView.getModelView().onPawnPositionUpdate(1,new Position(0,1));
        clientView.getModelView().onPawnPositionUpdate(2,new Position(4,4));
        clientView.getModelView().onPawnPositionUpdate(3,new Position(3,3));
        clientView.getModelView().onPawnPositionUpdate(4,new Position(4,3));
        clientView.getModelView().onPawnPositionUpdate(5,new Position(2,0));
        clientView.getModelView().onCellUpdate(new Position(1,4), BlockType.LEVEL2);
        clientView.getModelView().onCellUpdate(new Position(4,0), BlockType.DOME);
        clientView.getModelView().onCellUpdate(new Position(2,2), BlockType.DOME);
        clientView.getModelView().onCellUpdate(new Position(4,4), BlockType.DOME);
        clientView.getModelView().onChosenCardUpdate(new CardView(2,"Apollo","do as he wishes"),"Ian");
        clientView.getModelView().onChosenCardUpdate(new CardView(4,"Arthemis","do as he wishes"),"Luca");
        clientView.getModelView().onChosenCardUpdate(new CardView(6,"Athena","do as he wishes"),"Riccardo");
        clientView.setName("Riccardo");
    }

    @Override
    public void initialize() {
        launch();
    }

    @Override
    public void quickInitialize(String hostname, int port) {
    }

    /**
     * Loads a scene and its relative controller from an fxml file
     * @param fxmlResource is the path of the fxml file that will be loaded
     */
    public void showScene(String fxmlResource) {
        if (fxmlResource == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlResource));
            Scene scene = loader.load();

            currentController = loader.getController();

            stage.setMaximized(false);

            if (stage != null) {
                stage.hide();
            } else {
                stage = new Stage();
                stage.setTitle(SANTORINI_STAGE_TITLE);
                stage.setResizable(true);
            }

            if (currentController != null) {
                currentController.setClientView(clientView);
                currentController.setStage(stage);
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            //TODO: manage exception properly
            System.out.println("Exception while loading fxml resource.");
            System.out.println(e.toString());
            System.out.println(e.getCause().toString());
        }

    }

    @Override
    public void refreshView(PawnView pawnView) {
    }

    @Override
    public void refreshView(CardView cardView) {

    }

    @Override
    public void refreshView(PlayerView playerView) {

    }

    @Override
    public void refreshView(CellView cellView) {

    }

    @Override
    public void refreshViewOnlyGameInfo() {

    }

    @Override
    public void refreshView() {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).updateBoard();
        });
    }

    /**
     * Loads the main scene controller and fxml scene
     */
    public void showMainScene() {
        Platform.runLater(() -> {
            showScene("/fxml/mainScene.fxml");
            stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/2);
            stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/2);
            ((MainSceneController)currentController).buildMainScene();
        });
    }

    /**
     * Loads the waiting scene controller and fxml scene
     */
    public void showWaitingScene() {
        Platform.runLater(() -> {
            showScene("/fxml/waitingScene.fxml");
            stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/2);
            stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/2);
        });
    }

    @Override
    public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).chooseBlockType(availableBlockTypes);
        });
    }

    @Override
    public void onChosenCardRequest(ArrayList<CardView> availableCards) {
        Platform.runLater(() -> {
            showScene("/fxml/chosenCardRequestScene.fxml");
            stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/5);
            stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/4);
            ((ChosenCardRequestSceneController)currentController).loadCards(availableCards);
        });
    }

    @Override
    public void onChosenPositionForMoveRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).chooseMovePosition(availablePositions);
        });
    }

    @Override
    public void onChosenPositionForConstructRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).chooseConstructPosition(availablePositions);
        });
    }

    @Override
    public void onFirstPlayerRequest() {

    }

    @Override
    public void onInGameCardsRequest(ArrayList<CardView> availableCards) {
        Platform.runLater(() -> {
            showScene("/fxml/gameCardsRequestScene.fxml");
            stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/5);
            stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/4);
            ((GameCardsRequestSceneController)currentController).loadCards(availableCards);
        });
    }

    @Override
    public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            if (!(currentController instanceof MainSceneController)) showMainScene();
            //call the controller method that lets place the pawns on the board.
        });
    }

    @Override
    public void onNicknameRequest() {
        Platform.runLater(() -> {
            showScene("/fxml/nicknameRequestScene.fxml");
            stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/5);
            stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/4);
        });
    }

    @Override
    public void onNumberOfPlayersRequest() {
        Platform.runLater(() -> {
            showScene("/fxml/numberOfPlayersRequestScene.fxml");
            stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/5);
            stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/4);
        });
    }

    @Override
    public void onSelectPawnRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).enablePawnSelection(availablePositions);
        });
    }

    @Override
    public void onWin() {

    }

    @Override
    public void onYouLostAndSomeOneWon(String winnerName) {

    }

    @Override
    public void onGameEnded(String reason) {

    }
}
