package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


//TODO: add loading animation, logo, background etc.
public class WaitingSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private Label waitLabel;


    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {
        /* ===== FXML Properties ===== */
        StringProperty waitString = new SimpleStringProperty("Lobby");
        waitLabel.textProperty().bind(waitString);
    }
}
