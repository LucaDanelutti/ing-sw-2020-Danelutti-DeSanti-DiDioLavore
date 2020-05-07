package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.CellView;
import it.polimi.ingsw.view.modelview.PawnView;
import it.polimi.ingsw.view.modelview.PlayerView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GUIEngine extends Application implements UserInterface {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("HBox Experiment 1");

        Label label = new Label("My Label GuiManager");

        Scene scene = new Scene(label, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize() {
        launch();
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
}
