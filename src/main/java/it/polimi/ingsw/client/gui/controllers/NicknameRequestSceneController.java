package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.utility.messages.sets.NicknameSetMessage;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class NicknameRequestSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private Label titleLabel;
    @FXML
    private TextField nicknameTextField;

    /* ===== FXML Properties ===== */
    private StringProperty nickname;
    final private StringProperty initialTitle = new SimpleStringProperty("Hi ");
    final private StringProperty endTitle = new SimpleStringProperty("!");

    @FXML
    public void initialize() {
        nickname = new SimpleStringProperty("");
        titleLabel.textProperty().bind(initialTitle.concat(nicknameTextField.textProperty()).concat(endTitle));
        nicknameTextField.textProperty().bindBidirectional(nickname);
    }

    /**
     * It is activated when the player clicks on the confirm button.
     * It forwards the selected nickname to the ClientView
     */
    public void confirm() {
        System.out.println("nickname:" + nickname.getValue());
        clientView.update(new NicknameSetMessage(nickname.getValue()));
        //loads the WaitingScene
        ((GUIEngine)clientView.getUserInterface()).showWaitingScene(true);
    }
}
