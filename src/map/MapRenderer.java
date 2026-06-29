package map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Renders the current maze map onto a JavaFX Canvas.
 */
public class MapRenderer {

    // ================= Constants =================

    public static final int TILE_SIZE = Tile.TILE_SIZE;

    // ================= Fields =================

    private final MazeMap mazeMap;
    private final GraphicsContext gc;

    // ================= Sprites =================

    private Image floorSprite;
    private Image wallSprite;
    private Image spawnSprite;
    private Image exitSprite;
    private Image portalSprite;
    private Image beginnerSprite;
    private Image advancedSprite;
    private Image bossSprite;
    private Image keySprite;

    // ================= Constructor =================

    public MapRenderer(MazeMap mazeMap, Canvas canvas) {

        this.mazeMap = mazeMap;
        this.gc = canvas.getGraphicsContext2D();

        loadSprites();
    }

    // ================= Sprite Loading =================

    /**
     * Loads all map sprites from the resources folder.
     */
    private void loadSprites() {

        floorSprite = loadImage("/map/floor.png");
        wallSprite = loadImage("/map/wall.png");

        spawnSprite = loadImage("/map/door.png");
        exitSprite = loadImage("/map/door.png");
        portalSprite = loadImage("/map/door.png");

        beginnerSprite = loadImage("/map/floor.png");
        advancedSprite = loadImage("/map/floor.png");
        bossSprite = loadImage("/map/floor.png");

        keySprite = loadImage("/map/key.png");
    }

    /**
     * Loads an image from the resources folder.
     */
    private Image loadImage(String path) {

        var stream = getClass().getResourceAsStream(path);

        if (stream == null) {
            System.err.println("[MapRenderer] Missing sprite: " + path);
            return null;
        }

        return new Image(stream);
    }

    // ================= Rendering =================

    /**
     * Draws the whole current map onto the canvas.
     */
    public void render() {

        for (int row = 0; row < MazeMap.ROWS; row++) {
            for (int col = 0; col < MazeMap.COLS; col++) {

                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;

                drawCell(mazeMap.getChar(row, col), x, y);
            }
        }
    }

    /**
     * Draws one map cell according to its map symbol.
     */
    private void drawCell(char cell, double x, double y) {

        switch (cell) {

            case MazeMap.WALL:
                drawTile(wallSprite, x, y);
                break;

            case MazeMap.SPAWN:
                drawTile(spawnSprite, x, y);
                break;

            case MazeMap.EXIT:
                drawTile(floorSprite, x, y);
                drawTile(exitSprite, x, y);
                break;

            case MazeMap.PORTAL:
                drawTile(floorSprite, x, y);
                drawTile(portalSprite, x, y);
                break;

            case MazeMap.BEGINNER:
                drawTile(beginnerSprite, x, y);
                break;

            case MazeMap.ADVANCED:
                drawTile(advancedSprite, x, y);
                break;

            case MazeMap.BOSS:
                drawTile(bossSprite, x, y);
                break;

            case MazeMap.KEY:
                drawKeyTile(x, y);
                break;

            default:
                drawTile(floorSprite, x, y);
                break;
        }
    }

    /**
     * Draws a floor tile with a key image on top.
     */
    private void drawKeyTile(double x, double y) {

        drawTile(floorSprite, x, y);
        drawTile(keySprite, x, y);
    }

    /**
     * Draws one tile image.
     */
    private void drawTile(Image sprite, double x, double y) {

        if (sprite == null) {
            return;
        }

        gc.drawImage(
                sprite,
                x,
                y,
                TILE_SIZE,
                TILE_SIZE
        );
    }
}