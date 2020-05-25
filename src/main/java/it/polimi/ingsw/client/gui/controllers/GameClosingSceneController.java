package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameClosingSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private Label messageLabel;

    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {
    }

    /**
     * Sets the text of the label that has to be rendered.
     * @param message is the text that has to be set.
     */
    public void loadMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * It is activated when the player clicks on the close button.
     * It closes the current stage.
     */
    public void closeStage() {
        stage.close();
    }
}
