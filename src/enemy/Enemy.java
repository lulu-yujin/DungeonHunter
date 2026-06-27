package enemy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import map.MazeMap;
import player.Player;

public abstract class Enemy {

    protected int row;
    protected int col;

    public static final int TILE_SIZE = 48;

    protected ImageView sprite;

    protected MazeMap mazeMap;

    protected int maxHp;
    protected int hp;
    protected int damage;
    protected int coinReward;

    protected String typeName;

    protected Player.Direction direction = Player.Direction.RIGHT;

    protected boolean attacking = false;

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

        var stream = getClass().getResourceAsStream(imagePath);

        if (stream != null) {
            Image image = new Image(stream);
            sprite = new ImageView(image);
        } else {
            System.err.println("[Enemy] Cannot load image: " + imagePath);
            sprite = new ImageView();
        }

        sprite.setFitWidth(TILE_SIZE);
        sprite.setFitHeight(TILE_SIZE);

        updateViewPosition();
    }

    public abstract void move();

    public ImageView getSprite() {
        return sprite;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

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

    public String getTypeName() {
        return typeName;
    }

    public Player.Direction getDirection() {
        return direction;
    }

    public void setDirection(Player.Direction direction) {
        this.direction = direction;
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

    public void takeDamage(int amount) {

        hp -= amount;

        if (hp < 0) {
            hp = 0;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    protected boolean canMoveTo(int nextRow, int nextCol) {
        return mazeMap != null && mazeMap.isWalkable(nextRow, nextCol);
    }

    protected void updateViewPosition() {
        sprite.setX(col * TILE_SIZE);
        sprite.setY(row * TILE_SIZE);
    }

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