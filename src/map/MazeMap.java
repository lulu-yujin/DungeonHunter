package map;

/**
 * Stores and manages all maze map data.
 * The map uses characters to represent different tile types.
 */
public class MazeMap {

    // ================= Constants =================

    public static final int ROWS = 15;
    public static final int COLS = 20;
    public static final int TOTAL_LEVELS = 3;

    public static final char WALL = '#';
    public static final char FLOOR = '.';
    public static final char SPAWN = 'S';
    public static final char KEY = 'K';
    public static final char EXIT = 'E';
    public static final char PORTAL = 'T';
    public static final char BEGINNER = 'B';
    public static final char ADVANCED = 'A';
    public static final char BOSS = 'X';

    // ================= Fields =================

    private final char[][][] allMaps = new char[TOTAL_LEVELS][ROWS][COLS];

    private int currentLevel = 1;

    private Tile[][] tiles;

    // ================= Constructor =================

    public MazeMap() {

        tiles = new Tile[ROWS][COLS];

        loadAllMaps();

        buildTiles();
    }

    // ================= Map Loading =================

    private void loadAllMaps() {

        loadMap1();

        loadMap2();

        loadMap3();
    }

    private void loadMap1() {

        String[] layout = {
                "####################",
                "#S.......B.........#",
                "#..###.............#",
                "#..#......B..#######",
                "#..#...B.....#..K..#",
                "#..#K........#.....#",
                "#..######..........#",
                "#.............B....#",
                "#....B.............#",
                "#........######....#",
                "#........#..K......#",
                "#...B....#.........#",
                "#........#......B..T",
                "#..........B.......#",
                "####################"
        };

        fillLevel(0, layout);
    }

    private void loadMap2() {

        String[] layout = {
                "####################",
                "T..................#",
                "#....A.....######..#",
                "####............#..#",
                "#..............A#..#",
                "#.............K.#..#",
                "#..####....######..#",
                "#..#........A......#",
                "#..#.............K.#",
                "#..#.......#########",
                "#..#......A........#",
                "#..######..........#",
                "#..........A.......T",
                "#K.................#",
                "####################"
        };

        fillLevel(1, layout);
    }

    private void loadMap3() {

        String[] layout = {
                "####################",
                "#XXXXXXXXXXXXXXXXXX#",
                "#XXXXXXX###XXXXXXXX#",
                "#XXXXXXX###XXXXXXXX#",
                "#XXXXXXXXXXXX#XXXXX#",
                "#XXXXXXXXXXXX#XXXXX#",
                "TXXXXX########XXXXX#",
                "#XXXXXXXXXXXXXXXXXX#",
                "#XXXX###XXXXXXXXXXX#",
                "#XXXX###XXXXXX######",
                "#XXXX###XXXXXX######",
                "#XXXXX####XXXXXXXXX#",
                "#XXXXX####XXX#XXXXX#",
                "#XXXXXXXXXXXX#XXXXE#",
                "####################"
        };

        fillLevel(2, layout);
    }

    private void fillLevel(int levelIndex, String[] layout) {

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                allMaps[levelIndex][row][col] = layout[row].charAt(col);
            }
        }
    }

    // ================= Tile Building =================

    /**
     * Builds Tile objects from the current character map.
     */
    private void buildTiles() {

        char[][] map = getMap();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                if (map[row][col] == WALL) {

                    tiles[row][col] = new Wall(row, col);

                } else {

                    tiles[row][col] = new Tile(row, col, true);
                }
            }
        }
    }

    // ================= Level Control =================

    /**
     * Switches to the selected level and rebuilds the tile grid.
     */
    public void loadLevel(int level) {

        if (level < 1 || level > TOTAL_LEVELS) {
            return;
        }

        currentLevel = level;

        buildTiles();
    }

    public boolean isFinalLevel() {
        return currentLevel == TOTAL_LEVELS;
    }

    // ================= Key Logic =================

    public boolean isKey(int row, int col) {

        if (!isInsideMap(row, col)) {
            return false;
        }

        return getChar(row, col) == KEY;
    }

    /**
     * Removes a collected key from the map.
     */
    public void removeKeyAt(int row, int col) {

        if (isKey(row, col)) {
            getMap()[row][col] = FLOOR;
        }
    }

    // ================= Tile Checks =================

    public boolean isWalkable(int row, int col) {

        if (!isInsideMap(row, col)) {
            return false;
        }

        return tiles[row][col].isWalkable();
    }

    public boolean isPortal(int row, int col) {

        if (!isInsideMap(row, col)) {
            return false;
        }

        return getChar(row, col) == PORTAL;
    }

    public boolean isExit(int row, int col) {

        if (!isInsideMap(row, col)) {
            return false;
        }

        return getChar(row, col) == EXIT;
    }

    private boolean isInsideMap(int row, int col) {

        return row >= 0
                && row < ROWS
                && col >= 0
                && col < COLS;
    }

    // ================= Spawn Point =================

    /**
     * Returns the player spawn position.
     * Level 1 uses S, while later levels use T as the entrance.
     */
    public int[] getSpawnPoint() {

        char[][] map = getMap();

        char target = currentLevel == 1 ? SPAWN : PORTAL;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                if (map[row][col] == target) {
                    return new int[] {row, col};
                }
            }
        }

        return new int[] {1, 1};
    }

    // ================= Getters =================

    public char[][] getMap() {
        return allMaps[currentLevel - 1];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public char getChar(int row, int col) {
        return getMap()[row][col];
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }
}