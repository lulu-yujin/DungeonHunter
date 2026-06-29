package game;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import player.Player;
import shop.Shop;
import ui.GameOverUI;
import ui.GamePanel;
import ui.InstructionPane;
import ui.PauseMenu;
import ui.StartMenu;
// import ui.VictoryUI; // Use this if you created a separate VictoryUI class.

/**
 * Manages the main game flow and JavaFX scene switching.
 */
public class GameManager {

    // ================= Constants =================

    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 720;
    private static final int FINAL_MAP = 3;

    // ================= Fields =================

    private Stage stage;

    private Player player;

    private GamePanel gamePanel;

    private Scene gameScene;

    private int currentMap;

    private GameState gameState;

    // ================= Constructor =================

    public GameManager(Stage stage) {
        this.stage = stage;
        this.currentMap = 1;
        this.gameState = GameState.MENU;
    }

    // ================= Scene Creation =================

    /**
     * Creates the main game scene and connects keyboard input to GamePanel.
     */
    private Scene createGameScene() {

        Scene scene = new Scene(
                gamePanel,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );

        scene.setOnKeyPressed(e -> {
            gamePanel.handleKeyPressed(e.getCode());
            e.consume();
        });

        scene.setOnKeyReleased(e -> {
            gamePanel.handleKeyReleased(e.getCode());
            e.consume();
        });

        return scene;
    }

    /**
     * Requests focus for the game window after switching scenes.
     */
    private void requestGameFocus() {

        Platform.runLater(() -> {
            stage.toFront();
            stage.requestFocus();
            gamePanel.requestFocus();
        });
    }

    // ================= Main Menu and Instructions =================

    public void showStartMenu() {

        gameState = GameState.MENU;

        StartMenu menu = new StartMenu(this);

        stage.setScene(
                new Scene(
                        menu,
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                )
        );
    }

    public void showInstructions() {

        InstructionPane pane = new InstructionPane(this);

        stage.setScene(
                new Scene(
                        pane,
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                )
        );
    }

    // ================= Game Start and Restart =================

    public void startGame() {

        player = new Player();

        currentMap = 1;

        gameState = GameState.PLAYING;

        gamePanel = new GamePanel(this);

        gameScene = createGameScene();

        stage.setScene(gameScene);

        requestGameFocus();

        gamePanel.startGameLoop();
    }

    public void restartGame() {
        startGame();
    }

    // ================= Level Control =================

    /**
     * Moves the player to the next map if enough keys have been collected.
     */
    public void nextMap() {

        if (player == null) {
            return;
        }

        if (!player.hasEnoughKeys()) {

            System.out.println(
                    "You need 3 keys. Current keys: "
                            + player.getKeyCount()
                            + "/"
                            + player.getRequiredKeys()
            );

            return;
        }

        player.useKeysForNextLevel();

        currentMap++;

        if (currentMap > FINAL_MAP) {

            showVictory();

        } else {

            gamePanel.loadMap(currentMap);
        }
    }

    // ================= Pause and Shop =================

    public void pauseGame() {

        gameState = GameState.PAUSED;

        gamePanel.clearInputState();

        gamePanel.stopGameLoop();

        PauseMenu pause = new PauseMenu(this);

        stage.setScene(
                new Scene(
                        pause,
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                )
        );
    }

    public void openShop() {

        gameState = GameState.SHOP;

        gamePanel.clearInputState();

        gamePanel.stopGameLoop();

        Shop shop = new Shop(this);

        stage.setScene(
                new Scene(
                        shop,
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                )
        );
    }

    /**
     * Returns to the existing game scene after shop or pause menu.
     */
    public void resumeGame() {

        gameState = GameState.PLAYING;

        stage.setScene(gameScene);

        gamePanel.clearInputState();

        requestGameFocus();

        gamePanel.startGameLoop();
    }

    // ================= End Screens =================

    public void showGameOver() {

        if (gameState == GameState.GAME_OVER) {
            return;
        }

        gameState = GameState.GAME_OVER;

        if (gamePanel != null) {
            gamePanel.stopGameLoop();
        }

        GameOverUI ui = new GameOverUI(this, false);

        stage.setScene(
                new Scene(
                        ui,
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                )
        );
    }

    public void showVictory() {

        if (gameState == GameState.WIN) {
            return;
        }

        gameState = GameState.WIN;

        if (gamePanel != null) {
            gamePanel.stopGameLoop();
        }

        GameOverUI ui = new GameOverUI(this, true);

        stage.setScene(
                new Scene(
                        ui,
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                )
        );

        /*
         * If you created a separate VictoryUI class, use this instead:
         *
         * VictoryUI ui = new VictoryUI(this);
         * stage.setScene(new Scene(ui, WINDOW_WIDTH, WINDOW_HEIGHT));
         */
    }

    // ================= Getters =================

    public Player getPlayer() {
        return player;
    }

    public int getCurrentMap() {
        return currentMap;
    }

    public GameState getGameState() {
        return gameState;
    }
}