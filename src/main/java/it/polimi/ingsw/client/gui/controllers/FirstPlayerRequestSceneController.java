package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.FirstPlayerSetMessage;
import it.polimi.ingsw.view.modelview.ModelView;
import it.polimi.ingsw.view.modelview.PlayerView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class FirstPlayerRequestSceneController extends GUIController {

    /* ===== Constants ===== */
    final static private double GOD_PAIN_RATIO = 6;


    /* ===== FXML elements ===== */
    @FXML
    private Button buttonPlayer1;
    @FXML
    private Button buttonPlayer2;
    @FXML
    private Button buttonPlayer3;

    @FXML
    private ImageView player1ImageView;
    @FXML
    private ImageView player2ImageView;
    @FXML
    private ImageView player3ImageView;

    @FXML
    private GridPane mainGridPane;


    /* ===== Variables ===== */
    ArrayList<String> playersList = new ArrayList<>();
    String firstPlayerSelected;


    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {

        player1ImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(GOD_PAIN_RATIO));
        player1ImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(GOD_PAIN_RATIO));
        player2ImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(GOD_PAIN_RATIO));
        player2ImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(GOD_PAIN_RATIO));
        player3ImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(GOD_PAIN_RATIO));
        player3ImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(GOD_PAIN_RATIO));

    }

    /**
     * Loads the names and gods images into the scene.
     */
    public void loadPlayers() {
        ModelView modelView = clientView.getModelView();
        playersList.add(clientView.getName());
        playersList.addAll(modelView.getEnemiesNames(clientView.getName()));
        buttonPlayer1.setText(playersList.get(0));
        Image player1Image = new Image("images/gods/god_" + modelView.getClientPlayerCard(clientView.getName()).getId() + ".png");
        player1ImageView.setImage(player1Image);

        buttonPlayer2.setText(playersList.get(1));
        Image player2Image = new Image("images/gods/god_" + modelView.getEnemiesCards(clientView.getName()).get(0).getId() + ".png");
        player2ImageView.setImage(player2Image);

        if (playersList.size() < 3)  {
            buttonPlayer3.setVisible(false);
        } else {
            buttonPlayer3.setText(playersList.get(2));
            Image player3Image = new Image("images/gods/god_" + modelView.getEnemiesCards(clientView.getName()).get(1).getId() + ".png");
            player3ImageView.setImage(player3Image);
        }
        selectPlayer1();
    }

    /**
     * It is activated when the user clicks on a player's name button.
     * It updates the style of the UI so as to give a feedback of the selection and sets the firstPlayerSelected.
     */
    public void selectPlayer1() {
        firstPlayerSelected = playersList.get(0);
        buttonPlayer1.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer1.getStyleClass().add("selectedDarkButton");
        buttonPlayer2.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer2.getStyleClass().add("classicButton");
        buttonPlayer3.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer3.getStyleClass().add("classicButton");
    }

    /**
     * It is activated when the user clicks on a player's name button.
     * It updates the style of the UI so as to give a feedback of the selection and sets the firstPlayerSelected.
     */
    public void selectPlayer2() {
        firstPlayerSelected = playersList.get(1);
        buttonPlayer1.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer1.getStyleClass().add("classicButton");
        buttonPlayer2.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer2.getStyleClass().add("selectedDarkButton");
        buttonPlayer3.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer3.getStyleClass().add("classicButton");
    }

    /**
     * It is activated when the user clicks on a player's name button.
     * It updates the style of the UI so as to give a feedback of the selection and sets the firstPlayerSelected.
     */
    public void selectPlayer3() {
        firstPlayerSelected = playersList.get(2);
        buttonPlayer1.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer1.getStyleClass().add("classicButton");
        buttonPlayer2.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer2.getStyleClass().add("classicButton");
        buttonPlayer3.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer3.getStyleClass().add("selectedDarkButton");
    }

    /**
     * It is activated when the user clicks on the confirm button.
     * It forwards the name of the player selected as firstPlayerSelected to the ClientView.
     */
    public void confirm() {
        System.out.println("firstPlayerSelected: " + firstPlayerSelected);
        clientView.update(new FirstPlayerSetMessage(firstPlayerSelected));
        ((GUIEngine)clientView.getUserInterface()).showMainScene();
    }

}
