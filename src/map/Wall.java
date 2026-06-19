package map;

/**
 * Wall — 继承 Tile，表示不可通行的墙壁
 * Wall — Extends Tile; represents an impassable wall cell.
 *
 * walkable 固定为 false，其他逻辑与 Tile 相同。
 * walkable is always false; everything else behaves like a Tile.
 */
public class Wall extends Tile {

    /**
     * 构造方法 / Constructor
     *
     * @param row 格子行号 / Grid row index
     * @param col 格子列号 / Grid column index
     */
    public Wall(int row, int col) {
        // 调用父类构造，walkable = false（墙不可通行）
        // Call super constructor with walkable = false (walls block movement)
        super(row, col, false);
    }

    @Override
    public String toString() {
        return String.format("Wall[row=%d, col=%d]", row, col);
    }
}