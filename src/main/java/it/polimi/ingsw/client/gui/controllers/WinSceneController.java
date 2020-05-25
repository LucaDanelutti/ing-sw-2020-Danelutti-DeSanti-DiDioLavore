package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.view.modelview.ModelView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class WinSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private ImageView winImageView;


    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {

    }

    /**
     * Sets up the scene loading the ImageView.
     */
    public void setUpScene() {
        ModelView modelView = clientView.getModelView();
        String playerColor = modelView.getPlayerColor(clientView.getName());
        System.out.println("playerColor:" + playerColor);
        Image winImage = new Image("images/utility/winner_badge_" + playerColor + ".png");
        winImageView.setImage(winImage);
        winImageView.setPreserveRatio(true);
    }

    /**
     * It is activated when the player clicks on the close button.
     * It closes the current stage.
     */
    public void closeStage() {
        stage.close();
    }

}
