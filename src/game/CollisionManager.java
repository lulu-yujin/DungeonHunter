package map;

import player.Player;
import enemy.Enemy;
import map.MazeMap;

/**
 * CollisionManager — 碰撞检测管理器
 * CollisionManager — Handles all collision detection for the game.
 *
 * 负责：
 *   1. 玩家撞墙检测 / Player-wall collision
 *   2. 怪物撞墙检测 / Enemy-wall collision
 *   3. 地图边界检测 / Map boundary checks
 *
 * 所有检测基于格子坐标（row / col），不使用像素坐标。
 * All checks use tile coordinates (row / col), never raw pixel coords.
 */
public class CollisionManager {

    // ── 依赖地图 / Depends on the map ─────────────────────────────────────
    private final MazeMap mazeMap;

    /**
     * 构造方法 / Constructor
     *
     * @param mazeMap 当前关卡地图 / Current level's map
     */
    public CollisionManager(MazeMap mazeMap) {
        this.mazeMap = mazeMap;
    }

    // ─────────────────────────────────────────────────────────────────────
    // checkPlayerCollision() — 检查玩家目标格子是否可移动
    //                        — Check whether the player can move to a target cell
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 检测玩家能否移动到 (targetRow, targetCol)。
     * Check whether the player can move to (targetRow, targetCol).
     *
     * 调用方式 / How to call (in Player.move()):
     *   int nextRow = player.getRow() + deltaRow;
     *   int nextCol = player.getCol() + deltaCol;
     *   if (collisionManager.checkPlayerCollision(player, nextRow, nextCol)) {
     *       player.setRow(nextRow);
     *       player.setCol(nextCol);
     *   }
     *
     * @param player    玩家对象 / Player object
     * @param targetRow 目标行   / Target row
     * @param targetCol 目标列   / Target col
     * @return true = 可以移动到那里 / true = movement is allowed
     */
    public boolean checkPlayerCollision(Player player, int targetRow, int targetCol) {
        // 1. 边界检测 / boundary check
        if (!isWithinBounds(targetRow, targetCol)) return false;

        // 2. 墙壁检测 / wall check
        if (!mazeMap.isWalkable(targetRow, targetCol)) return false;

        // 通过：可以移动 / Passed: movement allowed
        return true;
    }

    // ─────────────────────────────────────────────────────────────────────
    // checkEnemyCollision() — 检查怪物目标格子是否可移动
    //                       — Check whether an enemy can move to a target cell
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 检测怪物能否移动到 (targetRow, targetCol)。
     * Check whether an enemy can move to (targetRow, targetCol).
     *
     * 逻辑与玩家相同；将来可在此加入"怪物专属通道"等特殊规则。
     * Logic mirrors the player check; special enemy-only rules can be added here later.
     *
     * @param enemy     怪物对象 / Enemy object
     * @param targetRow 目标行   / Target row
     * @param targetCol 目标列   / Target col
     * @return true = 怪物可移动到那里 / true = enemy movement allowed
     */
    public boolean checkEnemyCollision(Enemy enemy, int targetRow, int targetCol) {
        // 1. 边界检测 / boundary check
        if (!isWithinBounds(targetRow, targetCol)) return false;

        // 2. 墙壁检测 / wall check
        if (!mazeMap.isWalkable(targetRow, targetCol)) return false;

        return true;
    }

    // ─────────────────────────────────────────────────────────────────────
    // isWithinBounds() — 地图边界检测工具方法
    //                  — Utility: check if a cell is within map bounds
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 检测 (row, col) 是否在地图范围内。
     * Check whether (row, col) is inside the map boundaries.
     *
     * @param row 行 / row
     * @param col 列 / col
     * @return true = 在边界内 / true = within bounds
     */
    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < MazeMap.ROWS
            && col >= 0 && col < MazeMap.COLS;
    }
    //检测是否踩到通道口
    public boolean isOnPortal(int row, int col) {
        return mazeMap.getChar(row, col) == MazeMap.PORTAL;
    }
}