package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.FirstPlayerSetMessage;
import it.polimi.ingsw.view.modelview.ModelView;
import it.polimi.ingsw.view.modelview.PlayerView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class FirstPlayerRequestSceneController extends GUIController {

    @FXML
    private Button buttonPlayer1;
    @FXML
    private Button buttonPlayer2;
    @FXML
    private Button buttonPlayer3;


    ArrayList<String> playersList = new ArrayList<>();
    String firstPlayerSelected;

    @FXML
    public void initialize() {

    }

    public void loadPlayers() {
        ModelView modelView = clientView.getModelView();
        playersList.add(clientView.getName());
        playersList.addAll(modelView.getEnemiesNames(clientView.getName()));
        buttonPlayer1.setText(playersList.get(0));
        buttonPlayer2.setText(playersList.get(1));
        if (playersList.size() < 3)  {
            buttonPlayer3.setVisible(false);
        } else {
            buttonPlayer3.setText(playersList.get(2));
        }
        selectPlayer1();
    }

    public void selectPlayer1() {
        firstPlayerSelected = playersList.get(0);
        buttonPlayer1.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer1.getStyleClass().add("selectedDarkButton");
        buttonPlayer2.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer2.getStyleClass().add("classicButton");
        buttonPlayer3.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer3.getStyleClass().add("classicButton");
    }

    public void selectPlayer2() {
        firstPlayerSelected = playersList.get(1);
        buttonPlayer1.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer1.getStyleClass().add("classicButton");
        buttonPlayer2.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer2.getStyleClass().add("selectedDarkButton");
        buttonPlayer3.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer3.getStyleClass().add("classicButton");
    }

    public void selectPlayer3() {
        firstPlayerSelected = playersList.get(2);
        buttonPlayer1.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer1.getStyleClass().add("classicButton");
        buttonPlayer2.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer2.getStyleClass().add("classicButton");
        buttonPlayer3.getStyleClass().removeAll("selectedDarkButton", "classicButton");
        buttonPlayer3.getStyleClass().add("selectedDarkButton");
    }

    public void confirm() {
        System.out.println("firstPlayerSelected: " + firstPlayerSelected);
        clientView.update(new FirstPlayerSetMessage(firstPlayerSelected));
        ((GUIEngine)clientView.getUserInterface()).showMainScene();
    }

}
