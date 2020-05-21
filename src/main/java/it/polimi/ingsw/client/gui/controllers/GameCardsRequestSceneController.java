package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.InGameCardsSetMessage;
import it.polimi.ingsw.view.modelview.CardView;
import it.polimi.ingsw.view.modelview.ModelView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;

//TODO: add graphic effect on the ImageViews of the cards within cardsSelectedList
public class GameCardsRequestSceneController extends GUIController {

    /* ===== Constants ===== */
    private static final int NUM_ROWS = 2;
    private static final int NUM_COLUMNS = 7;
    private static final double CARD_GRIDPANE_HGAP = 10;
    private static final double CARD_GRIDPANE_VGAP = 10;

    /* ===== FXML elements ===== */
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane cardsGridPane;
    @FXML
    private ImageView bigCardImageView;
    @FXML
    private Label cardDescription;

    /* ===== Variables ===== */
    private int currentCardId;
    private ArrayList<CardView> availableCards = new ArrayList<>();
    private int expectedNumberOfCards;
    private ArrayList<Integer>  cardsSelectedList = new ArrayList<>();

    /* ===== FXML Set Up and Bindings ===== */
    public void initialize() {
        cardsGridPane.setHgap(CARD_GRIDPANE_HGAP);
        cardsGridPane.setVgap(CARD_GRIDPANE_VGAP);
    }

    /**
     * It loads the ImageViews of the CardViews contained within availableCards.
     * @param availableCards is the list of CardViews that have to be rendered.
     */
    public void loadCards(ArrayList<CardView> availableCards) {
        //sets the expectedNumberOfCards
        ModelView modelView = clientView.getModelView();
        expectedNumberOfCards = modelView.getPlayerList().size();
        System.out.println("expectedNumberOfCards:" + expectedNumberOfCards);
        titleLabel.setText("Pick " + expectedNumberOfCards + " Cards!");

        //loads the availableCards within this.availableCards and renders the images
        this.availableCards.addAll(availableCards);
        for (int i = 0; i < cardsGridPane.getRowCount(); i++) {
            for (int j = 0; j < cardsGridPane.getColumnCount(); j++) {
                addImageView(i, j, availableCards.get(j + 6 * i).getId());
            }
        }
    }

    /**
     * It adds a card ImageView to the cardsGridPane
     * @param i is the y coordinate in which the card ImageView has to be rendered
     * @param j is the x coordinate in which the card ImageView has to be rendered
     * @param cardId is the id of the CardView that is rendered
     */
    private void addImageView(int i, int j, int cardId) {
        Image cardImage = new Image("images/cards/card_" + cardId + ".png");
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        cardImageView.setId(String.valueOf(cardId));
        cardImageView.fitWidthProperty().bind(cardsGridPane.widthProperty().subtract(8*CARD_GRIDPANE_VGAP).divide(7));
        cardImageView.fitHeightProperty().bind(cardsGridPane.heightProperty().subtract(3*CARD_GRIDPANE_VGAP).divide(2));
        cardImageView.setOnMouseClicked(e -> {
            Node source = (Node)e.getSource();
            System.out.printf("Mouse enetered cell [%d, %d, cardId: %d]%n", i, j, Integer.parseInt(source.getId()));
            showInsight(Integer.parseInt(source.getId()));
        });
        cardsGridPane.add(cardImageView, j, i);
    }

    /**
     * It updates the details shown in the bottom part of the screen (bigImage and the card description)
     * with the ones of the card currently selected.
     * @param cardId is the id of the card currently selected.
     */
    private void showInsight(int cardId) {
        //updates bigCardImageView image
        currentCardId = cardId;
        Image bigCardImage = new Image("images/cards/card_" + cardId + ".png");
        bigCardImageView.setImage(bigCardImage);

        //updates card description
        for (CardView card: availableCards) {
            if (card.getId() == cardId) {
                cardDescription.setText(card.getDescription());
            }
        }
    }

    /**
     * It is activated when the user clicks on the "pick it" button.
     * It adds the card to the list of the selected card.
     */
    public void pickCard() {
        cardsSelectedList.add(currentCardId);
    }

    /**
     * It is activated when the user clicks on the "unpick it" button.
     * It removes the card from the list of the selected cards.
     */
    public void unpickCard() {
        cardsSelectedList.remove((Object)currentCardId);
    }

    /**
     * It is activated when the user clicks on the confirm button.
     * It forwards the list of selected cards to the ClientView.
     */
    public void confirm() {
        if (cardsSelectedList.size() == expectedNumberOfCards) {
            clientView.update(new InGameCardsSetMessage(cardsSelectedList));
            ((GUIEngine)clientView.getUserInterface()).showWaitingScene();
            System.out.println("The number of Cards is correct.");
        }
        else {
            //TODO: show Alert View with the following message
            System.out.println("The number of Cards is wrong.");
        }
    }

}
