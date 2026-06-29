package enemy;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import map.MazeMap;
import player.Player;

/**
 * Abstract parent class for all enemies.
 * It stores common enemy data such as position, HP, damage, direction and attack state.
 */
public abstract class Enemy {

    // ================= Constants =================

    public static final int TILE_SIZE = 48;

    // ================= Position and Map =================

    protected int row;
    protected int col;

    protected MazeMap mazeMap;

    // ================= Display =================

    protected ImageView sprite;

    // ================= Combat Data =================

    protected int maxHp;
    protected int hp;
    protected int damage;
    protected int coinReward;

    // ================= Enemy Type and Animation State =================

    protected String typeName;

    protected Player.Direction direction = Player.Direction.RIGHT;

    protected boolean attacking = false;

    protected int walkFrameIndex = 0;

    // ================= Constructor =================

    public Enemy(
            int row,
            int col,
            String typeName,
            String imagePath,
            MazeMap mazeMap,
            int maxHp,
            int damage,
            int coinReward
    ) {
        this.row = row;
        this.col = col;
        this.typeName = typeName;
        this.mazeMap = mazeMap;

        this.maxHp = maxHp;
        this.hp = maxHp;
        this.damage = damage;
        this.coinReward = coinReward;

        loadSprite(imagePath);

        updateViewPosition();
    }

    // ================= Abstract Behaviour =================

    /**
     * Each enemy type has its own movement behaviour.
     */
    public abstract void move();

    // ================= Sprite Loading =================

    private void loadSprite(String imagePath) {

        InputStream stream = getClass().getResourceAsStream(imagePath);

        if (stream != null) {

            Image image = new Image(stream);
            sprite = new ImageView(image);

        } else {

            System.err.println("[Enemy] Cannot load image: " + imagePath);
            sprite = new ImageView();
        }

        sprite.setFitWidth(TILE_SIZE);
        sprite.setFitHeight(TILE_SIZE);
    }

    // ================= Position Getters =================

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public ImageView getSprite() {
        return sprite;
    }

    // ================= Combat Getters =================

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getCoinReward() {
        return coinReward;
    }

    // ================= Type and Direction =================

    public String getTypeName() {
        return typeName;
    }

    public Player.Direction getDirection() {
        return direction;
    }

    public void setDirection(Player.Direction direction) {
        this.direction = direction;
    }

    // ================= Animation State =================

    public int getWalkFrameIndex() {
        return walkFrameIndex;
    }

    protected void nextWalkFrame() {
        walkFrameIndex++;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void startAttack() {
        attacking = true;
    }

    public void stopAttack() {
        attacking = false;
    }

    // ================= Combat Methods =================

    /**
     * Reduces enemy HP when it is hit by the player.
     */
    public void takeDamage(int amount) {

        hp -= amount;

        if (hp < 0) {
            hp = 0;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    // ================= Movement Helpers =================

    /**
     * Checks whether the enemy can move to the target tile.
     */
    protected boolean canMoveTo(int nextRow, int nextCol) {
        return mazeMap != null && mazeMap.isWalkable(nextRow, nextCol);
    }

    /**
     * Updates the JavaFX sprite position according to the enemy's grid position.
     */
    protected void updateViewPosition() {
        sprite.setX(col * TILE_SIZE);
        sprite.setY(row * TILE_SIZE);
    }

    /**
     * Updates enemy direction based on movement change.
     */
    protected void setDirectionByDelta(int dRow, int dCol) {

        if (dRow < 0) {
            direction = Player.Direction.UP;
        } else if (dRow > 0) {
            direction = Player.Direction.DOWN;
        } else if (dCol < 0) {
            direction = Player.Direction.LEFT;
        } else if (dCol > 0) {
            direction = Player.Direction.RIGHT;
        }
    }
}