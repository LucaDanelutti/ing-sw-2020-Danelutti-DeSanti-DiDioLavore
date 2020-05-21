package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


//TODO: add loading animation, logo etc.
public class WaitingSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private Label waitLabel;

    /* ===== FXML Properties ===== */
    private StringProperty waitString;

    @FXML
    public void initialize() {
        waitString = new SimpleStringProperty("Lobby");
        waitLabel.textProperty().bind(waitString);
    }
}
