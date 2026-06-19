package map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * MapRenderer — 负责用 JavaFX 把地图贴图到 Canvas 上
 * MapRenderer — Renders the maze map onto a JavaFX Canvas using tile sprites.
 *
 * 贴图资源路径（放在 resources/images/map/ 目录下）：
 * Sprite paths (place these in resources/images/map/):
 *   floor.png    — 普通地板       / generic floor tile
 *   wall.png     — 墙             / wall tile
 *   spawn.png    — 出生点         / spawn tile
 *   exit.png     — 出口           / exit tile
 *   portal.png   — 通道口         / portal tile (新增 / new)
 *   beginner.png — 初级怪物区地板 / beginner zone floor
 *   advanced.png — 高级怪物区地板 / advanced zone floor
 *   boss.png     — Boss 区地板    / boss zone floor
 *
 * 如果贴图缺失，会自动退回纯色填充（方便开发阶段测试）。
 * If a sprite is missing, falls back to solid colors (handy during development).
 *
 * 用法 / Usage:
 *   Canvas canvas = new Canvas(MazeMap.COLS * Tile.TILE_SIZE,
 *                              MazeMap.ROWS * Tile.TILE_SIZE);
 *   MapRenderer renderer = new MapRenderer(mazeMap, canvas);
 *   renderer.render();   // 每帧调用 / call every frame
 */
public class MapRenderer {

    // ── 依赖 / Dependencies ────────────────────────────────────────────────
    private final MazeMap         mazeMap;
    private final Canvas          canvas;
    private final GraphicsContext gc;

    // ── 贴图 / Sprites ────────────────────────────────────────────────────
    private Image floorSprite;
    private Image wallSprite;
    private Image spawnSprite;
    private Image exitSprite;
    private Image portalSprite;    // 通道口 / portal to next level
    private Image beginnerSprite;  // 初级怪物区 / beginner zone
    private Image advancedSprite;  // 高级怪物区 / advanced zone
    private Image bossSprite;      // Boss 区 / boss zone

    // ── 备用颜色（贴图缺失时）/ Fallback colors (when sprites are missing) ──
    private static final Color COLOR_FLOOR    = Color.web("#8B7355"); // 棕色地板     / brown floor
    private static final Color COLOR_WALL     = Color.web("#3A3A4A"); // 深灰墙       / dark wall
    private static final Color COLOR_SPAWN    = Color.web("#4CAF50"); // 绿色出生点   / green spawn
    private static final Color COLOR_EXIT     = Color.web("#FFC107"); // 黄色出口     / yellow exit
    private static final Color COLOR_PORTAL   = Color.web("#9B59B6"); // 紫色通道口   / purple portal
    private static final Color COLOR_BEGINNER = Color.web("#5A7A40"); // 橄榄绿初级区 / olive beginner
    private static final Color COLOR_ADVANCED = Color.web("#7A3A2A"); // 深红高级区   / dark-red advanced
    private static final Color COLOR_BOSS     = Color.web("#8A1A1A"); // 暗红Boss区   / crimson boss

    /**
     * 构造方法 / Constructor
     *
     * @param mazeMap 已加载的地图数据 / Loaded map data
     * @param canvas  JavaFX Canvas，尺寸应为 COLS*32 × ROWS*32
     *                JavaFX Canvas sized COLS*32 × ROWS*32
     */
    public MapRenderer(MazeMap mazeMap, Canvas canvas) {
        this.mazeMap = mazeMap;
        this.canvas  = canvas;
        this.gc      = canvas.getGraphicsContext2D();
        loadSprites();
    }

    // ─────────────────────────────────────────────────────────────────────
    // loadSprites() — 加载所有贴图，失败时静默跳过（使用备用颜色）
    //               — Load all sprites; silently skip on failure (fallback colors)
    // ─────────────────────────────────────────────────────────────────────

    private void loadSprites() {
        floorSprite    = loadImage("/images/map/floor.png");
        wallSprite     = loadImage("/images/map/wall.png");
        spawnSprite    = loadImage("/images/map/spawn.png");
        exitSprite     = loadImage("/images/map/exit.png");
        portalSprite   = loadImage("/images/map/portal.png");   // 新增 / new
        beginnerSprite = loadImage("/images/map/beginner.png");
        advancedSprite = loadImage("/images/map/advanced.png");
        bossSprite     = loadImage("/images/map/boss.png");
    }

    /**
     * 安全加载单张图片，失败返回 null。
     * Safely load one image; returns null if the resource is not found.
     */
    private Image loadImage(String path) {
        try {
            var stream = getClass().getResourceAsStream(path);
            if (stream == null) return null;
            return new Image(stream);
        } catch (Exception e) {
            System.err.println("[MapRenderer] 无法加载贴图 / Failed to load sprite: " + path);
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // render() — 主渲染方法，逐格绘制当前层地图
    //          — Main render method; draws the current level map cell by cell
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 把当前层地图绘制到 Canvas 上。
     * Draw the current level map onto the Canvas.
     * 切换层后直接再调用一次即可刷新画面。
     * After switching levels, just call this again to refresh the canvas.
     */
    public void render() {
        int ts = Tile.TILE_SIZE; // 32px

        for (int r = 0; r < MazeMap.ROWS; r++) {
            for (int c = 0; c < MazeMap.COLS; c++) {

                double px = c * ts; // 像素 X / Pixel X
                double py = r * ts; // 像素 Y / Pixel Y

                char cell = mazeMap.getChar(r, c);

                switch (cell) {

                    case MazeMap.WALL:
                        // 墙 / Wall
                        drawTile(wallSprite, COLOR_WALL, px, py, ts);
                        break;

                    case MazeMap.SPAWN:
                        // 出生点 / Spawn point
                        drawTile(spawnSprite, COLOR_SPAWN, px, py, ts);
                        break;

                    case MazeMap.EXIT:
                        // 出口（第三层）/ Exit (level 3 only)
                        drawTile(floorSprite, COLOR_FLOOR, px, py, ts);
                        drawTile(exitSprite,  COLOR_EXIT,  px, py, ts);
                        break;

                    case MazeMap.PORTAL:
                        // 通道口：先画地板再叠通道贴图
                        // Portal: floor first, then portal overlay
                        drawTile(floorSprite,  COLOR_FLOOR,  px, py, ts);
                        drawTile(portalSprite, COLOR_PORTAL, px, py, ts);
                        break;

                    case MazeMap.BEGINNER:
                        // 初级怪物区 / Beginner zone
                        drawTile(beginnerSprite, COLOR_BEGINNER, px, py, ts);
                        break;

                    case MazeMap.ADVANCED:
                        // 高级怪物区 / Advanced zone
                        drawTile(advancedSprite, COLOR_ADVANCED, px, py, ts);
                        break;

                    case MazeMap.BOSS:
                        // Boss 区 / Boss zone
                        drawTile(bossSprite, COLOR_BOSS, px, py, ts);
                        break;

                    default: // '.' 普通地板 / generic floor
                        drawTile(floorSprite, COLOR_FLOOR, px, py, ts);
                        break;
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // drawTile() — 绘制单格：有贴图用贴图，否则用纯色
    //            — Draw one cell: use sprite if available, else solid color
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 绘制单个格子到 Canvas。
     * Draw a single tile onto the Canvas.
     *
     * @param sprite   贴图（可为 null）/ Sprite image (may be null)
     * @param fallback 备用颜色        / Fallback color if sprite is null
     * @param px       像素 X          / Pixel X
     * @param py       像素 Y          / Pixel Y
     * @param size     格子边长（像素）/ Cell side length in pixels
     */
    private void drawTile(Image sprite, Color fallback, double px, double py, int size) {
        if (sprite != null) {
            gc.drawImage(sprite, px, py, size, size);
        } else {
            gc.setFill(fallback);
            gc.fillRect(px, py, size, size);
        }
    }
}