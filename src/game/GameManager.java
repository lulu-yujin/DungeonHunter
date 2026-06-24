package game;

import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.stage.Stage;

import player.Player;

import ui.*;

import shop.*;

public class GameManager {

    private Stage stage;

    private Player player;

    private GamePanel gamePanel;
    
    private Scene gameScene;

    private int currentMap;

    private GameState gameState;
    
    private Scene createGameScene() {

        Scene scene = new Scene(gamePanel, 960, 720);

        scene.setOnKeyPressed(e -> {
            gamePanel.handleKeyPressed(e.getCode());
            e.consume();
        });

        return scene;
    }

    public GameManager(Stage stage) {

        this.stage = stage;

        currentMap = 1;
    }

    public void showStartMenu() {

        gameState = GameState.MENU;

        StartMenu menu = new StartMenu(this);

        stage.setScene(new Scene(menu, 960, 720));
    }
    
    public void showInstructions() {

        InstructionPane pane = new InstructionPane(this);

        stage.setScene(new Scene(pane, 960, 720));
    }

    public void startGame() {

        player = new Player();

        currentMap = 1;

        gameState = GameState.PLAYING;

        gamePanel = new GamePanel(this);

        gameScene = new Scene(gamePanel, 960, 720);

        gameScene.setOnKeyPressed(e -> {
            gamePanel.handleKeyPressed(e.getCode());
            e.consume();
        });

        gameScene.setOnKeyReleased(e -> {
            gamePanel.handleKeyReleased(e.getCode());
            e.consume();
        });

        stage.setScene(gameScene);

        Platform.runLater(() -> {
            stage.toFront();
            stage.requestFocus();
            gamePanel.requestFocus();
        });

        gamePanel.startGameLoop();
    }

    public Player getPlayer() {
        return player;
    }

    public int getCurrentMap() {
        return currentMap;
    }

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

        if (currentMap > 3) {

            showVictory();

        } else {

            gamePanel.loadMap(currentMap);
        }
    }

    public void pauseGame() {

        gameState = GameState.PAUSED;
        
        gamePanel.clearInputState();

        gamePanel.stopGameLoop();

        PauseMenu pause = new PauseMenu(this);

        stage.setScene(new Scene(pause, 960, 720));
    }

    public void openShop() {

        gameState = GameState.SHOP;
        
        gamePanel.clearInputState();

        gamePanel.stopGameLoop();

        Shop shop = new Shop(this);

        stage.setScene(new Scene(shop, 960, 720));
    }

    public void resumeGame() {

        gameState = GameState.PLAYING;

        stage.setScene(gameScene);
        
        gamePanel.clearInputState();

        Platform.runLater(() -> {
            stage.toFront();
            stage.requestFocus();
            gamePanel.requestFocus();
        });

        gamePanel.startGameLoop();
    }

    public void showGameOver() {

    		if (gameState == GameState.GAME_OVER) {
            return;
        }

        gameState = GameState.GAME_OVER;

        if (gamePanel != null) {
            gamePanel.stopGameLoop();
        }

        GameOverUI ui = new GameOverUI(this, false);

        stage.setScene(new Scene(ui, 960, 720));
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

        stage.setScene(new Scene(ui, 960, 720));
    }

    public void restartGame() {

        startGame();
    }
}