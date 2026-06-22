package game;

import javafx.scene.Scene;
import javafx.stage.Stage;

import player.Player;

import ui.*;

import shop.*;

public class GameManager {

    private Stage stage;

    private Player player;

    private GamePanel gamePanel;

    private int currentMap;

    private GameState gameState;

    public GameManager(Stage stage) {

        this.stage = stage;

        currentMap = 1;
    }

    public void showStartMenu() {

        gameState = GameState.MENU;

        StartMenu menu = new StartMenu();

        stage.setScene(new Scene(menu,960,720));
    }

    public void startGame() {

        player = new Player();

        currentMap = 1;

        gameState = GameState.PLAYING;

        gamePanel = new GamePanel();

        stage.setScene(new Scene(gamePanel,960,720));

        gamePanel.startGameLoop();
    }

    public Player getPlayer() {
        return player;
    }

    public int getCurrentMap() {
        return currentMap;
    }

    public void nextMap() {

        currentMap++;

        if(currentMap > 3) {

            showVictory();

        } else {

            gamePanel.loadMap(currentMap);
        }
    }

    public void pauseGame() {

        gameState = GameState.PAUSED;

        gamePanel.stopGameLoop();

        PauseMenu pause = new PauseMenu(this);

        stage.setScene(new Scene(pause,960,720));
    }

    public void openShop() {

        gameState = GameState.SHOP;

        gamePanel.stopGameLoop();

        Shop shop = new Shop(this);

        stage.setScene(new Scene(shop,960,720));
    }

    public void resumeGame() {

        gameState = GameState.PLAYING;

        stage.setScene(new Scene(gamePanel,960,720));

        gamePanel.startGameLoop();
    }

    public void showGameOver() {

        gameState = GameState.GAME_OVER;

        GameOverUI ui = new GameOverUI(this,false);

        stage.setScene(new Scene(ui,960,720));
    }

    public void showVictory() {

        gameState = GameState.WIN;

        GameOverUI ui = new GameOverUI(this,true);

        stage.setScene(new Scene(ui,960,720));
    }

    public void restartGame() {

        startGame();
    }
}