package ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import animation.Animator;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

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
    
    //动画
    private Animator playerAnimator;

    private Map<Enemy, Animator> enemyAnimators = new HashMap<>();

    private int playerAttackTimer = 0;

    private static final int PLAYER_ATTACK_DURATION = 18;

    private static final int SPRITE_SIZE = 64;
    private static final int SPRITE_OFFSET = (SPRITE_SIZE - TILE_SIZE) / 2;
    
    private int getEnemySpriteSize(Enemy enemy) {

        if (enemy.getTypeName().equals("skeleton")) {
            return 80;
        }

        return SPRITE_SIZE;
    }
    //------------------------
    // 地图系统
    //------------------------

    private MazeMap mazeMap;

    private MapRenderer mapRenderer;
    
    //怪物
    private ArrayList<Enemy> enemies = new ArrayList<>();
    
    private Set<KeyCode> pressedKeys = new HashSet<>();

    private int moveCooldown = 0;

    private int enemyMoveCounter = 0;
    
    private int attackCooldown = 0;
    
    private int playerDamageCooldown = 0;

    //------------------------
    // 游戏循环
    //------------------------

    private AnimationTimer gameLoop;
    private boolean gameEnded = false;

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

        setupGameLoop();

        Platform.runLater(() -> requestFocus());
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
        
        setupPlayerAnimator();

        setupEnemyAnimators();
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

        setupEnemyAnimators();

        requestFocus();
    }
    
    //动画加载
    private void setupPlayerAnimator() {

        playerAnimator = new Animator();

        addPlayerWeaponClips("wood");
        addPlayerWeaponClips("iron");
        addPlayerWeaponClips("diamond");

        playerAnimator.setFrameSpeed(8);
    }
    
    private void addPlayerWeaponClips(String weapon) {

        playerAnimator.addClip(
                weapon + "_walk_down",
                "/player/player_" + weapon + "_walk_down_0.png",
                "/player/player_" + weapon + "_walk_down_1.png"
        );

        playerAnimator.addClip(
                weapon + "_walk_up",
                "/player/player_" + weapon + "_walk_up_0.png",
                "/player/player_" + weapon + "_walk_up_1.png"
        );

        playerAnimator.addClip(
                weapon + "_walk_left",
                "/player/player_" + weapon + "_walk_left_0.png",
                "/player/player_" + weapon + "_walk_left_1.png"
        );

        playerAnimator.addClip(
                weapon + "_walk_right",
                "/player/player_" + weapon + "_walk_right_0.png",
                "/player/player_" + weapon + "_walk_right_1.png"
        );

        playerAnimator.addClip(
                weapon + "_attack_down",
                "/player/player_" + weapon + "_attack_down_0.png"
        );

        playerAnimator.addClip(
                weapon + "_attack_up",
                "/player/player_" + weapon + "_attack_up_0.png"
        );

        playerAnimator.addClip(
                weapon + "_attack_left",
                "/player/player_" + weapon + "_attack_left_0.png"
        );

        playerAnimator.addClip(
                weapon + "_attack_right",
                "/player/player_" + weapon + "_attack_right_0.png"
        );
    }
    
    private void setupEnemyAnimators() {

        enemyAnimators.clear();

        for (Enemy enemy : enemies) {

            Animator animator = new Animator();

            String type = enemy.getTypeName();

            if (type.equals("slime")) {

                animator.addClip(
                        "idle",
                        "/enemy/slime_idle_0.png",
                        "/enemy/slime_idle_1.png",
                        "/enemy/slime_idle_2.png"
                );

                animator.addClip(
                        "attack_left",
                        "/enemy/slime_attack_left_0.png",
                        "/enemy/slime_attack_left_1.png"
                );

                animator.addClip(
                        "attack_right",
                        "/enemy/slime_attack_right_0.png",
                        "/enemy/slime_attack_right_1.png"
                );

            } else if (type.equals("goblin")) {

                animator.addClip(
                        "walk_left",
                        "/enemy/goblin_walk_left_0.png",
                        "/enemy/goblin_walk_left_1.png"
                );

                animator.addClip(
                        "walk_right",
                        "/enemy/goblin_walk_right_0.png",
                        "/enemy/goblin_walk_right_1.png"
                );

                animator.addClip(
                        "attack_left",
                        "/enemy/goblin_attack_left_0.png",
                        "/enemy/goblin_attack_left_1.png"
                );

                animator.addClip(
                        "attack_right",
                        "/enemy/goblin_attack_right_0.png",
                        "/enemy/goblin_attack_right_1.png"
                );

            } else if (type.equals("skeleton")) {

                animator.addClip(
                        "walk_down",
                        "/enemy/skeleton_walk_down_0.png",
                        "/enemy/skeleton_walk_down_1.png"
                );

                animator.addClip(
                        "walk_up",
                        "/enemy/skeleton_walk_up_0.png",
                        "/enemy/skeleton_walk_up_1.png"
                );

                animator.addClip(
                        "walk_left",
                        "/enemy/skeleton_walk_left_0.png",
                        "/enemy/skeleton_walk_left_1.png"
                );

                animator.addClip(
                        "walk_right",
                        "/enemy/skeleton_walk_right_0.png",
                        "/enemy/skeleton_walk_right_1.png"
                );

                animator.addClip(
                        "attack_down",
                        "/enemy/skeleton_attack_down_0.png",
                        "/enemy/skeleton_attack_down_1.png"
                );

                animator.addClip(
                        "attack_up",
                        "/enemy/skeleton_attack_up_0.png",
                        "/enemy/skeleton_attack_up_1.png"
                );

                animator.addClip(
                        "attack_left",
                        "/enemy/skeleton_attack_left_0.png",
                        "/enemy/skeleton_attack_left_1.png"
                );

                animator.addClip(
                        "attack_right",
                        "/enemy/skeleton_attack_right_0.png",
                        "/enemy/skeleton_attack_right_1.png"
                );
            }

            animator.setFrameSpeed(8);

            enemyAnimators.put(enemy, animator);
        }
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

        if (gameEnded) {
            return;
        }

        Player player = gameManager.getPlayer();

        if (player != null) {
            player.setMoving(false);
        }

        if (moveCooldown > 0) {
            moveCooldown--;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (playerDamageCooldown > 0) {
            playerDamageCooldown--;
        }

        if (playerAttackTimer > 0) {

            playerAttackTimer--;

            if (playerAttackTimer == 0 && player != null) {
                player.stopAttack();
            }
        }

        processInput();

        checkKeyCollection();

        checkPortal();

        checkExit();

        updateEnemies();

        checkEnemyContact();

        checkPlayerDeath();

        checkBossDefeated();

        updatePlayerAnimation();

        updateEnemyAnimations();
    }
    
    private void updatePlayerAnimation() {

        Player player = gameManager.getPlayer();

        if (player == null || playerAnimator == null) {
            return;
        }

        String dir = getDirectionKey(player.getDirection());
        String weapon = getWeaponKey(player.getWeaponName());

        if (player.isAttacking()) {

            playerAnimator.play(weapon + "_attack_" + dir, false);

            playerAnimator.update();

        } else {

            playerAnimator.setClipFrame(
                    weapon + "_walk_" + dir,
                    player.getWalkFrameIndex()
            );
        }
    }
    
    private void updateEnemyAnimations() {

        for (Enemy enemy : enemies) {

            Animator animator = enemyAnimators.get(enemy);

            if (animator == null) {
                continue;
            }

            String type = enemy.getTypeName();

            if (type.equals("slime")) {

                updateSlimeAnimation(enemy, animator);

            } else if (type.equals("goblin")) {

                updateGoblinAnimation(enemy, animator);

            } else if (type.equals("skeleton")) {

                updateSkeletonAnimation(enemy, animator);
            }

            if (enemy.isAttacking() && animator.isFinished()) {
                enemy.stopAttack();
            }
        }
    }
    
    private void updateSlimeAnimation(Enemy enemy, Animator animator) {

        if (enemy.isAttacking()) {

            String dir = getHorizontalDirectionKey(enemy.getDirection());

            animator.play("attack_" + dir, false);

            animator.update();

        } else {

            animator.play("idle", true);

            animator.update();
        }
    }
    
    private void updateGoblinAnimation(Enemy enemy, Animator animator) {

        String dir = getHorizontalDirectionKey(enemy.getDirection());

        if (enemy.isAttacking()) {

            animator.play("attack_" + dir, false);

            animator.update();

        } else {

            animator.setClipFrame(
                    "walk_" + dir,
                    enemy.getWalkFrameIndex()
            );
        }
    }
    
    private void updateSkeletonAnimation(Enemy enemy, Animator animator) {

        String dir = getDirectionKey(enemy.getDirection());

        if (enemy.isAttacking()) {

            animator.play("attack_" + dir, false);

            animator.update();

        } else {

            animator.setClipFrame(
                    "walk_" + dir,
                    enemy.getWalkFrameIndex()
            );
        }
    }
    
    private String getDirectionKey(Player.Direction direction) {

        if (direction == Player.Direction.UP) {
            return "up";
        }

        if (direction == Player.Direction.DOWN) {
            return "down";
        }

        if (direction == Player.Direction.LEFT) {
            return "left";
        }

        return "right";
    }
    
    private String getHorizontalDirectionKey(Player.Direction direction) {

        if (direction == Player.Direction.LEFT) {
            return "left";
        }

        return "right";
    }
    
    private String getWeaponKey(String weaponName) {

        if (weaponName == null) {
            return "wood";
        }

        if (weaponName.equalsIgnoreCase("Iron Sword")) {
            return "iron";
        }

        if (weaponName.equalsIgnoreCase("Diamond Sword")) {
            return "diamond";
        }

        return "wood";
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
    
    //攻击系统
    private void playerAttack() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        if (attackCooldown > 0) {
            return;
        }

        attackCooldown = 16;

        player.startAttack();

        playerAttackTimer = PLAYER_ATTACK_DURATION;

        for (int i = enemies.size() - 1; i >= 0; i--) {

            Enemy enemy = enemies.get(i);

            if (player.isEnemyInAttackRange(
                    enemy.getRow(),
                    enemy.getCol()
            )) {

                enemy.takeDamage(player.getDamage());

                if (enemy.isDead()) {

                    player.addKill();

                    player.addCoins(enemy.getCoinReward());

                    enemyAnimators.remove(enemy);

                    enemies.remove(i);
                }

                return;
            }
        }
    }

    //------------------------
    // 怪物
    //------------------------
    private void checkEnemyContact() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        if (playerDamageCooldown > 0) {
            return;
        }

        for (Enemy enemy : enemies) {

            if (enemy.getRow() == player.getRow()
                    && enemy.getCol() == player.getCol()) {

                faceEnemyToPlayer(enemy, player);

                enemy.startAttack();

                player.takeDamage(enemy.getDamage());

                playerDamageCooldown = 60;

                return;
            }
        }
    }
    
    private void faceEnemyToPlayer(Enemy enemy, Player player) {

        String type = enemy.getTypeName();

        int rowDiff = player.getRow() - enemy.getRow();
        int colDiff = player.getCol() - enemy.getCol();

        if (type.equals("slime") || type.equals("goblin")) {

            if (colDiff < 0) {
                enemy.setDirection(Player.Direction.LEFT);
            } else if (colDiff > 0) {
                enemy.setDirection(Player.Direction.RIGHT);
            }

            return;
        }

        if (Math.abs(rowDiff) > Math.abs(colDiff)) {

            if (rowDiff < 0) {
                enemy.setDirection(Player.Direction.UP);
            } else if (rowDiff > 0) {
                enemy.setDirection(Player.Direction.DOWN);
            }

        } else {

            if (colDiff < 0) {
                enemy.setDirection(Player.Direction.LEFT);
            } else if (colDiff > 0) {
                enemy.setDirection(Player.Direction.RIGHT);
            }
        }
    }

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
        	    
        		gameEnded = true;

            stopGameLoop();

            gameManager.showGameOver();
        }
    }

    //------------------------
    // Boss死亡
    //------------------------

    private void checkBossDefeated() {

        if (mazeMap.isFinalLevel() && enemies.isEmpty()) {

            gameManager.showVictory();
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

        int x = player.getCol() * TILE_SIZE - SPRITE_OFFSET;
        int y = player.getRow() * TILE_SIZE - SPRITE_OFFSET;

        Image frame = null;

        if (playerAnimator != null) {
            frame = playerAnimator.getCurrentFrame();
        }

        if (frame != null) {

            gc.drawImage(
                    frame,
                    x,
                    y,
                    SPRITE_SIZE,
                    SPRITE_SIZE
            );

        } else {

            gc.setFill(Color.DODGERBLUE);

            gc.fillOval(
                    player.getCol() * TILE_SIZE + 8,
                    player.getRow() * TILE_SIZE + 8,
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

        int size = getEnemySpriteSize(enemy);

        int offset = (size - TILE_SIZE) / 2;

        int x = enemy.getCol() * TILE_SIZE - offset;
        int y = enemy.getRow() * TILE_SIZE - offset;

        Animator animator = enemyAnimators.get(enemy);

        Image frame = null;

        if (animator != null) {
            frame = animator.getCurrentFrame();
        }

        if (frame != null) {

            gc.drawImage(
                    frame,
                    x,
                    y,
                    size,
                    size
            );

        } else if (enemy.getSprite().getImage() != null) {

            gc.drawImage(
                    enemy.getSprite().getImage(),
                    x,
                    y,
                    size,
                    size
            );

        } else {

            gc.setFill(Color.RED);

            gc.fillOval(
                    enemy.getCol() * TILE_SIZE + 8,
                    enemy.getRow() * TILE_SIZE + 8,
                    TILE_SIZE - 16,
                    TILE_SIZE - 16
            );
        }

        drawEnemyHealthBar(
                enemy,
                enemy.getCol() * TILE_SIZE,
                enemy.getRow() * TILE_SIZE
        );
    }
}
    
    private void drawEnemyHealthBar(Enemy enemy, double x, double y) {

        double barWidth = TILE_SIZE - 8;
        double barHeight = 5;

        double hpRatio = (double) enemy.getHp() / enemy.getMaxHp();

        gc.setFill(Color.BLACK);
        gc.fillRect(
                x + 4,
                y - 8,
                barWidth,
                barHeight
        );

        gc.setFill(Color.RED);
        gc.fillRect(
                x + 4,
                y - 8,
                barWidth * hpRatio,
                barHeight
        );
    }

    //------------------------
    // HUD
    //------------------------

    private void drawHUD() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        gc.setFill(Color.rgb(0, 0, 0, 0.65));
        gc.fillRoundRect(10, 10, 430, 95, 15, 15);

        gc.setStroke(Color.GOLD);
        gc.strokeRoundRect(10, 10, 430, 95, 15, 15);

        gc.setFill(Color.WHITE);

        gc.fillText(
                "HP: " + player.getHp() + "/" + player.getMaxHp(),
                25,
                35
        );

        gc.fillText(
                "Coins: " + player.getCoins(),
                150,
                35
        );

        gc.fillText(
                "Keys: " + player.getKeyCount() + "/" + player.getRequiredKeys(),
                280,
                35
        );

        gc.fillText(
                "Weapon: " + player.getWeaponName()
                        + " | Damage: " + player.getDamage(),
                25,
                62
        );

        gc.fillText(
                "Kills: " + player.getKills()
                        + " | Enemies: " + enemies.size(),
                25,
                88
        );
    }

    //------------------------
    // 按键
    //------------------------

    public void handleKeyPressed(KeyCode key) {

        pressedKeys.add(key);

        if (key == KeyCode.ESCAPE) {
            clearInputState();
            gameManager.pauseGame();
            return;
        }

        if (key == KeyCode.B) {
            clearInputState();
            gameManager.openShop();
        }
    }
    
    public void handleKeyReleased(KeyCode key) {
        pressedKeys.remove(key);
    }
    
    public void clearInputState() {
        pressedKeys.clear();
        moveCooldown = 0;
        attackCooldown = 0;
    }
    
    private void processInput() {

        if (pressedKeys.contains(KeyCode.SPACE)) {
            playerAttack();
        }

        if (moveCooldown > 0) {
            return;
        }

        if (pressedKeys.contains(KeyCode.W)) {
            movePlayer(Player.Direction.UP);
            moveCooldown = 8;
        } else if (pressedKeys.contains(KeyCode.A)) {
            movePlayer(Player.Direction.LEFT);
            moveCooldown = 8;
        } else if (pressedKeys.contains(KeyCode.S)) {
            movePlayer(Player.Direction.DOWN);
            moveCooldown = 8;
        } else if (pressedKeys.contains(KeyCode.D)) {
            movePlayer(Player.Direction.RIGHT);
            moveCooldown = 8;
        }
    }

    //------------------------
    // 提供地图访问
    //------------------------

    public MazeMap getMazeMap() {

        return mazeMap;
    }
}