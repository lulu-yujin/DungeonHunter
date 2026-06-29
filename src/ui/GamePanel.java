package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import animation.Animator;
import enemy.Enemy;
import enemy.EnemyType;
import game.GameManager;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import map.MapRenderer;
import map.MazeMap;
import map.Tile;
import player.Player;

/**
 * Main game panel.
 * It connects map rendering, player control, enemies, animation, input and HUD drawing.
 */
public class GamePanel extends Pane {

    // ================= Constants =================

    public static final int TILE_SIZE = Tile.TILE_SIZE;

    private static final int SCREEN_WIDTH = MazeMap.COLS * TILE_SIZE;
    private static final int SCREEN_HEIGHT = MazeMap.ROWS * TILE_SIZE;

    private static final int MOVE_COOLDOWN_FRAMES = 8;
    private static final int ATTACK_COOLDOWN_FRAMES = 16;
    private static final int PLAYER_ATTACK_DURATION = 18;
    private static final int PLAYER_DAMAGE_COOLDOWN_FRAMES = 60;
    private static final int ENEMY_MOVE_INTERVAL = 30;

    private static final int SPRITE_SIZE = 64;
    private static final int SKELETON_SPRITE_SIZE = 80;

    private static final int PLAYER_HIT_FLASH_DURATION = 25;

    private static final int HUD_X = 10;
    private static final int HUD_Y = 10;
    private static final int HUD_WIDTH = 430;
    private static final int HUD_HEIGHT = 95;

    // ================= Core Objects =================

    private final GameManager gameManager;

    private Canvas canvas;
    private GraphicsContext gc;

    private MazeMap mazeMap;
    private MapRenderer mapRenderer;

    // ================= Player and Enemy Systems =================

    private ArrayList<Enemy> enemies = new ArrayList<>();

    // ================= Animation =================

    private Animator playerAnimator;

    private Map<Enemy, Animator> enemyAnimators = new HashMap<>();

    private int playerAttackTimer = 0;
    private int playerHitFlashTimer = 0;

    // ================= Input and Cooldowns =================

    private Set<KeyCode> pressedKeys = new HashSet<>();

    private int moveCooldown = 0;
    private int attackCooldown = 0;
    private int playerDamageCooldown = 0;
    private int enemyMoveCounter = 0;

    // ================= Game Loop =================

    private AnimationTimer gameLoop;

    private boolean gameEnded = false;

    // ================= Constructor =================

    public GamePanel(GameManager gameManager) {

        this.gameManager = gameManager;

        setupCanvas();

        setupMap();

        setupGameLoop();

        Platform.runLater(this::requestFocus);
    }

    // ================= Setup Methods =================

    private void setupCanvas() {

        canvas = new Canvas(
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );

        gc = canvas.getGraphicsContext2D();

        getChildren().add(canvas);
    }

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

    private void setupGameLoop() {

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                update();

                draw();
            }
        };
    }

    // ================= Game Loop Control =================

    public void startGameLoop() {

        if (gameLoop != null) {
            gameLoop.start();
        }
    }

    public void stopGameLoop() {

        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    // ================= Map and Level =================

    public void loadMap(int level) {

        mazeMap.loadLevel(level);

        clearInputState();

        moveCooldown = 0;
        attackCooldown = 0;
        playerDamageCooldown = 0;
        enemyMoveCounter = 0;
        playerAttackTimer = 0;
        playerHitFlashTimer = 0;

        spawnPlayer();

        spawnEnemies();

        setupEnemyAnimators();

        requestFocus();
    }

    public void spawnPlayer() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int[] spawn = mazeMap.getSpawnPoint();

        player.resetToSpawn(
                spawn[0],
                spawn[1]
        );
    }

    private void spawnEnemies() {

        enemies.clear();

        int level = mazeMap.getCurrentLevel();

        if (level == 1) {

            spawnEnemiesFromChar(MazeMap.BEGINNER);

        } else if (level == 2) {

            spawnEnemiesFromChar(MazeMap.ADVANCED);

        } else if (level == 3) {

            spawnBoss();
        }
    }

    private void spawnEnemiesFromChar(char spawnChar) {

        char[][] map = mazeMap.getMap();

        for (int row = 0; row < MazeMap.ROWS; row++) {
            for (int col = 0; col < MazeMap.COLS; col++) {

                if (map[row][col] == spawnChar) {

                    if (spawnChar == MazeMap.BEGINNER) {

                        enemies.add(
                                EnemyType.createSlime(row, col, mazeMap)
                        );

                    } else if (spawnChar == MazeMap.ADVANCED) {

                        enemies.add(
                                EnemyType.createGoblin(row, col, mazeMap)
                        );
                    }
                }
            }
        }
    }

    private void spawnBoss() {

        int centerRow = MazeMap.ROWS / 2;
        int centerCol = MazeMap.COLS / 2;

        enemies.add(
                EnemyType.createSkeleton(centerRow, centerCol, mazeMap)
        );
    }

    // ================= Player Animation Setup =================

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

    // ================= Enemy Animation Setup =================

    private void setupEnemyAnimators() {

        enemyAnimators.clear();

        for (Enemy enemy : enemies) {

            Animator animator = new Animator();

            String type = enemy.getTypeName();

            if (type.equals("slime")) {

                addSlimeClips(animator);

            } else if (type.equals("goblin")) {

                addGoblinClips(animator);

            } else if (type.equals("skeleton")) {

                addSkeletonClips(animator);
            }

            animator.setFrameSpeed(8);

            enemyAnimators.put(enemy, animator);
        }
    }

    private void addSlimeClips(Animator animator) {

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
    }

    private void addGoblinClips(Animator animator) {

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
    }

    private void addSkeletonClips(Animator animator) {

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

    // ================= Update =================

    private void update() {

        if (gameEnded) {
            return;
        }

        Player player = gameManager.getPlayer();

        if (player != null) {
            player.setMoving(false);
        }

        updateCooldowns();

        updatePlayerAttackState(player);

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

    private void updateCooldowns() {

        if (moveCooldown > 0) {
            moveCooldown--;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (playerDamageCooldown > 0) {
            playerDamageCooldown--;
        }

        if (playerHitFlashTimer > 0) {
            playerHitFlashTimer--;
        }
    }

    private void updatePlayerAttackState(Player player) {

        if (playerAttackTimer > 0) {

            playerAttackTimer--;

            if (playerAttackTimer == 0 && player != null) {
                player.stopAttack();
            }
        }
    }

    // ================= Player Animation Update =================

    private void updatePlayerAnimation() {

        Player player = gameManager.getPlayer();

        if (player == null || playerAnimator == null) {
            return;
        }

        String directionKey = getDirectionKey(player.getDirection());
        String weaponKey = getWeaponKey(player.getWeaponName());

        if (player.isAttacking()) {

            playerAnimator.play(
                    weaponKey + "_attack_" + directionKey,
                    false
            );

            playerAnimator.update();

        } else {

            playerAnimator.setClipFrame(
                    weaponKey + "_walk_" + directionKey,
                    player.getWalkFrameIndex()
            );
        }
    }

    // ================= Enemy Animation Update =================

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

            String directionKey = getHorizontalDirectionKey(enemy.getDirection());

            animator.play(
                    "attack_" + directionKey,
                    false
            );

            animator.update();

        } else {

            animator.play(
                    "idle",
                    true
            );

            animator.update();
        }
    }

    private void updateGoblinAnimation(Enemy enemy, Animator animator) {

        String directionKey = getHorizontalDirectionKey(enemy.getDirection());

        if (enemy.isAttacking()) {

            animator.play(
                    "attack_" + directionKey,
                    false
            );

            animator.update();

        } else {

            animator.setClipFrame(
                    "walk_" + directionKey,
                    enemy.getWalkFrameIndex()
            );
        }
    }

    private void updateSkeletonAnimation(Enemy enemy, Animator animator) {

        String directionKey = getDirectionKey(enemy.getDirection());

        if (enemy.isAttacking()) {

            animator.play(
                    "attack_" + directionKey,
                    false
            );

            animator.update();

        } else {

            animator.setClipFrame(
                    "walk_" + directionKey,
                    enemy.getWalkFrameIndex()
            );
        }
    }

    // ================= Player Movement and Attack =================

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

    private void playerAttack() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        if (attackCooldown > 0) {
            return;
        }

        attackCooldown = ATTACK_COOLDOWN_FRAMES;

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

    // ================= Enemy Logic =================

    private void updateEnemies() {

        enemyMoveCounter++;

        if (enemyMoveCounter % ENEMY_MOVE_INTERVAL != 0) {
            return;
        }

        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

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

                playerDamageCooldown = PLAYER_DAMAGE_COOLDOWN_FRAMES;

                playerHitFlashTimer = PLAYER_HIT_FLASH_DURATION;

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

    // ================= Key and Level Checks =================

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

    private void checkPortal() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int row = player.getRow();
        int col = player.getCol();

        if (mazeMap.isPortal(row, col) && player.hasEnoughKeys()) {
            gameManager.nextMap();
        }
    }

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

    private void checkBossDefeated() {

        if (mazeMap.isFinalLevel() && enemies.isEmpty()) {
            gameManager.showVictory();
        }
    }

    // ================= Rendering =================

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

    private void drawPlayer() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        int x = player.getCol() * TILE_SIZE - getSpriteOffset(SPRITE_SIZE);
        int y = player.getRow() * TILE_SIZE - getSpriteOffset(SPRITE_SIZE);

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

            drawPlayerFallback(player);
        }

        if (playerHitFlashTimer > 0) {
            drawPlayerHitEffect(player);
        }
    }

    private void drawPlayerFallback(Player player) {

        gc.setFill(Color.DODGERBLUE);

        gc.fillOval(
                player.getCol() * TILE_SIZE + 8,
                player.getRow() * TILE_SIZE + 8,
                TILE_SIZE - 16,
                TILE_SIZE - 16
        );
    }

    private void drawPlayerHitEffect(Player player) {

        int x = player.getCol() * TILE_SIZE;
        int y = player.getRow() * TILE_SIZE;

        if ((playerHitFlashTimer / 4) % 2 == 0) {

            gc.setGlobalAlpha(0.35);
            gc.setFill(Color.RED);

            gc.fillRect(
                    x,
                    y,
                    TILE_SIZE,
                    TILE_SIZE
            );

            gc.setGlobalAlpha(1.0);
        }

        gc.setStroke(Color.RED);
        gc.setLineWidth(3);

        gc.strokeRect(
                x + 2,
                y + 2,
                TILE_SIZE - 4,
                TILE_SIZE - 4
        );

        gc.setFill(Color.RED);

        gc.fillText(
                "-HP",
                x + 10,
                y - 6
        );

        gc.setLineWidth(1);
    }

    private void drawEnemies() {

        for (Enemy enemy : enemies) {

            int size = getEnemySpriteSize(enemy);
            int offset = getSpriteOffset(size);

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

            } else if (enemy.getSprite() != null
                    && enemy.getSprite().getImage() != null) {

                gc.drawImage(
                        enemy.getSprite().getImage(),
                        x,
                        y,
                        size,
                        size
                );

            } else {

                drawEnemyFallback(enemy);
            }

            drawEnemyHealthBar(
                    enemy,
                    enemy.getCol() * TILE_SIZE,
                    enemy.getRow() * TILE_SIZE
            );
        }
    }

    private void drawEnemyFallback(Enemy enemy) {

        gc.setFill(Color.RED);

        gc.fillOval(
                enemy.getCol() * TILE_SIZE + 8,
                enemy.getRow() * TILE_SIZE + 8,
                TILE_SIZE - 16,
                TILE_SIZE - 16
        );
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

    private void drawHUD() {

        Player player = gameManager.getPlayer();

        if (player == null) {
            return;
        }

        gc.setFill(Color.rgb(0, 0, 0, 0.65));

        gc.fillRoundRect(
                HUD_X,
                HUD_Y,
                HUD_WIDTH,
                HUD_HEIGHT,
                15,
                15
        );

        gc.setStroke(Color.GOLD);

        gc.strokeRoundRect(
                HUD_X,
                HUD_Y,
                HUD_WIDTH,
                HUD_HEIGHT,
                15,
                15
        );

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
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

    // ================= Input Handling =================

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

            return;
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
            moveCooldown = MOVE_COOLDOWN_FRAMES;

        } else if (pressedKeys.contains(KeyCode.A)) {

            movePlayer(Player.Direction.LEFT);
            moveCooldown = MOVE_COOLDOWN_FRAMES;

        } else if (pressedKeys.contains(KeyCode.S)) {

            movePlayer(Player.Direction.DOWN);
            moveCooldown = MOVE_COOLDOWN_FRAMES;

        } else if (pressedKeys.contains(KeyCode.D)) {

            movePlayer(Player.Direction.RIGHT);
            moveCooldown = MOVE_COOLDOWN_FRAMES;
        }
    }

    // ================= Helper Methods =================

    private int getEnemySpriteSize(Enemy enemy) {

        if (enemy.getTypeName().equals("skeleton")) {
            return SKELETON_SPRITE_SIZE;
        }

        return SPRITE_SIZE;
    }

    private int getSpriteOffset(int spriteSize) {
        return (spriteSize - TILE_SIZE) / 2;
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

    // ================= Getters =================

    public MazeMap getMazeMap() {
        return mazeMap;
    }
}