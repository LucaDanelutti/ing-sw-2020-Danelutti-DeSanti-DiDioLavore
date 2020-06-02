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
    StringProperty waitString = new SimpleStringProperty("");
    @FXML
    public void initialize() {
        /* ===== FXML Properties ===== */
        waitLabel.textProperty().bind(waitString);
    }

    public void setTitle(String title) {
        waitString.set(title);
    }
}
