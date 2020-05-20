package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class WinSceneController extends GUIController {

    @FXML
    private ImageView winImageView;

    @FXML
    public void initialize() {
        Image winImage = new Image("images/utility/winner_badge.png");
        winImageView.setImage(winImage);
        winImageView.setPreserveRatio(true);
    }

}
