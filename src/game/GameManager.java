package game;

import javafx.scene.Scene;
import javafx.stage.Stage;

import ui.StartMenu;
import ui.PauseMenu;
import ui.GameOverUI;
import ui.GamePanel;

import player.Player;

import enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

import map.MazeMap;

public class GameManager {

    private Stage stage;

    private Scene startScene;
    private Scene gameScene;
    private Scene pauseScene;
    private Scene gameOverScene;
    private Scene winScene;

    private GamePanel gamePanel;

    private Player player;

    private MazeMap map;

    private List<Enemy> enemy;

    public GameManager(Stage stage) {
        this.stage = stage;
    }

    /*
     * ==========================
     * START MENU
     * ==========================
     */

    public void showStartMenu() {

        StartMenu startMenu = new StartMenu();

        startScene = new Scene(
                startMenu,
                960,
                720
        );

        stage.setScene(startScene);
    }

    /*
     * ==========================
     * START GAME
     * ==========================
     */

    public void startGame() {

        initializeGame();

        gamePanel = new GamePanel(
                player,
                enemy,
                map,
                this
        );

        gameScene = new Scene(
                gamePanel,
                960,
                720
        );

        stage.setScene(gameScene);

        gamePanel.requestFocus();

        gamePanel.startGameLoop();
    }

    /*
     * ==========================
     * CREATE OBJECTS
     * ==========================
     */

    private void initializeGame() {

        loadMap();

        createPlayer();

        createMonsters();
    }

    private void loadMap() {

        map = new MazeMap();
    }

    private void createPlayer() {

        player = new Player(
                100,    // x
                100     // y
        );
    }

    private void createMonsters() {

        enemy = new ArrayList<>();

        /*
         * Beginner Zone
         */
        enemy.add(
                new Slime(300, 200)
        );

        enemy.add(
                new Slime(350, 250)
        );

        /*
         * Advanced Zone
         */
        enemy.add(
                new Goblin(600, 200)
        );

        enemy.add(
                new Skeleton(700, 300)
        );
    }

    /*
     * ==========================
     * PAUSE
     * ==========================
     */

    public void pauseGame() {

        if (gamePanel != null) {
            gamePanel.stopGameLoop();
        }

        PauseMenu pauseMenu = new PauseMenu(this);

        pauseScene = new Scene(
                pauseMenu,
                1024,
                768
        );

        stage.setScene(pauseScene);
    }

    /*
     * ==========================
     * RESUME
     * ==========================
     */

    public void resumeGame() {

        stage.setScene(gameScene);

        gamePanel.requestFocus();

        gamePanel.startGameLoop();
    }

    /*
     * ==========================
     * RESTART
     * ==========================
     */

    public void restartGame() {

        startGame();
    }

    /*
     * ==========================
     * GAME OVER
     * ==========================
     */

    public void gameOver() {

        if (gamePanel != null) {
            gamePanel.stopGameLoop();
        }

        GameOverUI screen =
                new GameOverUI(
                        this,
                        player
                );

        gameOverScene = new Scene(
                screen,
                1024,
                768
        );

        stage.setScene(gameOverScene);
    }

    /*
     * ==========================
     * WIN
     * ==========================
     */

    public void winGame() {

        if (gamePanel != null) {
            gamePanel.stopGameLoop();
        }

        GameOverUI screen =
                new GameOverUI(
                        this,
                        player
                );

        winScene = new Scene(
                screen,
                1024,
                768
        );

        stage.setScene(winScene);
    }

    /*
     * ==========================
     * GETTERS
     * ==========================
     */

    public Player getPlayer() {
        return player;
    }

    public MazeMap getMap() {
        return map;
    }

    public List<Enemy> getMonsters() {
        return enemy;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}