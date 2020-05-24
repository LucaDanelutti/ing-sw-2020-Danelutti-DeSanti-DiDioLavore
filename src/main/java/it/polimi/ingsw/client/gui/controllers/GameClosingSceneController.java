package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameClosingSceneController extends GUIController {

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
    }

    public void loadMessage(String message) {
        messageLabel.setText(message);
    }

    public void closeStage() {
        stage.close();
    }
}
