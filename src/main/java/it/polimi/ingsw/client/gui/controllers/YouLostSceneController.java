package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.view.modelview.ModelView;
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

    }

    public void loadData(String winnerName) {
        youLostLabel.setText("You lost. The winner is " + winnerName);

        ModelView modelView = clientView.getModelView();
        String playerColor = modelView.getPlayerColor(clientView.getName());
        Image winImage = new Image("images/utility/loose_" + playerColor + ".png");
        looseImageView.setImage(winImage);
        looseImageView.setPreserveRatio(true);
    }

    public void closeStage() {
        stage.close();
    }
}
