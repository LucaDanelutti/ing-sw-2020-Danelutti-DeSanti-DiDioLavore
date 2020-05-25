package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.ChosenCardSetMessage;
import it.polimi.ingsw.view.modelview.CardView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ChosenCardRequestSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private GridPane mainGridPane;
    @FXML
    private HBox cardsHBox;
    @FXML
    private Label cardDescriptionLabel;
    @FXML
    private Button confirmButton;


    /* ===== Variables ===== */
    private ArrayList<CardView> availableCards = new ArrayList<>();
    private int chosenCardId;

    public void initialize() {
        cardsHBox.spacingProperty().bind(cardsHBox.widthProperty().divide(10));

    }


    /**
     * It loads the ImageViews of the CardViews contained within availableCards.
     * @param availableCards is the list of CardViews that have to be rendered.
     */
    public void loadCards(ArrayList<CardView> availableCards) {
        this.availableCards.addAll(availableCards);
        for (CardView card : availableCards) {
            Image cardImage = new Image("images/cards/card_" + card.getId() + ".png");
            ImageView cardImageView = new ImageView(cardImage);
            cardImageView.setPreserveRatio(true);
            cardImageView.setId(String.valueOf(card.getId()));
            cardImageView.fitWidthProperty().bind(cardsHBox.widthProperty().divide(availableCards.size()));
            cardImageView.fitHeightProperty().bind(cardsHBox.heightProperty());
            cardsHBox.getChildren().add(cardImageView);
            cardImageView.setOnMouseClicked(e -> {
                Node source = (Node)e.getSource();
                int cardId = Integer.parseInt(source.getId());
                System.out.printf("cardId: %d %n", cardId);
                updateDescriptionLabel(cardId);
                updateConfirmButtonText(cardId);
                chosenCardId = cardId;
            });
        }

        //picks the first card available
        chosenCardId = availableCards.get(0).getId();
        updateDescriptionLabel(chosenCardId);
        updateConfirmButtonText(chosenCardId);
    }

    /**
     * Updates the description label setting the description of the card currently selected.
     * @param cardId is the id of the card currently selected.
     */
    private void updateDescriptionLabel(int cardId) {
        for (CardView card: availableCards) {
            if (card.getId() == cardId) {
                cardDescriptionLabel.setText(card.getDescription());
            }
        }
    }

    /**
     * It is activated when the user clicks on a card. Updates the title of the confirm button.
     * @param cardId is the id of the clicked card.
     */
    private void updateConfirmButtonText(int cardId) {
        for (CardView card: availableCards) {
            if (card.getId() == cardId) {
                confirmButton.setText("Pick " + card.getName() + "!");
            }
        }
    }

    /**
     * It is activated when the user clicks on the confirm button.
     * It forwards the chosen card to the ClientView.
     */
    public void confirm() {
        System.out.println("chosenCardId:" + chosenCardId);
        clientView.update(new ChosenCardSetMessage(chosenCardId));

        ((GUIEngine)clientView.getUserInterface()).showMainScene();
    }

}
