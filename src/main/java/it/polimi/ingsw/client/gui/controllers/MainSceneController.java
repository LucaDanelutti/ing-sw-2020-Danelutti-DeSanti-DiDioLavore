package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.view.modelview.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.scene.image.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainSceneController extends GUIController {

    private static final double BOARD_PADDING_RATIO = 0.74;
    private static final double BOARD_PADDING_PERCENTAGE = 0.13;

    /* ===== FXML elements ===== */
    @FXML
    private GridPane mainGridPane;

    @FXML
    private GridPane boardGridPane;
    @FXML
    private AnchorPane boardAnchorPane;

    @FXML
    private Label clientPlayerNameLabel;
    @FXML
    private Label enemy1PlayerNameLabel;
    @FXML
    private Label enemy2PlayerNameLabel;

    @FXML
    private ImageView clientPlayerCardImageView;
    @FXML
    private ImageView enemy1PlayerCardImageView;
    @FXML
    private ImageView enemy2PlayerCardImageView;


    /* ===== FXML Properties ===== */
    private DoubleProperty boardPaddingPercentage = new SimpleDoubleProperty(BOARD_PADDING_PERCENTAGE);

    /* ===== FXML Set Up and Bindings ===== */
   @FXML
    public void initialize() {
       //player cards dimensions bindings
       clientPlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(6));
       clientPlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(2.5));
       enemy1PlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(12));
       enemy1PlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(5));
       enemy2PlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(12));
       enemy2PlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(5));

       //board dimensions bindings
       boardAnchorPane.setPrefWidth(mainGridPane.heightProperty().multiply(0.5).getValue());
       boardAnchorPane.setPrefHeight(mainGridPane.heightProperty().multiply(0.5).getValue());
       boardAnchorPane.maxWidthProperty().bind(mainGridPane.heightProperty().multiply(0.5));
       boardAnchorPane.maxHeightProperty().bind(boardAnchorPane.widthProperty());

       boardGridPane.paddingProperty().bind((Bindings.createObjectBinding(() -> new Insets(boardGridPane.widthProperty().multiply(BOARD_PADDING_PERCENTAGE).doubleValue()), boardGridPane.widthProperty().multiply(BOARD_PADDING_PERCENTAGE))));
       System.out.println(boardGridPane.paddingProperty());
   }


   public void buildMainScene() {
       stage.setMinWidth(Screen.getPrimary().getBounds().getWidth()/2);
       stage.setMinHeight(Screen.getPrimary().getBounds().getHeight()/2);

       updatePlayersName();
       updatePlayersCard();
       updateBoard(); //TODO: has to be remove from here
   }

   public void updateBoardTest() {
       ((GUIEngine)clientView.getUserInterface()).updateModelView();
       clientView.getUserInterface().refreshView();
       updatePlayersName();
       //TODO: RANDOM TEST HERE, IT HAS TO BE REMOVED ASAP
       clientView.getUserInterface().onNumberOfPlayersRequest();
   }


    /**
     * clears the boardGridPane and renders the updated board from the modelView loading first the block types imageViews
     * and then the pawns imageViews
     */
   public void updateBoard() {
       boardGridPane.getChildren().clear();
       ModelView modelView = clientView.getModelView();
       for(int i = 0; i < modelView.getMatrix().length; i++){
           for(int  j = 0; j < modelView.getMatrix()[0].length; j++){
               if (modelView.getMatrix()[i][j].getPeek() != BlockType.TERRAIN) {
                   System.out.println("blockType: " + modelView.getMatrix()[i][j].getPeek().getLevel());
                   Image blockImage = new Image("images/board/block_lv_" + modelView.getMatrix()[i][j].getPeek().getLevel() +".png");
                   ImageView blockImageView = new ImageView(blockImage);
                   blockImageView.setPreserveRatio(true);
                   blockImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(5).multiply(BOARD_PADDING_RATIO));
                   blockImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(5).multiply(BOARD_PADDING_RATIO));
                   boardGridPane.add(blockImageView, i, j);
               }
           }
       }
       updatePawns();
   }

   private void updatePawns() {
       ModelView modelView = clientView.getModelView();
       ArrayList<PawnView> pawnsList = modelView.getPawns();
       for (PawnView pawn: pawnsList) {
           Image pawnImage = new Image("images/board/pawn_" + pawn.getColor() + ".png");
           ImageView pawnImageView = new ImageView(pawnImage);
           pawnImageView.setPreserveRatio(true);
           pawnImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(5).multiply(BOARD_PADDING_RATIO));
           pawnImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(5).multiply(BOARD_PADDING_RATIO));
           boardGridPane.add(pawnImageView, pawn.getPawnPosition().getX(), pawn.getPawnPosition().getY());
       }

   }

   public void updatePlayersName() {
       ModelView modelView = clientView.getModelView();
       clientPlayerNameLabel.setText(clientView.getName());
       ArrayList<String> enemiesNames = modelView.getEnemiesNames(clientView.getName());
       if (enemiesNames != null) {
           if (enemiesNames.size() >= 1) enemy1PlayerNameLabel.setText(enemiesNames.get(0));
           if (enemiesNames.size() == 2) enemy2PlayerNameLabel.setText(enemiesNames.get(1));
       }
   }

   public void updatePlayersCard() {
       ModelView modelView = clientView.getModelView();

       //gets the Card of the client player from the modelView and renders the proper Image, Name and Description
       CardView clientPlayerCard = modelView.getClientPlayerCard(clientView.getName());
       if (clientPlayerCard != null) {
           Image clientPlayerCardImage = new Image("images/cards/card_" + clientPlayerCard.getId() + ".png");
           clientPlayerCardImageView.setImage(clientPlayerCardImage);
       }
       ArrayList<CardView> enemiesCards = modelView.getEnemiesCards(clientView.getName());
       if (enemiesCards != null) {
           if (enemiesCards.size() >= 1)  {
               Image enemy1CardImage = new Image("images/cards/card_" + enemiesCards.get(0).getId() + ".png");
               enemy1PlayerCardImageView.setImage(enemy1CardImage); //enemiesCards.get(0)
           }
           if (enemiesCards.size() == 2) {
               Image enemy2CardImage = new Image("images/cards/card_" + enemiesCards.get(1).getId() + ".png");
               enemy2PlayerCardImageView.setImage(enemy2CardImage);
           }
       }
   }


}
