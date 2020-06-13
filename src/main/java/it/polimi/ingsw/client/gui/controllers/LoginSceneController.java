package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        // makes portTextField numeric only
        portTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    portTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    /**
     * It is activated when the player clicks on the connect button.
     * It calls a method within ClientView which sets up the connection.
     */
    public void connect() {
        if (port.getValue().length() > 0 && clientView.startServerConnection(serverHostname.getValue(), Integer.parseInt(port.getValue()))) {
            //opens the WaitingScene
            ((GUIEngine)clientView.getUserInterface()).showWaitingScene(false, "Just wait...");
        } else {
            ((GUIEngine)clientView.getUserInterface()).showMessage("Wrong hostname or port. Please try again!", Alert.AlertType.ERROR);
        }
    }
}
