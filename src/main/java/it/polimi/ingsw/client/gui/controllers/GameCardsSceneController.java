package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;

import java.awt.*;

public class GameCardsSceneController extends GUIController {

    @FXML
    private ScrollPane cardsScrollPane;
    @FXML
    private HBox cardsHBox;

    public void initialize() {
//        Image cardImage = new Image("images/cards/card_1.png");
//        ImageView cardImageView = new ImageView(cardImage);
//
//        Label label = new Label("test");
//        cardsHBox.getChildren().add(cardImageView);
    }
}
