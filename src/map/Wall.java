package map;

/**
 * Represents an impassable wall tile.
 */
public class Wall extends Tile {

    public Wall(int row, int col) {
        super(row, col, false);
    }

    @Override
    public String toString() {
        return String.format(
                "Wall[row=%d, col=%d]",
                row,
                col
        );
    }
}