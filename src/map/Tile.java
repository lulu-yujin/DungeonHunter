package map;

/**
 * Tile — Base class for every cell in the maze map.
 *
 * 按照技术规范，所有坐标使用 row / col（格子坐标），
 * 显示时再乘以 TILE_SIZE 转换为像素。
 * Per the tech spec, all positions use row/col (tile coords).
 * Multiply by TILE_SIZE when you need pixel coordinates.
 */
public class Tile {

    // ── 统一格子尺寸 / Unified tile size ──────────────────────────────────
    /** 1 格 = 32×32 像素 / 1 cell = 48x48 px */
    public static final int TILE_SIZE = 48;

    // ── 格子位置（格子坐标）/ Tile position (grid coords) ─────────────────
    protected int row;
    protected int col;

    // ── 是否可行走 / Whether the tile can be walked on ────────────────────
    protected boolean walkable;

    /**
     * 构造方法 / Constructor
     *
     * @param row      格子行号 / Grid row index
     * @param col      格子列号 / Grid column index
     * @param walkable 是否可通行 / Whether entities can walk here
     */
    public Tile(int row, int col, boolean walkable) {
        this.row      = row;
        this.col      = col;
        this.walkable = walkable;
    }

    // ── Getters ───────────────────────────────────────────────────────────

    /** 返回行号 / Return row index */
    public int getRow() { return row; }

    /** 返回列号 / Return column index */
    public int getCol() { return col; }

    /** 是否可行走 / Whether walkable */
    public boolean isWalkable() { return walkable; }

    /**
     * 返回像素 X（左上角）/ Return pixel X of top-left corner
     * pixelX = col * TILE_SIZE
     */
    public int getPixelX() { return col * TILE_SIZE; }

    /**
     * 返回像素 Y（左上角）/ Return pixel Y of top-left corner
     * pixelY = row * TILE_SIZE
     */
    public int getPixelY() { return row * TILE_SIZE; }

    @Override
    public String toString() {
        return String.format("Tile[row=%d, col=%d, walkable=%b]", row, col, walkable);
    }
}