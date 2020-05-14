package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.view.modelview.CardView;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;

public class GameCardsSceneController extends GUIController {

    @FXML
    private GridPane cardsGridPane;


    public void initialize() {

    }

    public void confirm() {

    }

    public void loadCards(ArrayList<CardView> availableCards) {
        for (int i=0; i< cardsGridPane.getRowCount(); i++) {
            for (int j=0; j< cardsGridPane.getRowCount(); j++) {
                Image cardImage = new Image("images/cards/card_" + availableCards.get(j + 4*i).getId() + ".png");
                ImageView cardImageView = new ImageView(cardImage);
                cardImageView.setPreserveRatio(true);
                cardImageView.fitWidthProperty().bind(cardsGridPane.widthProperty().divide(4));
                cardImageView.fitHeightProperty().bind(cardsGridPane.heightProperty().divide(4));
                cardsGridPane.add(cardImageView, i, j);
            }
        }
    }
}
