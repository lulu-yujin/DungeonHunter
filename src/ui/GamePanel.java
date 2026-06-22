package ui;

import game.GameManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import map.*;

public class GamePanel extends Pane {

    //------------------------
    // 基础配置
    //------------------------

    public static final int TILE_SIZE = 48;

    private final int SCREEN_WIDTH =
            MazeMap.COLS * TILE_SIZE;

    private final int SCREEN_HEIGHT =
            MazeMap.ROWS * TILE_SIZE;

    //------------------------
    // 核心对象
    //------------------------

    private GameManager gameManager;

    private Canvas canvas;

    private GraphicsContext gc;

    //------------------------
    // 地图系统
    //------------------------

    private MazeMap mazeMap;

    private MapRenderer mapRenderer;

    //------------------------
    // 游戏循环
    //------------------------

    private AnimationTimer gameLoop;

    //------------------------
    // 游戏数据
    //------------------------

    private int keyCount = 0;

    private int totalKeys = 3;

    //------------------------
    // 构造器
    //------------------------

    public GamePanel(GameManager gameManager) {

        this.gameManager = gameManager;

        setupCanvas();

        setupMap();

        setupKeyHandler();

        setupGameLoop();

        requestFocus();
    }

    //------------------------
    // Canvas
    //------------------------

    private void setupCanvas() {

        canvas = new Canvas(
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );

        gc = canvas.getGraphicsContext2D();

        getChildren().add(canvas);
    }

    //------------------------
    // 地图系统
    //------------------------

    private void setupMap() {

        mazeMap = new MazeMap();

        mapRenderer = new MapRenderer(
                mazeMap,
                canvas
        );

        spawnPlayer();
    }

    //------------------------
    // 玩家出生
    //------------------------

    public void spawnPlayer() {

        int[] spawn =
                mazeMap.getSpawnPoint();

        int row = spawn[0];

        int col = spawn[1];

        int x = col * TILE_SIZE;

        int y = row * TILE_SIZE;

        // TODO
        // player.setPosition(x,y);
    }

    //------------------------
    // 游戏循环
    //------------------------

    private void setupGameLoop() {

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                update();

                draw();
            }
        };
    }

    //------------------------
    // 开始游戏
    //------------------------

    public void startGameLoop() {

        gameLoop.start();
    }

    //------------------------
    // 停止游戏
    //------------------------

    public void stopGameLoop() {

        gameLoop.stop();
    }

    //------------------------
    // 游戏更新
    //------------------------

    private void update() {

        updatePlayer();

        updateEnemies();

        checkKeyCollection();

        checkPortal();

        checkPlayerDeath();

        checkBossDefeated();
    }

    //------------------------
    // 玩家
    //------------------------

    private void updatePlayer() {

        // TODO
        // player.update();
    }

    //------------------------
    // 怪物
    //------------------------

    private void updateEnemies() {

        // TODO
        // enemy update
    }

    //------------------------
    // 钥匙
    //------------------------

    private void checkKeyCollection() {

        // TODO

        /*
        if(player碰到钥匙){

            keyCount++;

        }
        */
    }

    //------------------------
    // 通道检测
    //------------------------

    private void checkPortal() {

        /*
         * 计算玩家所在格子
         */

        int row = 0;
        int col = 0;

        // TODO
        // row = playerY / TILE_SIZE
        // col = playerX / TILE_SIZE

        if (mazeMap.isPortal(row, col)) {

            if (keyCount >= totalKeys) {

                if (!mazeMap.isFinalLevel()) {

                    mazeMap.nextLevel();

                    spawnPlayer();

                    keyCount = 0;
                }
            }
        }
    }

    //------------------------
    // 玩家死亡
    //------------------------

    private void checkPlayerDeath() {

        // TODO

        /*
        if(player.getHp() <= 0){

            gameManager.showGameOver();

        }
        */
    }

    //------------------------
    // Boss死亡
    //------------------------

    private void checkBossDefeated() {

        if (mazeMap.isFinalLevel()) {

            // TODO

            /*
            if(bossDead){

                gameManager.showVictory();

            }
            */
        }
    }

    //------------------------
    // 绘制
    //------------------------

    private void draw() {

        gc.clearRect(
                0,
                0,
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );

        mapRenderer.render();

        drawItems();

        drawEnemies();

        drawPlayer();

        drawHUD();
    }

    //------------------------
    // 绘制玩家
    //------------------------

    private void drawPlayer() {

        // TODO
        // player.draw(gc);
    }

    //------------------------
    // 绘制怪物
    //------------------------

    private void drawEnemies() {

        // TODO
    }

    //------------------------
    // 绘制道具
    //------------------------

    private void drawItems() {

        // TODO
    }

    //------------------------
    // HUD
    //------------------------

    private void drawHUD() {

        gc.fillText(
                "Keys: "
                        + keyCount
                        + "/"
                        + totalKeys,
                10,
                20
        );

        /*
        gc.fillText(
                "HP: " + player.getHp(),
                10,
                40
        );

        gc.fillText(
                "Coins: " + player.getCoins(),
                10,
                60
        );
        */
    }

    //------------------------
    // 按键
    //------------------------

    private void setupKeyHandler() {

        setFocusTraversable(true);

        setOnKeyPressed(e -> {

            KeyCode key = e.getCode();

            switch (key) {

                case ESCAPE:

                    gameManager.pauseGame();

                    break;

                case B:

                    gameManager.openShop();

                    break;

                case W:

                    // player move

                    break;

                case A:

                    break;

                case S:

                    break;

                case D:

                    break;

                case SPACE:

                    // attack

                    break;
            }
        });
    }

    //------------------------
    // 提供地图访问
    //------------------------

    public MazeMap getMazeMap() {

        return mazeMap;
    }
}