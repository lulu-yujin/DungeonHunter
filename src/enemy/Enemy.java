package enemy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Enemy {

    protected int row;
    protected int col;

    public static final int TILE_SIZE = 48;

    protected ImageView sprite;

    public Enemy(int row, int col, String imagePath) {

        this.row = row;
        this.col = col;

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

    protected void updateViewPosition() {
        sprite.setX(col * TILE_SIZE);
        sprite.setY(row * TILE_SIZE);
    }
}