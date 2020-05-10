package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WaitingSceneController extends GUIController {

    @FXML
    private Label waitLabel;

    private StringProperty waitString;
    @FXML
    public void initialize() {
        waitLabel.textProperty().bind(waitString);
    }
}
