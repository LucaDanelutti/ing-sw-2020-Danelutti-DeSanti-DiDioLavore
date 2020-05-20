package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class YouLostSceneController extends GUIController {
    @FXML
    private ImageView looseImageView;
    @FXML
    private Label youLostLabel;

    @FXML
    public void initialize() {
        Image winImage = new Image("images/utility/winner_badge.png");
        looseImageView.setImage(winImage);
        looseImageView.setPreserveRatio(true);
    }

    public void loadData(String winnerName) {
        youLostLabel.setText("You lost. The winner is " + winnerName);
    }
}
