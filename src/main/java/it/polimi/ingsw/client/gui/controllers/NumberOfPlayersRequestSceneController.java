package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.NumberOfPlayersSetMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NumberOfPlayersRequestSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private Button button2Players;
    @FXML
    private Button button3Players;
    @FXML
    private ImageView players2ImageView;
    @FXML
    private ImageView players3ImageView;



    /* ===== FXML Properties ===== */
    private int selectedNumberOfPlayers = 2;

    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {
        button2Players.setStyle("-fx-background-color: #99ccff; ");
        button3Players.setStyle("-fx-background-color: #ffffff; ");

        Image players2Image = new Image("images/utility/2players_game.png");
        players2ImageView.setImage(players2Image);

        Image players3Image = new Image("images/utility/3players_game.png");
        players3ImageView.setImage(players3Image);
    }

    /**
     * It is activated when the button "2" is clicked and temporarily saves the player choice
     */
    public void choose2Players() {
        button2Players.setStyle("-fx-background-color: #99ccff; ");
        button3Players.setStyle("-fx-background-color: #ffffff; ");
        selectedNumberOfPlayers = 2;

    }

    /**
     * It is activated when the button "3" is clicked and temporarily saves the player choice
     */
    public void choose3Players() {
        button3Players.setStyle("-fx-background-color: #99ccff; ");
        button2Players.setStyle("-fx-background-color: #ffffff; ");
        selectedNumberOfPlayers = 3;
    }

    /**
     * It is activated when the player clicks on the confirm button.
     * It forwards the selected number of players to the ClientView
     */
    public void confirm() {
        System.out.println("selectedNumberOfPlayers:" + selectedNumberOfPlayers);
        clientView.update(new NumberOfPlayersSetMessage(selectedNumberOfPlayers));

        ((GUIEngine)clientView.getUserInterface()).showWaitingScene(true);
    }


}
