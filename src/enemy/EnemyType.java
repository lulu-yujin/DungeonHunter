package enemy;

import map.MazeMap;

/**
 * Factory class used to create different enemy objects.
 */
public class EnemyType {

    public static Enemy createSlime(int row, int col, MazeMap mazeMap) {
        return new Slime(row, col, mazeMap);
    }

    public static Enemy createGoblin(int row, int col, MazeMap mazeMap) {
        return new Goblin(row, col, mazeMap);
    }

    public static Enemy createSkeleton(int row, int col, MazeMap mazeMap) {
        return new Skeleton(row, col, mazeMap);
    }
}

/**
 * Slime is a basic enemy.
 * It does not move and only plays idle or attack animation.
 */
class Slime extends Enemy {

    private static final int MAX_HP = 30;
    private static final int DAMAGE = 5;
    private static final int COIN_REWARD = 20;

    public Slime(int row, int col, MazeMap mazeMap) {
        super(
                row,
                col,
                "slime",
                "/enemy/slime.png",
                mazeMap,
                MAX_HP,
                DAMAGE,
                COIN_REWARD
        );
    }

    @Override
    public void move() {
        // Slime does not move.
        // Its idle animation is handled by GamePanel.
    }
}

/**
 * Goblin is a middle-level enemy.
 * It only moves horizontally.
 */
class Goblin extends Enemy {

    private static final int MAX_HP = 50;
    private static final int DAMAGE = 10;
    private static final int COIN_REWARD = 35;

    private static final int RIGHT = 1;

    private int horizontalDirection = RIGHT;

    public Goblin(int row, int col, MazeMap mazeMap) {
        super(
                row,
                col,
                "goblin",
                "/enemy/goblin.png",
                mazeMap,
                MAX_HP,
                DAMAGE,
                COIN_REWARD
        );
    }

    @Override
    public void move() {

        int nextCol = col + horizontalDirection;

        if (canMoveTo(row, nextCol)) {

            moveHorizontally(nextCol);

        } else {

            reverseDirection();

            nextCol = col + horizontalDirection;

            if (canMoveTo(row, nextCol)) {
                moveHorizontally(nextCol);
            }
        }

        updateViewPosition();
    }

    private void moveHorizontally(int nextCol) {

        setDirectionByDelta(0, horizontalDirection);

        col = nextCol;

        nextWalkFrame();
    }

    private void reverseDirection() {
        horizontalDirection *= -1;
    }
}

/**
 * Skeleton is the boss enemy.
 * It can move in four directions randomly.
 */
class Skeleton extends Enemy {

    private static final int MAX_HP = 120;
    private static final int DAMAGE = 15;
    private static final int COIN_REWARD = 0;

    public Skeleton(int row, int col, MazeMap mazeMap) {
        super(
                row,
                col,
                "skeleton",
                "/enemy/skeleton.png",
                mazeMap,
                MAX_HP,
                DAMAGE,
                COIN_REWARD
        );
    }

    @Override
    public void move() {

        int[] movement = getRandomMovement();

        int dRow = movement[0];
        int dCol = movement[1];

        int nextRow = row + dRow;
        int nextCol = col + dCol;

        if (canMoveTo(nextRow, nextCol)) {

            setDirectionByDelta(dRow, dCol);

            row = nextRow;
            col = nextCol;

            nextWalkFrame();

            updateViewPosition();
        }
    }

    private int[] getRandomMovement() {

        int random = (int) (Math.random() * 4);

        if (random == 0) {
            return new int[] {-1, 0};
        }

        if (random == 1) {
            return new int[] {1, 0};
        }

        if (random == 2) {
            return new int[] {0, -1};
        }

        return new int[] {0, 1};
    }
}