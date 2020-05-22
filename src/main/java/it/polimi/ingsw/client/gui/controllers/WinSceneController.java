package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.view.modelview.ModelView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class WinSceneController extends GUIController {

    @FXML
    private ImageView winImageView;

    @FXML
    public void initialize() {

    }

    public void setUpScene() {
        ModelView modelView = clientView.getModelView();
        String playerColor = modelView.getPlayerColor(clientView.getName());
        System.out.println("playerColor:" + playerColor);
        Image winImage = new Image("images/utility/winner_badge_" + playerColor + ".png");
        winImageView.setImage(winImage);
        winImageView.setPreserveRatio(true);
    }

    public void closeStage() {
        stage.close();
    }

}
