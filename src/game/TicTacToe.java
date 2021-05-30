package game;

import ai.MiniMaxAlgorithm;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class TicTacToe extends Application {

    /* *********************************************************
     * Define JavaFX-elements as well as UI-variables
     ********************************************************* */

    //JavaFX-elements for game-scene
    private static GridPane gameBoard;
    private static Board board;
    private AnimationTimer gameTimer;
    private MenuBar menuBar;
    private Menu gameMenu;
    private MenuItem resetGameOption;
    private MenuItem toMainMenuOption;
    private BorderPane gamelayout;

    //JavaFX-elements for menu-scene
    private VBox menulayout = new VBox(20);
    private Label title = new Label("TicTacToe");
    private Label instruction = new Label("Choose a game-mode:");
    private Button bEasy = new Button("Easy");
    private Button bMedium = new Button("Medium");
    private Button bHard = new Button("Hard");
    private Button explanation = new Button("Settings");
    private Button endG = new Button("End Game");
    private Font eightBitFont = Font.loadFont("file:res/game/SuperLegendBoy-4w8Y.ttf", 45);

    //JavaFX-elements and variables for settings-scene
    private VBox settingslayout = new VBox(20);
    private Label settingsTitle = new Label("TicTacToe");
    private Label settingsMainText = new Label("You are O\nThe computer is X\n\nTicTacToe is a paper-and-pencil game for two players, X and O, who take turns marking the spaces in a 3x3 grid. The player who succeeds in placing three of their marks in a diagonal, horizontal, or vertical row is the winner.");
    private Button bToMainMenu = new Button("To main menu");
    private Button triggerDarkMode = new Button("DO NOT PRESS!");
    private Boolean isDark = false;
    private Slider volumeSlider = new Slider(0, 1, 0);
    private Label volumeInstructions = new Label("Volume:");

    //Variables and constants for the different game modes
    private static final String EASYGAME = "eg";
    private static final String MEDIUMGAME = "mg";
    private static final String HARDGAME = "hg";
    private String gameMode = "";
    private final Random random = new Random();

    //Variables and constants for the music
    private static final String MUSICSTARTSCENE = "dummy.mp3";
    private static final String MUSICHARD = "dummy.mp3";
    private static final String MUSICMEDIUM = "dummy.mp3";
    private static final String MUSICEASY = "dummy.mp3";
    private MediaPlayer mediaPlayer;
    private double volume = 0.15;

    //Variables and constants for the audio clips
    private static final String YOUWIN = "YouWin.mp3";
    private static final String YOULOSE = "YouLose.mp3";
    private static final String TIE = "Tie.mp3";
    private AudioClip audioClip;

    //Variables for banners
    private Text aniX = new Text("X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O  ");
    private Text aniO = new Text("O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X   O   X  ");

    /* *********************************************************
     * Anonymous inner class for tiles
     ******************************************************** */
    public final static class Tile extends Button {
        private final int row;
        private final int col;
        private Mark mark;

        public Tile(int initRow, int initCol, Mark initMark) {
            row = initRow;
            col = initCol;
            mark = initMark;
            initialiseTile();
        }

        private void initialiseTile() {
            this.setOnMouseClicked(e -> {
                if (!board.isCrossTurn()) {
                    board.placeMark(this.row, this.col);
                    this.update();
                }
            });
            this.setStyle("-fx-font-size:70");
            this.setTextAlignment(TextAlignment.CENTER);
            this.setMinSize(150.0, 150.0);
            this.setText("" + this.mark);
        }

        public void update() {
            this.mark = board.getMarkAt(this.row, this.col);
            this.setText("" + mark);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support.
     * @param args the command line arguments
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Start() for thread based JavaFX.
     * @param primaryStage Resembles the window in which everything is shown
     */
    @Override
    public void start(Stage primaryStage) {

        /* **********************************************************
         * MediaPlayer for the background music
         ********************************************************** */
        mediaPlayer = new MediaPlayer(createMedia(MUSICSTARTSCENE));

        /* **********************************************************
         * Start threads for moving banners
         ********************************************************** */
        startLiveBanners(aniX, true);
        startLiveBanners(aniO, false);

        /* *********************************************************
         * Create and configure JavaFX-elements for the game-scene
         ********************************************************* */
        //Create Layout
        gamelayout = new BorderPane();
        gamelayout.setCenter(generateBoard());
        //Create Menu
        menuBar = new MenuBar();
        gameMenu = new Menu("Options");
        resetGameOption = new MenuItem("Reset Game");
        toMainMenuOption = new MenuItem("Exit to menu");
        gameMenu.getItems().addAll(toMainMenuOption, resetGameOption);
        menuBar.getMenus().add(gameMenu);
        gamelayout.setTop(menuBar);
        //Create Scene
        Scene game = new Scene(gamelayout);

        /* *********************************************************
        * Create and configure JavaFX-elements for the menu-scene
        ********************************************************* */
        //Configure Labels
        title.getStyleClass().add("title");
        instruction.getStyleClass().add("title");
        title.setFont(eightBitFont);
        instruction.setStyle("-fx-font-size: 10pt");
        //Configure Buttons
        bEasy.setMinWidth(80);
        bMedium.setMinWidth(80);
        bHard.setMinWidth(80);
        explanation.setMinWidth(80);
        endG.setMinWidth(80);
        //Create Layout
        menulayout.getChildren().addAll(aniX, title, instruction, bEasy, bMedium, bHard, explanation, endG, aniO);
        menulayout.setAlignment(Pos.CENTER);
        //Create Scene
        Scene startMenu = new Scene(menulayout, 400, 450);

        /* *********************************************************
         * Create and configure JavaFX-elements for the settings-scene
         ********************************************************* */

        //Configure Labels
        settingsTitle.getStyleClass().add("title");
        settingsTitle.setFont(eightBitFont);
        settingsMainText.getStyleClass().add("text");
        settingsMainText.setMaxWidth(300);
        settingsMainText.setWrapText(true);
        settingsMainText.setTextAlignment(TextAlignment.CENTER);
        volumeInstructions.getStyleClass().add("text");

        //Configure VolumeSlider
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.2);
        volumeSlider.setMinorTickCount(2);
        volumeSlider.setBlockIncrement(0.1);
        volumeSlider.setMaxWidth(300);
        volumeSlider.setValue(0.15);

        //Create Layout
        settingslayout.getChildren().addAll(settingsTitle, settingsMainText, volumeInstructions, volumeSlider, bToMainMenu, triggerDarkMode);
        settingslayout.setAlignment(Pos.CENTER);

        //Create Scene
        Scene settings = new Scene(settingslayout, 400, 450);

        /* *********************************************************
         * Configure Stage
         ********************************************************* */
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(startMenu);
        primaryStage.show();

        /* *********************************************************
         * Configure MediaPlayer
         ********************************************************* */
        mediaPlayer.setVolume(volume);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        /* *********************************************************
         * RUN GAME
         ********************************************************* */
        runGameLoop();

        /* *********************************************************
         * EventListener for UI-Elements
         ********************************************************* */
        //Reset game option in top menu
        resetGameOption.setOnAction(e -> {
            resetGame();
        });

        //To main menu option in top menu
        toMainMenuOption.setOnAction(e-> {
            gameTimer.stop();
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(createMedia(MUSICSTARTSCENE));
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            primaryStage.setScene(startMenu);
        });

        //Easy game button
        bEasy.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(createMedia(MUSICEASY));
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            gameMode = EASYGAME;
            resetGame();
            gameTimer.start();
            primaryStage.setScene(game);
        });

        //Medium game button
        bMedium.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(createMedia(MUSICMEDIUM));
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            gameMode = MEDIUMGAME;
            resetGame();
            gameTimer.start();
            primaryStage.setScene(game);
        });

        //Hard Game option
        bHard.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(createMedia(MUSICHARD));
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            gameMode = HARDGAME;
            resetGame();
            gameTimer.start();
            primaryStage.setScene(game);
        });

        //Explanation button
        explanation.setOnAction(e -> {
            primaryStage.setScene(settings);
        });

        //Volumeslider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volume = (double) newValue;
                mediaPlayer.setVolume(volume);
            }
        });

        //Button for darkmode aka the easteregg
        triggerDarkMode.setOnAction(event -> {
            if (isDark) {
                startMenu.getStylesheets().add(getClass().getResource("lightmode.css").toExternalForm());
                game.getStylesheets().add(getClass().getResource("lightmode.css").toExternalForm());
                settings.getStylesheets().add(getClass().getResource("lightmode.css").toExternalForm());
                triggerDarkMode.setDisable(true);
                triggerDarkMode.setText("Told you...");
            } else {
                startMenu.getStylesheets().add(getClass().getResource("darkmode.css").toExternalForm());
                game.getStylesheets().add(getClass().getResource("darkmode.css").toExternalForm());
                settings.getStylesheets().add(getClass().getResource("darkmode.css").toExternalForm());
                isDark = true;
                triggerDarkMode.setText("I said: DO NOT PRESS!");

            }
        });

        //Button to go back to the main menu from the explanation scene
        bToMainMenu.setOnAction(event -> {
            primaryStage.setScene(startMenu);
        });

        //Exit the game and close all threads
        endG.setOnAction(e -> {
            System.exit(0);
        });

    }

    /**
     * Creates the TicTacToe game layout by placing the Tiles for each row and column.
     * In addition, each Tile gets a CSS class added.
     * @return Returns a GridPane with the individual tiles
     */
    private static GridPane generateBoard() {
        gameBoard = new GridPane();
        board = new Board();
        gameBoard.setAlignment(Pos.CENTER);

        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = new Tile(row, col, board.getMarkAt(row, col));
                tile.getStyleClass().add("tile");
                GridPane.setConstraints(tile, col, row);
                gameBoard.getChildren().add(tile);
            }
        }
        return gameBoard;
    }

    /**
     * Runs the AnimationTimer which is containing the logic for the different gamemodes
     * and their individual methods of placing the X.
     * If the game is won or a tie has occurred the game is ended.
     */
    private void runGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (board.isGameOver()) {
                    endGame();
                } else {
                    if (board.isCrossTurn()) {
                        switch (gameMode) {
                            case MEDIUMGAME:
                                if (random.nextBoolean()) {
                                    playAI();
                                    System.out.println("Medium: AI");
                                } else {
                                    playRandom();
                                    System.out.println("Medium: Random");
                                }
                                break;
                            case HARDGAME:
                                playAI();
                                System.out.println("Hard");
                                break;
                            default:
                                playRandom();
                                System.out.println("Easy");
                                break;
                        }
                    }
                }
            }
        };
        gameTimer.start();
    }

    /**
     * Plays the computers move with a random selection of the tile
     */
    private static void playRandom() {
        int[] move = ai.Random.randomMove(board);
        int row = move[0];
        int col = move[1];
        setMark(row, col);
    }

    /**
     * Plays the computers move with the algorithm
     */
    private static void playAI() {
        int[] move = MiniMaxAlgorithm.aiMove(board);
        int row = move[0];
        int col = move[1];
        setMark(row, col);
    }

    /**
     * This functions sets the corresponding mark at the specified tile and is
     * called by the computer players
     * @param row The index of the row where the mark is placed
     * @param col The index of the column where the mark is placed
     */
    private static void setMark(int row, int col) {
        board.placeMark(row, col);
        for (Node child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                Tile t = (Tile) child;
                t.update();
                return;
            }
        }
    }

    /**
     * Resets the current game by stopping the gameTimer, cleaning the tiles and running the gameloop.
     */
    private void resetGame() {
        gameTimer.stop();
        gamelayout.setCenter(generateBoard());
        runGameLoop();
    }
    /**
     * This function is called when a player has won the game.
     * It stops the current game, shows the popup-message with the correct winner or tie as message,
     * pauses the mediaplayer and plays the according audioclip.
     * If the popup-message is closed the game will be reset and the player can play again.
     */
    private void endGame() {
        setVolume(mediaPlayer, 0, 1000);
        gameTimer.stop();
        Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION, "", new ButtonType("New Game"));
        Mark winner = board.getWinningMark();

        gameOverAlert.setTitle("Game Over");
        gameOverAlert.setHeaderText(null);
        if (winner == Mark.BLANK) {
            gameOverAlert.setContentText("Draw!");
            audioClip = new AudioClip(getClass().getResource(TIE).toExternalForm());
            audioClip.play();
        } else {
            gameOverAlert.setContentText(winner + " wins! Good game.");
            if (winner == Mark.X) {
                audioClip = new AudioClip(getClass().getResource(YOULOSE).toExternalForm());
                audioClip.play();
                gameOverAlert.setContentText(winner + " wins! Try again.");
            } else {
                audioClip = new AudioClip(getClass().getResource(YOUWIN).toExternalForm());
                audioClip.play();
            }
        }
        gameOverAlert.setOnHidden(e -> {
            gameOverAlert.close();
            resetGame();
            setVolume(mediaPlayer, volume, 1000);
        });
        gameOverAlert.show();
    }

    /**
     * Takes a Sting with a filename, gets the whole URI and returns a media-object for further use.
     * The file has to be located in the res-directory!
     * @param file The name of the file from which a media-object is created
     * @return Returns a media-object
     */
    private Media createMedia(String file) {
        Media media = new Media(getClass().getResource(file).toExternalForm());
        return media;
    }

    /**
     * Takes a mediaplayer and fades from its current volume to the target volume
     * in the set time.
     * @param mediaPlayer Mediaplayer which volume will be changed
     * @param targetVolume The target volume
     * @param milliseconds The time in which the volumefade should take place
     */
    private void setVolume(MediaPlayer mediaPlayer, double targetVolume, int milliseconds) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(milliseconds),
                        new KeyValue(mediaPlayer.volumeProperty(), targetVolume))
        );
        timeline.play();
    }

    /**
     * Takes a JavaFX-Text-element and an Boolean indicating the direction.
     * Two KeyValues as well as two KeyFrames are created with different values according to the Boolean.
     * These KeyFrames are animated with a Timeline. The whole Timeline runs in its own thread.
     * @param msg The text to animate
     * @param direction The direction, in which the text is moving
     */
    private void startLiveBanners(Text msg, Boolean direction){
        new Thread(() -> {
            try{
                msg.setTextOrigin(VPos.TOP);
                msg.setFont(Font.font(25));

                double sceneWidth = 400;

                KeyValue initKeyValue = new KeyValue(msg.translateXProperty(), -0.5 * sceneWidth);
                KeyValue endKeyValue = new KeyValue(msg.translateXProperty(), 0.5 * sceneWidth);

                if(direction){
                    initKeyValue = new KeyValue(msg.translateXProperty(), 0.5 * sceneWidth);
                    endKeyValue = new KeyValue(msg.translateXProperty(), -0.5 * sceneWidth);
                }

                KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
                KeyFrame endFrame = new KeyFrame(Duration.seconds(30), endKeyValue);

                Timeline timeline = new Timeline(initFrame, endFrame);

                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
