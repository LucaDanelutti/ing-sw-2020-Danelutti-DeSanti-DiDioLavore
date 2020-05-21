package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private TextField hostnameTextField;
    @FXML
    private TextField portTextField;

    /* ===== FXML Properties ===== */
    private StringProperty serverHostname;
    private StringProperty port;

    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {
        serverHostname = new SimpleStringProperty("");
        port = new SimpleStringProperty("");
        hostnameTextField.textProperty().bindBidirectional(serverHostname);
        portTextField.textProperty().bindBidirectional(port);
    }

    /**
     * It is activated when the player clicks on the connect button.
     * It calls a method within ClientView which sets up the connection.
     */
    public void connect() {
        System.out.println("clicked connect");
        System.out.println(serverHostname.getValue());
        System.out.println(port.getValue());

        //TODO: check port type, it must be an int
        clientView.startServerConnection(serverHostname.getValue(), Integer.parseInt(port.getValue()));

        //opens the WaitingScene
        ((GUIEngine)clientView.getUserInterface()).showWaitingScene(false);
    }
}
