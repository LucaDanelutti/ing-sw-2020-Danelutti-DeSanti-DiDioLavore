package it.polimi.ingsw.client.gui;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.client.gui.controllers.*;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUIEngine extends Application implements UserInterface {

    private Stage stage;
    private ClientView clientView;
    private GUIController currentController;
    private static final String SANTORINI_STAGE_TITLE = "Santorini";
    private boolean isGameStarted = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        clientView = new ClientView();
        clientView.setUserInterface(this);

//        setUpTest();

        loadLoginScene();
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
            System.out.println("Exception while loading fxml resource.");
            System.out.println(e.toString());
            System.out.println(e.getCause().toString());
        }

    }

    /**
     * Updates the minimum and current size of the stage.
     * @param widthRation is the width fraction of the screen size.
     * @param heightRatio is the height fraction of the screen size.
     */
    private void updateStageSize(double widthRation, double heightRatio) {
        stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/widthRation);
        stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/heightRatio);
        stage.setWidth(Screen.getPrimary().getBounds().getWidth()/widthRation);
        stage.setHeight(Screen.getPrimary().getBounds().getHeight()/heightRatio);
    }



    @Override
    public void refreshViewOnlyGameInfo() {
        Platform.runLater(() -> {
            try {
                if (isGameStarted) ((MainSceneController) currentController).updateGameInfo();
            } catch (Exception e) {
                System.out.println("Problem while refreshViewOnlyGameInfo(): MainSceneController may not be the currentController");
            }
        });
    }

    @Override
    public void refreshView() {
        Platform.runLater(() -> {
            try {
                if (isGameStarted)  {
                    ((MainSceneController) currentController).updateBoard();
                    ((MainSceneController) currentController).updateGameInfo();
                }
            } catch (Exception e) {
                System.out.println("Problem while refreshView(): MainSceneController may not be the currentController");
                System.out.println(e.toString());
                System.out.println(e.getCause().toString());
            }
        });
    }

    /**
     * Loads the main scene controller and fxml scene
     */
    public void showMainScene() {
        Platform.runLater(() -> {
            showScene("/fxml/mainScene.fxml");
            updateStageSize(1.5, 1.5);
            ((MainSceneController)currentController).buildMainScene();
            isGameStarted = true;
        });
    }

    /**
     * Loads the waiting scene controller and fxml scene
     */
    public void showWaitingScene(Boolean setMinDim) {
        Platform.runLater(() -> {
            showScene("/fxml/waitingScene.fxml");
            if (setMinDim) {
                updateStageSize(2, 2);
            } else {
                updateStageSize(4, 3);
            }
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
            updateStageSize(2,2);
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
        Platform.runLater(() -> {
            showScene("/fxml/firstPlayerRequestScene.fxml");
            updateStageSize(4, 2.5);
            ((FirstPlayerRequestSceneController)currentController).loadPlayers();
        });
    }

    @Override
    public void onInGameCardsRequest(ArrayList<CardView> availableCards) {
        Platform.runLater(() -> {
            showScene("/fxml/gameCardsRequestScene.fxml");
            updateStageSize(1.5, 1.3);
            ((GameCardsRequestSceneController)currentController).setUpScene(availableCards);
        });
    }

    @Override
    public void onInitialPawnPositionRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            try {
                ((MainSceneController)currentController).placeInitialPawns(availablePositions);
            } catch (Exception e) {
                System.out.println("Problem while executing onInitialPawnPositionRequest():" + e.toString());
            }
        });
    }

    @Override
    public void onNicknameRequest() {
        Platform.runLater(() -> {
            showScene("/fxml/nicknameRequestScene.fxml");
            updateStageSize(4, 3);
        });
    }

    @Override
    public void onNumberOfPlayersRequest() {
        Platform.runLater(() -> {
            showScene("/fxml/numberOfPlayersRequestScene.fxml");
            updateStageSize(2, 2);
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
        Platform.runLater(() -> {
            showScene("/fxml/winScene.fxml");
            updateStageSize(4, 3);
            ((WinSceneController)currentController).setUpScene();
        });
    }

    @Override
    public void onLost(String playerName) {
        Platform.runLater(() -> {
            if (playerName.equals(clientView.getName())) {
                ((MainSceneController) currentController).updateLostPhaseLabel();
            }
        });
    }

    @Override
    public void onYouLostAndSomeOneWon(String winnerName) {
        Platform.runLater(() -> {
            showScene("/fxml/youLost.fxml");
            updateStageSize(4, 3);
            ((YouLostSceneController)currentController).loadData(winnerName);
        });
    }

    @Override
    public void onGameEnded(String reason) {
        Platform.runLater(() -> {
            showScene("/fxml/gameClosingScene.fxml");
            updateStageSize(4, 3);
            ((GameClosingSceneController)currentController).loadMessage(reason);
        });
    }

    @Override
    public void onGameStartedAndYouAreNotSelected() {
        Platform.runLater(() -> {
            showScene("/fxml/gameClosingScene.fxml");
            updateStageSize(4, 3);
            ((GameClosingSceneController)currentController).loadMessage("Unfortunately the game has started and you haven't been selected.");
        });
    }

    /**
     * Shows a pop-up message above the current stage.
     * @param message is the message that has to be rendered within the pop-up.
     * @param alertType is the type of the Alert according to the enum AlertType.
     */
    public void showMessage(String message, AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.getButtonTypes().set(0, ButtonType.OK);
            Text alertText = new Text(message);
            alertText.setTextAlignment(TextAlignment.CENTER);
            alertText.setWrappingWidth(alert.getDialogPane().getWidth());
            alert.getDialogPane().setContent(alertText);
            alert.show();
        });
    }

    /**
     * Loads the login controller and fxml scene
     */
    private void loadLoginScene() {
        Platform.runLater(() -> {
            showScene("/fxml/loginScene.fxml");
            updateStageSize(4, 3);
        });
    }
}
