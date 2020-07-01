package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllers.*;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.modelview.CardView;
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

    /**
     * Launches the javafx application
     * @param primaryStage is the initial input stage
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        clientView = new ClientView();
        clientView.setUserInterface(this);
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/utility/santorini_logo.png")));

        loadLoginScene();
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

            stage.setOnCloseRequest( event -> {
                clientView.stopServerConnection();
            } );

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
        double newWidth = Screen.getPrimary().getBounds().getWidth()/widthRation;
        double newHeight = Screen.getPrimary().getBounds().getHeight()/heightRatio;
        stage.setX(stage.getX() - (newWidth - stage.getWidth())/2);
        stage.setY(stage.getY() - (newHeight - stage.getHeight())/2);
        stage.setMinWidth(newWidth);
        stage.setMinHeight(newHeight);
        stage.setWidth(newWidth);
        stage.setHeight(newHeight);
    }


    /**
     * Refreshes the players details within the main scene.
     */
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

    /**
     * Refreshes the main scene.
     */
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
    public void showWaitingScene(Boolean setMinDim, String title) {
        Platform.runLater(() -> {
            showScene("/fxml/waitingScene.fxml");
            ((WaitingSceneController)currentController).setTitle(title);
            if (setMinDim) {
                updateStageSize(2, 2);
            } else {
                updateStageSize(4, 3);
            }
        });
    }

    /**
     * Loads the availableBlockTypes through the proper controller
     * @param availableBlockTypes is the list of availableBlockTypes
     */
    @Override
    public void onChosenBlockTypeRequest(ArrayList<BlockType> availableBlockTypes) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).chooseBlockType(availableBlockTypes);
        });
    }

    /**
     * Loads the availableCards through the proper controller
     * @param availableCards is the list of available cards.
     */
    @Override
    public void onChosenCardRequest(ArrayList<CardView> availableCards) {
        Platform.runLater(() -> {
            showScene("/fxml/chosenCardRequestScene.fxml");
            updateStageSize(2,1.7);
            ((ChosenCardRequestSceneController)currentController).loadCards(availableCards);
        });
    }

    /**
     * Loads the availablePositions for a move action through the proper controller.
     * @param availablePositions is the list of available positions for a move action.
     */
    @Override
    public void onChosenPositionForMoveRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).chooseMovePosition(availablePositions);
        });
    }

    /**
     * Loads the availablePositions for a construct action through the proper controller.
     * @param availablePositions is the list of available positions for a construct action.
     */
    @Override
    public void onChosenPositionForConstructRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).chooseConstructPosition(availablePositions);
        });
    }

    /**
     * Loads the scene which lets the user select the first player of the match.
     */
    @Override
    public void onFirstPlayerRequest() {
        Platform.runLater(() -> {
            showScene("/fxml/firstPlayerRequestScene.fxml");
            updateStageSize(4, 2.5);
            ((FirstPlayerRequestSceneController)currentController).loadPlayers();
        });
    }

    /**
     * Loads the scene which lets the user select the cards available within the  current match.
     * @param availableCards is the list of cards available.
     */
    @Override
    public void onInGameCardsRequest(ArrayList<CardView> availableCards) {
        Platform.runLater(() -> {
            showScene("/fxml/gameCardsRequestScene.fxml");
            updateStageSize(1.5, 1.3);
            ((GameCardsRequestSceneController)currentController).setUpScene(availableCards);
        });
    }

    /**
     * Asks the user to select the initial positions of its pawns.
     * @param availablePositions is the list of available positions.
     */
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

    /**
     * Loads the scene which lets the user select his nickname.
     */
    @Override
    public void onNicknameRequest() {
        Platform.runLater(() -> {
            showScene("/fxml/nicknameRequestScene.fxml");
            updateStageSize(4, 3);
        });
    }

    /**
     * Loads the scene which lets the user select the number of players for the current match.
     */
    @Override
    public void onNumberOfPlayersRequest() {
        Platform.runLater(() -> {
            showScene("/fxml/numberOfPlayersRequestScene.fxml");
            updateStageSize(2, 2);
        });
    }

    /**
     * Asks the user to select a pawn among the available ones on the board.
     * @param availablePositions is the list of pawns available on the board for the current user.
     */
    @Override
    public void onSelectPawnRequest(ArrayList<Position> availablePositions) {
        Platform.runLater(() -> {
            ((MainSceneController)currentController).enablePawnSelection(availablePositions);
        });
    }

    /**
     * Loads the winner scene.
     */
    @Override
    public void onWin() {
        Platform.runLater(() -> {
            showScene("/fxml/winScene.fxml");
            updateStageSize(4, 3);
            ((WinSceneController)currentController).setUpScene();
        });
    }

    /**
     * Loads the looser scene.
     */
    @Override
    public void onLost(String playerName) {
        Platform.runLater(() -> {
            if (playerName.equals(clientView.getName())) {
                ((MainSceneController) currentController).updateLostPhaseLabel();
            }
        });
    }

    /**
     * Loads an end game scene which lets the user know that someone else has won.
     */
    @Override
    public void onYouLostAndSomeOneWon(String winnerName) {
        Platform.runLater(() -> {
            showScene("/fxml/youLost.fxml");
            updateStageSize(4, 3);
            ((YouLostSceneController)currentController).loadData(winnerName);
        });
    }

    /**
     * Loads an end game message.
     * @param reason is the reason why the game ended.
     */
    @Override
    public void onGameEnded(String reason) {
        Platform.runLater(() -> {
            showScene("/fxml/gameClosingScene.fxml");
            updateStageSize(4, 3);
            ((GameClosingSceneController)currentController).loadMessage(reason);
        });
    }

    /**
     * Lets the user know that he hasn't been selected for the current match.
     */
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
