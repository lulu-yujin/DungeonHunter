package enemy;

import map.MazeMap;

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

class Skeleton extends Enemy {

    public Skeleton(int row, int col, MazeMap mazeMap) {
        super(row, col, "/enemy/goblin.png", mazeMap, 120, 15, 100);
    }

    @Override
    public void move() {
        int nextRow = row + ((int) (Math.random() * 3) - 1);
        int nextCol = col + ((int) (Math.random() * 3) - 1);

        if (canMoveTo(nextRow, nextCol)) {
            row = nextRow;
            col = nextCol;
            updateViewPosition();
        }
    }
}

class Goblin extends Enemy {

    private int dir = 1;

    public Goblin(int row, int col, MazeMap mazeMap) {
        super(row, col, "/enemy/slime.png", mazeMap, 50, 10, 35);
    }

    @Override
    public void move() {
        int nextCol = col + dir;

        if (canMoveTo(row, nextCol)) {
            col = nextCol;
        } else {
            dir *= -1;

            nextCol = col + dir;

            if (canMoveTo(row, nextCol)) {
                col = nextCol;
            }
        }

        updateViewPosition();
    }
}

class Slime extends Enemy {

    private int frameCount = 0;

    public Slime(int row, int col, MazeMap mazeMap) {
        super(row, col, "/enemy/skeleton.png", mazeMap, 30, 5, 20);
    }

    @Override
    public void move() {
        frameCount++;

        if (frameCount % 100 == 0) {
            sprite.setScaleX(sprite.getScaleX() * -1);
        }
    }
}