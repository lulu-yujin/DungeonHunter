package ui;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import game.GameManager;
import map.*;
import player.Player;
import enemy.*;

public class GamePanel extends Pane {

    //------------------------
    // 基础配置
    //------------------------

    public static final int TILE_SIZE = Tile.TILE_SIZE;

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
    
    //怪物
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private int enemyMoveCounter = 0;

    //------------------------
    // 游戏循环
    //------------------------

    private AnimationTimer gameLoop;

    //------------------------
    // 玩家贴图缓存
    //------------------------

    private Image playerSprite;

    private String currentPlayerSpritePath;

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
        
        spawnEnemies();
    }
    
    private void spawnEnemies() {

        enemies.clear();

        int level = mazeMap.getCurrentLevel();

        if (level == 1) {

            spawnEnemiesFromChar(MazeMap.BEGINNER, "slime");

        } else if (level == 2) {

            spawnEnemiesFromChar(MazeMap.ADVANCED, "goblin");

        } else if (level == 3) {

            int centerRow = MazeMap.ROWS / 2;
            int centerCol = MazeMap.COLS / 2;

            enemies.add(
                    EnemyType.createSkeleton(centerRow, centerCol, mazeMap)
            );
        }
    }
    
    private void spawnEnemiesFromChar(char spawnChar, String enemyName) {

        char[][] map = mazeMap.getMap();

        for (int r = 0; r < MazeMap.ROWS; r++) {
            for (int c = 0; c < MazeMap.COLS; c++) {

                if (map[r][c] == spawnChar) {

                    if (enemyName.equals("slime")) {

                        enemies.add(
                                EnemyType.createSlime(r, c, mazeMap)
                        );

                    } else if (enemyName.equals("goblin")) {

                        enemies.add(
                                EnemyType.createGoblin(r, c, mazeMap)
                        );
                    }
                }
            }
        }
    }
    

    //------------------------
    // 切换地图
    //------------------------

    public void loadMap(int level) {

        mazeMap.loadLevel(level);

        spawnPlayer();

        spawnEnemies();

        requestFocus();
    }

    //------------------------
    // 玩家出生
    //------------------------

    public void spawnPlayer() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int[] spawn = mazeMap.getSpawnPoint();

        int row = spawn[0];

        int col = spawn[1];

        player.setPosition(row, col);
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

        checkKeyCollection();

        checkPortal();

        checkExit();

        checkPlayerDeath();

        updateEnemies();

        checkBossDefeated();
    }

    //------------------------
    // 玩家移动
    //------------------------

    private void movePlayer(Player.Direction direction) {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        player.face(direction);

        int nextRow = player.getNextRow(direction);

        int nextCol = player.getNextCol(direction);

        if (mazeMap.isWalkable(nextRow, nextCol)) {

            player.move(direction);
        }
    }

    //------------------------
    // 怪物
    //------------------------

    private void updateEnemies() {

        enemyMoveCounter++;

        if (enemyMoveCounter % 30 != 0) {
            return;
        }

        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    //------------------------
    // 钥匙
    //------------------------

    private void checkKeyCollection() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int row = player.getRow();

        int col = player.getCol();

        if (mazeMap.isKey(row, col)) {

            player.pickUpKey();

            mazeMap.removeKeyAt(row, col);

            System.out.println(
                    "Key collected: "
                            + player.getKeyCount()
                            + "/"
                            + player.getRequiredKeys()
            );
        }
    }

    //------------------------
    // 通道检测
    //------------------------

    private void checkPortal() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int row = player.getRow();

        int col = player.getCol();

        if (mazeMap.isPortal(row, col)) {

            if (player.hasEnoughKeys()) {

                gameManager.nextMap();

            } else {

                // 不想刷屏的话，这里可以不写 println
                // System.out.println("You need 3 keys to enter the next level.");
            }
        }
    }

    //------------------------
    // 出口检测
    //------------------------

    private void checkExit() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int row = player.getRow();

        int col = player.getCol();

        if (mazeMap.isExit(row, col)) {

            gameManager.showVictory();
        }
    }

    //------------------------
    // 玩家死亡
    //------------------------

    private void checkPlayerDeath() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        if (player.isDead()) {

            gameManager.showGameOver();
        }
    }

    //------------------------
    // Boss死亡
    //------------------------

    private void checkBossDefeated() {

        if (mazeMap.isFinalLevel()) {

            // TODO
            // if (bossDead) {
            //     gameManager.showVictory();
            // }
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

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int x = player.getCol() * TILE_SIZE;

        int y = player.getRow() * TILE_SIZE;

        Image sprite = getPlayerSprite(player);

        if (sprite != null) {

            gc.drawImage(
                    sprite,
                    x,
                    y,
                    TILE_SIZE,
                    TILE_SIZE
            );

        } else {

            gc.setFill(Color.DODGERBLUE);

            gc.fillOval(
                    x + 8,
                    y + 8,
                    TILE_SIZE - 16,
                    TILE_SIZE - 16
            );
        }
    }

    private Image getPlayerSprite(Player player) {

        String path = player.getCurrentSpritePath();

        if (path == null) {
            return null;
        }

        if (!path.equals(currentPlayerSpritePath)) {

            currentPlayerSpritePath = path;

            playerSprite = loadImage(path);
        }

        return playerSprite;
    }

    private Image loadImage(String path) {

        try {

            var stream = getClass().getResourceAsStream(path);

            if (stream == null) {
                return null;
            }

            return new Image(stream);

        } catch (Exception e) {

            return null;
        }
    }

    //------------------------
    // 绘制怪物
    //------------------------

    private void drawEnemies() {

        for (Enemy enemy : enemies) {

            gc.drawImage(
                    enemy.getSprite().getImage(),
                    enemy.getSprite().getX(),
                    enemy.getSprite().getY(),
                    TILE_SIZE,
                    TILE_SIZE
            );
        }
    }

    //------------------------
    // 绘制道具
    //------------------------

    private void drawItems() {

        // 钥匙已经在 MapRenderer 里面画了
        // 所以这里暂时不用写
    }

    //------------------------
    // HUD
    //------------------------

    private void drawHUD() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        gc.setFill(Color.WHITE);

        gc.fillText(
                "Keys: "
                        + player.getKeyCount()
                        + "/"
                        + player.getRequiredKeys(),
                10,
                20
        );

        gc.fillText(
                "HP: "
                        + player.getHp(),
                10,
                40
        );

        gc.fillText(
                "Coins: "
                        + player.getCoins(),
                10,
                60
        );
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

                    movePlayer(Player.Direction.UP);

                    break;

                case A:

                    movePlayer(Player.Direction.LEFT);

                    break;

                case S:

                    movePlayer(Player.Direction.DOWN);

                    break;

                case D:

                    movePlayer(Player.Direction.RIGHT);

                    break;

                case SPACE:

                    // TODO
                    // attack

                    break;

                default:

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