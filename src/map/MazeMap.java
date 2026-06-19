package map;

/**
 * MazeMap — 负责加载、生成、存储三层地图数据
 * MazeMap — Loads, generates, and stores all three level map data.
 *
 * 地图字符说明 / Tile character reference:
 *   '#' = 墙          Wall
 *   '.' = 地板        Floor
 *   'S' = 出生点      Player spawn (level 1 only)
 *   'E' = 出口        Exit (level 3 only)
 *   'T' = 通道口      Portal to next level
 *   'B' = 初级怪物区  Beginner zone floor
 *   'A' = 高级怪物区  Advanced zone floor
 *   'X' = Boss 区     Boss zone floor
 *
 * 规格 / Spec:
 *   ROWS = 15, COLS = 20  →  窗口 640×480 px (20*32 × 15*32)
 *   共三层 / Three levels total
 */
public class MazeMap {

    // ── 地图规格 / Map dimensions ──────────────────────────────────────────
    public static final int ROWS = 24;
    public static final int COLS = 32;

    // ── 字符常量 / Tile character constants ───────────────────────────────
    public static final char WALL     = '#';
    public static final char FLOOR    = '.';
    public static final char SPAWN    = 'S';
    public static final char EXIT     = 'E';
    public static final char PORTAL   = 'T'; // 通道口 / Portal to next level
    public static final char BEGINNER = 'B'; // 初级怪物区 / Beginner zone
    public static final char ADVANCED = 'A'; // 高级怪物区 / Advanced zone
    public static final char BOSS     = 'X'; // Boss 区 / Boss zone

    // ── 三层地图数据 / All three level maps ───────────────────────────────
    private final char[][][] allMaps = new char[3][ROWS][COLS];

    // ── 当前层（1-3）/ Current level (1–3) ────────────────────────────────
    private int currentLevel = 1;

    // ── Tile 对象网格 / Tile object grid ──────────────────────────────────
    private Tile[][] tiles;

    /**
     * 构造方法：加载全部三层地图，默认从第一层开始
     * Constructor: loads all three levels; starts on level 1.
     */
    public MazeMap() {
        tiles = new Tile[ROWS][COLS];
        loadAllMaps();
        buildTiles(); // 根据当前层构建 Tile / Build tiles for current level
    }

    // ─────────────────────────────────────────────────────────────────────
    // loadAllMaps() — 加载全部三层
    //              — Load all three level layouts
    // ─────────────────────────────────────────────────────────────────────

    private void loadAllMaps() {
        loadMap1();
        loadMap2();
        loadMap3();
    }

    // ─────────────────────────────────────────────────────────────────────
    // 第一层：初级怪物 + 出生区
    // Level 1: Beginner monsters + spawn area
    // 地形简单，少量矩形短墙，出口通道 T 在右侧中部
    // Simple terrain, few short walls, portal T on right side
    // ─────────────────────────────────────────────────────────────────────

    private void loadMap1() {
        String[] layout = {
            "################################",
            "#S..#...B........B....B...BBBB.#",
            "#...#...B........B....B...BBBB.#",
            "#...#########....B....B...BBBB.#",
            "#.......#..BB....B....B........#",
            "#..BB...#........B.............#",
            "#..BB...#..BB....B....######...#",
            "#.......#..BB....B....#B.......#",
            "#.......#..BB..########........#",
            "#.B..B..######....#...B.B......#",
            "#.B..B......BB....#...B.B......#",
            "#...........BB....#...B.B......#",
            "#......B#.............B.B......#",
            "#......B#......B......B.B......#",
            "#.......#.......B....######....T",
            "#.###...#......B.....#.....BB..#",
            "#.###...#......B.....#.....BB..#",
            "#.......##############.........#",
            "#...B....B........BB...........#",
            "#...B....B.......BB............#",
            "#........B.......B....B...BB...#",
            "#........B.......B....B...BB...#",
            "#.................B....B.......#",
            "################################"
        };
        fillLevel(0, layout);
    }


    // ─────────────────────────────────────────────────────────────────────
    // 第二层：高级怪物区
    // Level 2: Advanced monster zone
    // 地形复杂：T型隔墙、L型拐角、长竖墙
    // Complex terrain: T-walls, L-corners, long vertical walls
    // ─────────────────────────────────────────────────────────────────────

    private void loadMap2() {
        String[] layout = {
            "################################",
            "#....A....A........A...AAAA....#",
            "#......######A......A...AAAA...#",
            "#......#....AA......A..........#",
            "#.AAAA.#....AA......AAA........#",
            "#.AAAA.#....A........AAA.......T",
            "#.AAAA.######........A.........#",
            "#......A.............A...##....#",
            "#......A.............A...##....#",
            "#.####AAA....##................#",
            "#.#...AAA....##....AAA....###..#",
            "#.#......A...........AAA..#..#.#",
            "#.#......A...........AAA..#..#.#",
            "#........A...##......A....#..#.#",
            "T........A...##......A.......#.#",
            "#.....###.....AAA............#.#",
            "#.....###.....AAA............#.#",
            "#..............A...####........#",
            "#..A...........A...#A#.........#",
            "#..A.....AAAA..................#",
            "#..A.....AAAA..........AAA.....#",
            "#........................AAA...#",
            "#........................AAA...#",
            "################################"
        };
        fillLevel(1, layout);
    }


    // ─────────────────────────────────────────────────────────────────────
    // 第三层：Boss 通关区
    // Level 3: Boss arena
    // 场地开阔，少量零散障碍，出口 E 在右下
    // Open arena, sparse obstacles, exit E bottom-right
    // ─────────────────────────────────────────────────────────────────────

    private void loadMap3() {
        String[] layout = {
            "################################",
            "#XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXXX#######XXXXXXXX#",
            "#XXXXX####XXXXXXXXXXXX#XXXXXXXX#",
            "#XXXXXXXXXXXXXXXXXXXXX#XXXXXXXX#",
            "#XXXXX#XXXXXXXXXXXXXXX#XXXXXXXX#",
            "TXXXXX#XXXXXXX##XXXXXX#XXXXXXXX#",
            "#XXXXX#XXXXXXXXXXXXXXXXXXXXXXXX#",
            "#XXXXX#XXXXXXXXXXXXXXXXXXXXXXXX#",
            "#XXXXX#XXXX#####XXXXXXXXXXXXXXX#",
            "#XXXXX#XXXXXXXX#XXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXX#XXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXX#XXXXXX#XXXXXXXX#",
            "#XXXXXXXXXXXXXX#XXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXX########XXXXXXXX#",
            "#XXXXXXXXXX##XXXXXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXXXXXXXXXXXX###XXX#",
            "######XXXXXXXXX##XXXXXXXX#XXXXX#",
            "#XXXXXXXXXXXXXX####XXXXXX#XXXXX#",
            "#XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX#",
            "#XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX#",
            "###############################E"
        };
        fillLevel(2, layout);
    }

    // ─────────────────────────────────────────────────────────────────────
    // fillLevel() — 把 String[] 写入 allMaps[levelIndex]
    //             — Write a String[] layout into allMaps[levelIndex]
    // ─────────────────────────────────────────────────────────────────────

    private void fillLevel(int levelIndex, String[] layout) {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                allMaps[levelIndex][r][c] = layout[r].charAt(c);
    }

    // ─────────────────────────────────────────────────────────────────────
    // buildTiles() — 根据当前层 char[][] 生成 Tile 对象
    //             — Build Tile objects from the current level's char grid
    // ─────────────────────────────────────────────────────────────────────

    private void buildTiles() {
        char[][] map = getMap();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (map[r][c] == WALL) {
                    tiles[r][c] = new Wall(r, c);
                } else {
                    tiles[r][c] = new Tile(r, c, true);
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // loadLevel() — 切换到指定层并重建 Tile
    //            — Switch to the given level and rebuild tiles
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 切换到指定层（1-3）。
     * Switch to the specified level (1–3).
     * 玩家/怪物系统在调用此方法后应重置自身位置。
     * Player/enemy systems should reset their positions after calling this.
     *
     * @param level 目标层编号 1-3 / Target level number 1–3
     */
    public void loadLevel(int level) {
        if (level < 1 || level > 3) return;
        currentLevel = level;
        buildTiles();
    }

    /**
     * 前往下一层（最多第3层）。
     * Advance to the next level (max level 3).
     */
    public void nextLevel() {
        loadLevel(currentLevel + 1);
    }

    /**
     * 是否已是最终层（Boss 层）。
     * Whether the current level is the final (Boss) level.
     */
    public boolean isFinalLevel() {
        return currentLevel == 3;
    }

    // ─────────────────────────────────────────────────────────────────────
    // drawMap() — 控制台调试输出
    //           — Console debug print
    // ─────────────────────────────────────────────────────────────────────

    public void drawMap() {
        char[][] map = getMap();
        System.out.println("=== Level " + currentLevel + " ===");
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++)
                System.out.print(map[r][c]);
            System.out.println();
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────

    /**
     * 返回当前层的 char[][] 地图。
     * Return the char[][] map for the current level.
     */
    public char[][] getMap() {
        return allMaps[currentLevel - 1];
    }

    /** 返回 Tile 对象网格 / Return Tile object grid */
    public Tile[][] getTiles() { return tiles; }

    /** 返回当前层编号（1-3）/ Return current level number (1–3) */
    public int getCurrentLevel() { return currentLevel; }

    /**
     * 获取指定格子的字符 / Get the char at a specific cell
     */
    public char getChar(int row, int col) {
        return getMap()[row][col];
    }

    /**
     * 获取指定格子的 Tile 对象 / Get the Tile object at a specific cell
     */
    public Tile getTile(int row, int col) { return tiles[row][col]; }

    /**
     * 检查某格子是否可行走 / Check whether a given cell is walkable
     */
    public boolean isWalkable(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return false;
        return tiles[row][col].isWalkable();
    }

    /**
     * 找出当前层出生点 'S' 的坐标。
     * Find the spawn point 'S' in the current level.
     * 第2、3层入口在 T 格，这里统一返回 T 或 S 的位置。
     * Levels 2 & 3 start at portal T; this returns S or T accordingly.
     *
     * @return int[]{row, col}，找不到返回 {1, 1}
     */
    public int[] getSpawnPoint() {
        char[][] map = getMap();
        char target = (currentLevel == 1) ? SPAWN : PORTAL;
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (map[r][c] == target) return new int[]{r, c};
        return new int[]{1, 1}; // 默认值 / fallback
    }

    /**
     * 检查指定格子是否是通道口 'T'。
     * Check whether a cell is a portal tile 'T'.
     */
    public boolean isPortal(int row, int col) {
        return getChar(row, col) == PORTAL;
    }

    /**
     * 检查指定格子是否是出口 'E'。
     * Check whether a cell is the exit tile 'E'.
     */
    public boolean isExit(int row, int col) {
        return getChar(row, col) == EXIT;
    }
}