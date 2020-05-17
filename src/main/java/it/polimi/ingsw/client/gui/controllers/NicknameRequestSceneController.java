package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.NicknameSetMessage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class NicknameRequestSceneController extends GUIController {
    @FXML
    private TextField nicknameTextField;

    private StringProperty nickname;

    @FXML
    public void initialize() {
        nickname = new SimpleStringProperty("");
        nicknameTextField.textProperty().bindBidirectional(nickname);
    }

    /**
     * It is activated when the player clicks on the confirm button.
     * It forwards the selected nickname to the ClientView
     */
    public void confirm() {
        System.out.println("nickname:" + nickname.getValue());
//        clientView.update(new NicknameSetMessage(nickname.getValue())); //TODO: togliere commento, è il Set corretto
        //TODO: capire se deve anche settarlo nella ClientView
        //loads the WaitingScene
        ((GUIEngine)clientView.getUserInterface()).showWaitingScene();
    }
}
