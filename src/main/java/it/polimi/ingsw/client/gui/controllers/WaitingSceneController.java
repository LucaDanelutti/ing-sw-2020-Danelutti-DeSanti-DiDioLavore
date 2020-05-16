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

    @FXML
    private Label waitLabel;

    private StringProperty waitString;

    @FXML
    public void initialize() {
        waitString = new SimpleStringProperty("Waiting Screen");
        waitLabel.textProperty().bind(waitString);
    }

    @FXML
    public void testScene() {
        //testing pop-up
        Platform.runLater(() -> {
            ((GUIEngine)clientView.getUserInterface()).showMainScene();
        });
    }
}
