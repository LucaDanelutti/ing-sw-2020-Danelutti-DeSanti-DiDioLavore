package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.view.ClientView;
import javafx.stage.Stage;

/**
 * Abstract class which generalizes the concept of controller
 */
public abstract class GUIController {
    ClientView clientView;
    Stage stage;

    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
