package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

        showScene("/fxml/loginScene.fxml", false);
    }

    @Override
    public void initialize() {
        launch();
    }

    public synchronized void showScene(String fxmlResource, Boolean isFullScreen) {
        if (fxmlResource == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlResource));
            Scene scene = loader.load();

            currentController = loader.getController();

            stage.setMaximized(isFullScreen ? true : false);

            if (stage != null) {
                stage.hide();
            } else {
                stage = new Stage();
                stage.setTitle(SANTORINI_STAGE_TITLE);
                stage.setResizable(true);
            }

            if (currentController != null) {
                currentController.setClientView(clientView);
                currentController.setStage(this.stage);
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            //TODO: manage exception properly
            System.out.println("Exception while loading fxml resource.");
        }

    }

    //TODO: implement update() function within GuiController and call them within the methods below
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
    public void showBoardScreen() {
        showScene("/fxml/mainScene.fxml", true);
    }
}
