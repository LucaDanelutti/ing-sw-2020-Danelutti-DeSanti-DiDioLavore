package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.NumberOfPlayersSetMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NumberOfPlayersRequestSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private Button button2Players;
    @FXML
    private Button button3Players;


    /* ===== FXML Properties ===== */
    private int selectedNumberOfPlayers = 2;

    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {
        button2Players.setStyle("-fx-background-color: #99ccff; ");
        button3Players.setStyle("-fx-background-color: #ffffff; ");

    }

    public void choose2Players() {
        button2Players.setStyle("-fx-background-color: #99ccff; ");
        button3Players.setStyle("-fx-background-color: #ffffff; ");
        selectedNumberOfPlayers = 2;

    }

    public void choose3Players() {
        button3Players.setStyle("-fx-background-color: #99ccff; ");
        button2Players.setStyle("-fx-background-color: #ffffff; ");
        selectedNumberOfPlayers = 3;
    }

    public void confirm() {
        System.out.println("selectedNumberOfPlayers:" + selectedNumberOfPlayers);
//        TODO: togliere commento, chiama la Set correttamente
//        clientView.update(new NumberOfPlayersSetMessage(selectedNumberOfPlayers));

        ((GUIEngine)clientView.getUserInterface()).showWaitingScene();
    }


}