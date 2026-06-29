package map;

/**
 * Represents a single tile in the maze map.
 * All positions are stored as row and column indexes.
 */
public class Tile {

    // ================= Constants =================

    public static final int TILE_SIZE = 48;

    // ================= Fields =================

    protected final int row;
    protected final int col;
    protected final boolean walkable;

    // ================= Constructor =================

    public Tile(int row, int col, boolean walkable) {

        this.row = row;
        this.col = col;
        this.walkable = walkable;
    }

    // ================= Getters =================

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public int getPixelX() {
        return col * TILE_SIZE;
    }

    public int getPixelY() {
        return row * TILE_SIZE;
    }

    // ================= Debug =================

    @Override
    public String toString() {
        return String.format(
                "Tile[row=%d, col=%d, walkable=%b]",
                row,
                col,
                walkable
        );
    }
}