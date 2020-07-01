package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.view.ClientView;
import javafx.stage.Stage;

/**
 * Abstract class which generalizes the concept of controller
 */
public abstract class GUIController {
    public ClientView clientView;
    public Stage stage;

    /**
     * Sets clientView as ClientView to the current controller
     * @param clientView is the ClientView that has to be set.
     */
    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }

    /**
     * Sets stage as Stage to the current controller
     * @param stage is the Stage that has to be set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
