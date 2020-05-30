package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.modelview.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.scene.image.ImageView;

import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainSceneController extends GUIController {

    /* ===== Constants ===== */
    private static final int BOARD_SIZE = 5;
    private static final double CLIENT_CARD_WIDTH_RATIO = 6.5;
    private static final double CLIENT_CARD_HEIGHT_RATIO = 3;
    private static final double ENEMY_CARD_WIDTH_RATIO = 12;
    private static final double ENEMY_CARD_HEIGHT_RATIO = 5;
    private static final double BOARD_PANE_RATIO = 0.5;
    private static final double BOARD_BACKGROUND_RATIO = 1.36;
    private static final double BLOCK_CELL_RATIO = 0.86;


    /* ===== FXML elements ===== */
    @FXML
    private Label phaseLabel;
    @FXML
    private HBox blockTypesHBox;
    @FXML
    private GridPane mainGridPane;

    @FXML
    private GridPane boardGridPane;
    @FXML
    private AnchorPane boardAnchorPane;
    @FXML
    private ImageView boardImageView;

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

    @FXML
    private Button skipButton;

    @FXML
    private ImageView timerImageView;
    @FXML
    private Button confirmActionButton;
    @FXML
    private Button undoActionButton;
    @FXML
    private Button undoTurnButton;


    /* ===== Variables ===== */
    private ImageView[][] enlightenedImageViewsArray = new ImageView[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<Position> initialPawnPositionsList = new ArrayList<>();
    private Position chosenPosition = new Position(-1,-1);
    private BlockType chosenBlockType = null;
    private Timer undoProcessTimer;


    /* ===== FXML Set Up and Bindings ===== */
   @FXML
    public void initialize() {
       //graphical elements initialization
       phaseLabel.setText("");
       clientPlayerNameLabel.setText("");
       enemy1PlayerNameLabel.setText("");
       enemy2PlayerNameLabel.setText("");
       skipButton.setVisible(false);
       timerImageView.setVisible(false);
       timerImageView.setPreserveRatio(true);
       confirmActionButton.setVisible(false);
       undoActionButton.setVisible(false);
       undoTurnButton.setVisible(false);

        //board background image settings
       Image boardImage = new Image("images/board/board.png");
       boardImageView.setImage(boardImage);
       boardImageView.fitWidthProperty().bind(boardGridPane.widthProperty().multiply(BOARD_BACKGROUND_RATIO));
       boardImageView.fitHeightProperty().bind(boardGridPane.widthProperty().multiply(BOARD_BACKGROUND_RATIO));

       //player cards dimensions bindings
       clientPlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(CLIENT_CARD_WIDTH_RATIO));
       clientPlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(CLIENT_CARD_HEIGHT_RATIO));
       enemy1PlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(ENEMY_CARD_WIDTH_RATIO));
       enemy1PlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(ENEMY_CARD_HEIGHT_RATIO));
       enemy2PlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(ENEMY_CARD_WIDTH_RATIO));
       enemy2PlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(ENEMY_CARD_HEIGHT_RATIO));

       //board dimensions bindings
       boardAnchorPane.setPrefWidth(mainGridPane.heightProperty().multiply(BOARD_PANE_RATIO).getValue());
       boardAnchorPane.setPrefHeight(mainGridPane.heightProperty().multiply(BOARD_PANE_RATIO).getValue());
       boardAnchorPane.maxWidthProperty().bind(mainGridPane.heightProperty().multiply(BOARD_PANE_RATIO));
       boardAnchorPane.maxHeightProperty().bind(boardAnchorPane.widthProperty());

       blockTypesHBox.spacingProperty().bind(blockTypesHBox.widthProperty().divide(10));
   }

    /**
     * Sets up the main scene loading the elements from the ModelView.
     */
   public void buildMainScene() {
       updatePlayersName();
       updatePlayersCard();
       updateBoard();
       loadEnlightenedImageViews();
   }

    /**
     * Updates the data regarding with the cards and players names rendered.
     */
   public void updateGameInfo() {
       updatePlayersName();
       updatePlayersCard();
   }


    /**
     * Checks if a player has lost or if is without any pawn.
     * @param playerName is the name of the player
     * @return if the condition described hereabove is true or false
     */
   private boolean playerIsStillPlaying(String playerName) {
       boolean stillPlaying = true;
       ModelView modelView = clientView.getModelView();
       if (modelView.hasPlayerLost(playerName)) return false;
       for (PlayerView player : modelView.getPlayerList()) {
           if (player.getName().equals(playerName)) {
               if (player.getPawnList().isEmpty()) return false;
           }
       }
       return stillPlaying;
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
//                   System.out.println("blockType: " + modelView.getMatrix()[i][j].getPeek().getLevel());
                   Image blockImage;
                   if (modelView.getMatrix()[i][j].getPeek().getLevel() == 4 && modelView.getMatrix()[i][j].getSize() == 2) {
                       //in this case there is a dome on the terrain
                       blockImage = new Image("images/board/block_lv_4terrain.png");
                   } else {
                       blockImage = new Image("images/board/block_lv_" + modelView.getMatrix()[i][j].getPeek().getLevel() +".png");
                   }
                   ImageView blockImageView = new ImageView(blockImage);
                   blockImageView.setPreserveRatio(true);
                   blockImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(BOARD_SIZE).multiply(BLOCK_CELL_RATIO));
                   blockImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(BOARD_SIZE).multiply(BLOCK_CELL_RATIO));
                   boardGridPane.add(blockImageView, j, i);
               }
           }
       }
       updatePawns();
       //If the players is still in game it load the EnlightenedImageViews
       if (playerIsStillPlaying(clientView.getName())) {
           loadEnlightenedImageViews();
       }
   }

    /**
     * Updates the presence and position of the pawns images within the board.
     */
   private void updatePawns() {
       ModelView modelView = clientView.getModelView();
       ArrayList<PawnView> pawnsList = modelView.getPawns();
       for (PawnView pawn: pawnsList) {
           if (pawn.getPawnPosition() != null) {
               Image pawnImage = new Image("images/board/pawn_" + pawn.getColor() + ".png");
               ImageView pawnImageView = new ImageView(pawnImage);
               pawnImageView.setPreserveRatio(true);
               pawnImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(BOARD_SIZE).multiply(BLOCK_CELL_RATIO));
               pawnImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(BOARD_SIZE).multiply(BLOCK_CELL_RATIO));
               boardGridPane.add(pawnImageView, pawn.getPawnPosition().getY(), pawn.getPawnPosition().getX());
           }
       }

   }

    /**
     * Updates the texts of the labels showing the players names.
     */
   public void updatePlayersName() {
       ModelView modelView = clientView.getModelView();

       //if the player has lost hides the label
       if (!playerIsStillPlaying(clientView.getName())) {
           clientPlayerNameLabel.setVisible(false);
       } else {
           clientPlayerNameLabel.setText(clientView.getName());
           clientPlayerNameLabel.getStyleClass().add(modelView.getPlayerColor(clientView.getName()) + "LabelBackground");
       }

       ArrayList<String> enemiesNames = modelView.getEnemiesNames(clientView.getName());
       if (enemiesNames != null) {
           if (enemiesNames.size() >= 1)  {
               if (!playerIsStillPlaying(enemiesNames.get(0))) {
                   enemy1PlayerNameLabel.setVisible(false);
               } else {
                   enemy1PlayerNameLabel.setText(enemiesNames.get(0));
                   enemy1PlayerNameLabel.getStyleClass().add(modelView.getPlayerColor(enemiesNames.get(0)) + "LabelBackground");
               }
           }
           if (enemiesNames.size() == 2) {
               if (!playerIsStillPlaying(enemiesNames.get(1))) {
                   enemy2PlayerNameLabel.setVisible(false);
               } else {
                   enemy2PlayerNameLabel.setText(enemiesNames.get(1));
                   enemy2PlayerNameLabel.getStyleClass().add(modelView.getPlayerColor(enemiesNames.get(1)) + "LabelBackground");
               }
           }
       }
   }

    /**
     * Updates the ImageViews showing the players cards.
     */
   public void updatePlayersCard() {
       ModelView modelView = clientView.getModelView();

       //gets the Card of the client player from the modelView and renders the proper Image, Name and Description
       CardView clientPlayerCard = modelView.getClientPlayerCard(clientView.getName());
       if (clientPlayerCard != null) {
           if (!playerIsStillPlaying(clientView.getName())) {
               clientPlayerCardImageView.setVisible(false);
           } else {
               Image clientPlayerCardImage = new Image("images/cards/card_" + clientPlayerCard.getId() + ".png");
               clientPlayerCardImageView.setImage(clientPlayerCardImage);
           }
       }

       ArrayList<String> enemiesNames = modelView.getEnemiesNames(clientView.getName());
       ArrayList<CardView> enemiesCards = modelView.getEnemiesCards(clientView.getName());
       if (enemiesCards != null) {
           if (enemiesCards.size() >= 1 && enemiesCards.get(0) != null)  {
               if (!playerIsStillPlaying(enemiesNames.get(0))) {
                   enemy1PlayerCardImageView.setVisible(false);
               } else {
                   Image enemy1CardImage = new Image("images/cards/card_" + enemiesCards.get(0).getId() + ".png");
                   enemy1PlayerCardImageView.setImage(enemy1CardImage);
               }
           }
           if (enemiesCards.size() == 2 && enemiesCards.get(0) != null && enemiesCards.get(1) != null) {
               if (!playerIsStillPlaying(enemiesNames.get(1))) {
                   enemy2PlayerCardImageView.setVisible(false);
               } else {
                   Image enemy2CardImage = new Image("images/cards/card_" + enemiesCards.get(1).getId() + ".png");
                   enemy2PlayerCardImageView.setImage(enemy2CardImage);
               }
           }
       }
   }

    /**
     * Loads the ImageViews used to show the available/clickable cells of the board to the player.
     */
   private void loadEnlightenedImageViews() {
       ModelView modelView = clientView.getModelView();
       String playerColor = modelView.getPlayerColor(clientView.getName());
       //loads the panes that will be used to enlighten the board cells
       for (int i = 0; i < BOARD_SIZE; i++) {
           for (int j = 0; j < BOARD_SIZE; j++) {
               Image enlightenedImage = new Image("images/board/enlightened_cell_" + playerColor +".png");
               ImageView enlightenedImageView = new ImageView(enlightenedImage);
               enlightenedImageView.setOpacity(0.7);
               enlightenedImageViewsArray[i][j] = enlightenedImageView;
               enlightenedImageView.setId("permanent");
               enlightenedImageView.setPreserveRatio(true);
               enlightenedImageView.setVisible(false);
               enlightenedImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(BOARD_SIZE).multiply(BLOCK_CELL_RATIO));
               enlightenedImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(BOARD_SIZE).multiply(BLOCK_CELL_RATIO));
               boardGridPane.add(enlightenedImageView, j, i);
           }
       }
   }

    /**
     * Enlightens and makes clickable the cells containing the player pawns.
     * @param availablePositions is the list of the clickable positions.
     */
   public void enablePawnSelection(ArrayList<Position> availablePositions) {
       phaseLabel.setText("Select one of your pawns!");
       for (Position position : availablePositions) {
           //makes the ImageViews visible
           enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(true);
           enlightenedImageViewsArray[position.getX()][position.getY()].toFront();
           //adds an action recognizer to the ImageView
           enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(e -> {
               Node source = (Node)e.getSource();
               int colIndex = GridPane.getColumnIndex(source);
               int rowIndex = GridPane.getRowIndex(source);
               //cols -> x, rows -> y
               chosenPawn(new Position(rowIndex, colIndex));
           });
       }
   }

    /**
     * It is activated when the player clicks on a proposed cell containing one of his pawns so as to select it.
     * @param chosenPawnPosition is the position of the chosen pawn.
     */
   private void chosenPawn(Position chosenPawnPosition) {
       phaseLabel.setText("");
       System.out.println("chosenPawnPosition:" + chosenPawnPosition.getX() + " " + chosenPawnPosition.getY());
       clientView.update(new SelectedPawnSetMessage(chosenPawnPosition));
       clearEnlightenedImageViews();
   }

    /**
     * Makes invisible the available cells on the board and makes them unclickable.
     */
    private void clearEnlightenedImageViews() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                enlightenedImageViewsArray[i][j].setVisible(false);
                enlightenedImageViewsArray[i][j].setOnMouseClicked(null);
            }
        }
    }

    /**
     * Makes unclickable the available cells on the board while keeping them visible.
     */
    private void removeRecognizerFromEnlightenedImageViews() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                enlightenedImageViewsArray[i][j].setOnMouseClicked(null);
            }
        }
    }

    /**
     * Lets the player know that he has to select a position so as to move his pawn.
     * @param availablePositions is the list of the positions from which the player can choose.
     */
    public void chooseMovePosition(ArrayList<Position> availablePositions) {
       phaseLabel.setText("Choose a position to Move!");
       if (availablePositions.contains(null)) skipButton.setVisible(true);
       enablePositionSelection(availablePositions, "move");
       if (availablePositions.size()==0) resetTurn();
    }

    /**
     * Lets the player know that he has to select a position so as to build a block.
     * @param availablePositions is the list of the positions from which the player can choose.
     */
    public void chooseConstructPosition(ArrayList<Position> availablePositions) {
       phaseLabel.setText("Choose a position to Build!");
       if (availablePositions.contains(null)) skipButton.setVisible(true);
       enablePositionSelection(availablePositions, "construct");
    }

    /**
     * Shows the available cells enlightening them and making them clickable.
     * @param availablePositions is the list of the cells that can be clicked by the player.
     * @param actionType is the type of action. It can be "move" or "construct".
     */
    public void enablePositionSelection(ArrayList<Position> availablePositions, String actionType) {
        for (Position position : availablePositions) {
            if (position != null) {
                //makes the ImageViews visible
                enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(true);
                //adds an action recognizer to the ImageView
                enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(e -> {
                    Node source = (Node)e.getSource();
                    int colIndex = GridPane.getColumnIndex(source);
                    int rowIndex = GridPane.getRowIndex(source);

                    chosenPosition.setX(rowIndex);
                    chosenPosition.setY(colIndex);
                    skipButton.setVisible(false);

                    if (actionType.equals("move")) {
                        startUndoProcess();
                    } else {
                        setPosition();
                    }
                });
            }
        }
    }

    /**
     * Sends to the ClientView the position chosen by the player.
     */
    private void setPosition() {
        phaseLabel.setText("");
        if (chosenPosition != null) {
            System.out.println("chosenPosition:" + chosenPosition.getX() + " " + chosenPosition.getY());
        } else {
            System.out.println("Action Skipped");
        }
        System.out.println("setPosition - chosenPosition:" + chosenPosition);
        clientView.update(new ChosenPositionSetMessage(chosenPosition));
        chosenPosition = new Position(-1,-1);
        clearEnlightenedImageViews();

        skipButton.setVisible(false);
    }

    /**
     * Lets the player know that he has to select a block type and shows a list of the available ones.
     * @param availableBlockTypes is the list of the available block types that have to be shown to the player.
     */
    public void chooseBlockType(ArrayList<BlockType> availableBlockTypes) {
        phaseLabel.setText("Choose a Block Type!");
        blockTypesHBox.toFront();

        for (BlockType blockType : availableBlockTypes) {
            Image blockTypeImage;
            if (blockType.getLevel() == 4)  {
                blockTypeImage = new Image("images/board/block_lv_4terrain.png");
            } else {
                blockTypeImage = new Image("images/board/block_lv_" + blockType.getLevel() + ".png");
            }
            ImageView blockTypeImageView = new ImageView(blockTypeImage);
            blockTypeImageView.setPreserveRatio(true);
            blockTypeImageView.setId(String.valueOf(blockType.getLevel()));
            blockTypeImageView.fitWidthProperty().bind(blockTypesHBox.widthProperty().divide(availableBlockTypes.size()));
            blockTypeImageView.fitHeightProperty().bind(blockTypesHBox.heightProperty().multiply(0.7));
            blockTypesHBox.getChildren().add(blockTypeImageView);
            blockTypeImageView.setOnMouseClicked(e -> {
                Node source = (Node)e.getSource();
                int blockTypeLevel = Integer.parseInt(source.getId());
                System.out.printf("selected blockType: %d %n", blockTypeLevel);

                chosenBlockType = BlockType.LEVEL1;
                for(BlockType blockTypeElement : availableBlockTypes) {
                    if (blockTypeElement.getLevel() == blockTypeLevel) chosenBlockType = blockTypeElement;
                }

                blockTypesHBox.getChildren().clear();
                blockTypesHBox.setVisible(false);
                blockTypesHBox.toBack();
                startUndoProcess();
            });
        }

        blockTypesHBox.setVisible(true);
    }

    /**
     * Sends to the ClientView the block type chosen by the player.
     */
    private void setBlockType() {
       phaseLabel.setText("");

       System.out.println("chosenBlockType.getLevel(): " + chosenBlockType.getLevel());
       clientView.update(new ChosenBlockTypeSetMessage(chosenBlockType));
       chosenBlockType = null;
    }

    /**
     * Lets the player know that he has to place both his pawns at the beginning to the game.
     * It enlightens and makes visible a list of the cells that can be clicked by the player.
     * @param availablePositions is the list of the positions in which the player can place a pawn.
     */
    public void placeInitialPawns(ArrayList<Position> availablePositions) {
        phaseLabel.setText("Place your 2 pawns!");
        //TODO: questa parte di codice è condivisa con enablePositionSelection. Potrei scrivere un metodo a cui si passa la funzione da attivare con l'action.
        for (Position position : availablePositions) {
            //makes the ImageViews visible
            enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(true);
            //adds an action recognizer to the ImageView
            enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(e -> {
                Node source = (Node)e.getSource();
                int colIndex = GridPane.getColumnIndex(source);
                int rowIndex = GridPane.getRowIndex(source);
                //cols -> x, rows -> y
                selectInitialPawnPosition(new Position(rowIndex, colIndex));
            });
        }
    }

    /**
     * Sends to the ClientView a position chosen by the player to place his pawn at the beginning of the match.
     * @param position is a position chosen by the place to place one of his pawns.
     */
    private void selectInitialPawnPosition(Position position) {
       ModelView modelView = clientView.getModelView();
       ArrayList<Integer> pawnsId = modelView.getPlayerPawnsId(clientView.getName());

       initialPawnPositionsList.add(position);
       if (initialPawnPositionsList.size() == 2) {
           phaseLabel.setText("");
           clearEnlightenedImageViews();
           System.out.println("firstPawnPosition1:" + initialPawnPositionsList.get(0).getX() + " " + initialPawnPositionsList.get(0).getY());
           System.out.println("firstPawnPosition2:" + initialPawnPositionsList.get(1).getX() + " " + initialPawnPositionsList.get(1).getY());
           clientView.update(new InitialPawnPositionSetMessage(pawnsId.get(0), pawnsId.get(1), initialPawnPositionsList.get(0), initialPawnPositionsList.get(1)));
       } else {
           phaseLabel.setText("Place the pawn left!");
           enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(false);
           enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(null);
       }
    }

    /**
     * Shows a button that, if clicked, lets the player skip the current optional action.
     */
    public void skipAction() {
        phaseLabel.setText("");
        skipButton.setVisible(false);
        chosenPosition = null;
        startUndoProcess();
    }

    /**
     * Manages a timer which controls the undo operation.
     * At the end of a 5 seconds countdown the action previously selected by the player is confirmed.
     * During these seconds some buttons that let him undo the action or the turn are shown to the player
     */
    private void startUndoProcess() {
       phaseLabel.setText("");
        removeRecognizerFromEnlightenedImageViews();
        Image timerImage = new Image("images/utility/timer_counter_5.png");
        timerImageView.setImage(timerImage);

        timerImageView.setVisible(true);
        confirmActionButton.setVisible(true);
        undoActionButton.setVisible(true);
        undoTurnButton.setVisible(true);

        undoProcessTimer  = new Timer();

        undoProcessTimer.schedule( new TimerTask() {
            private int count = 5;
            @Override
            public void run() {
                if (count > 0) {
                    Platform.runLater(() -> {

                        Image updatedTimerImage =  new Image("images/utility/timer_counter_" + count + ".png");
                        timerImageView.setImage(updatedTimerImage);
                        count--;
                    });
                } else {
                    Platform.runLater(() -> {
                        confirmAction();
                        undoProcessTimer.cancel();
                    });
                }
            }
        }, 0, 1000);
    }

    /**
     * Deletes the times that manages the undo process and makes invisible the relative buttons.
     */
    private void endUndoProcess() {
        undoProcessTimer.cancel();
        timerImageView.setVisible(false);
        confirmActionButton.setVisible(false);
        undoActionButton.setVisible(false);
        undoTurnButton.setVisible(false);
    }

    /**
     * Confirm the action previously selected and ends the undo process.
     */
    public void confirmAction() {
       if (chosenBlockType != null) {
           setBlockType();
       } else {
           setPosition();
       }
        clearEnlightenedImageViews();
        endUndoProcess();
    }

    /**
     * Sends to the ClientView a UndoActionSetMessage thus making the player repeat his last action.
     */
    public void undoAction() {
       clientView.update(new UndoActionSetMessage());
       chosenPosition = new Position(-1,-1);
       chosenBlockType = null;
       endUndoProcess();
    }

    /**
     * Sends to the ClientView a UndoTurnSetMessage thus making the player repeat his last turn.
     */
    public void undoTurn() {
       clientView.update(new UndoTurnSetMessage());
       chosenPosition = new Position(-1,-1);
       chosenBlockType = null;
       endUndoProcess();
    }

    /**
     * Lets the player know that he has to repeat his turn and after 3 seconds
     * sends to the ClientView a UndoTurnSetMessage thus making the player repeat his last turn.
     */
    private void resetTurn() {
        phaseLabel.setText("Pawn blocked. Select another pawn!");

        undoProcessTimer  = new Timer();
        undoProcessTimer.schedule( new TimerTask() {
            private int count = 3;
            @Override
            public void run() {
                if (count > 0) {
                    System.out.println("count: " + count);
                    Platform.runLater(() -> {
                        count--;
                    });
                } else {
                    Platform.runLater(() -> {
                        clientView.update(new UndoTurnSetMessage());
                        undoProcessTimer.cancel();
                    });
                }
            }
        }, 0, 1000);
    }

    public void updateLostPhaseLabel() {
        phaseLabel.setText("You Lost, but you can still watch!");
    }

}
