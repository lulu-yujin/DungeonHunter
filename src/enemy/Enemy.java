package enemy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import map.MazeMap;

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

    public Enemy(
            int row,
            int col,
            String imagePath,
            MazeMap mazeMap,
            int maxHp,
            int damage,
            int coinReward
    ) {
        this.row = row;
        this.col = col;
        this.mazeMap = mazeMap;

        this.maxHp = maxHp;
        this.hp = maxHp;
        this.damage = damage;
        this.coinReward = coinReward;

        Image image = new Image(
                getClass().getResourceAsStream(imagePath)
        );

        sprite = new ImageView(image);

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
}