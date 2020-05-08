package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginSceneController extends GUIController {

    @FXML
    private TextField hostnameTextField;


    /**
     * Property to store the inserted hostname
     */
    private StringProperty serverHostname;

    @FXML
    public void initialize() {
        serverHostname = new SimpleStringProperty("");
        hostnameTextField.textProperty().bindBidirectional(serverHostname);
    }
}
