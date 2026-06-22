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

class Slime extends Enemy {

    public Slime(int row, int col, MazeMap mazeMap) {
        super(row, col, "/slime.png", mazeMap);
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
        super(row, col, "/goblin.png", mazeMap);
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

class Skeleton extends Enemy {

    private int frameCount = 0;

    public Skeleton(int row, int col, MazeMap mazeMap) {
        super(row, col, "/skeleton.png", mazeMap);
    }

    @Override
    public void move() {

        frameCount++;

        if (frameCount % 100 == 0) {
            sprite.setScaleX(sprite.getScaleX() * -1);
        }
    }
}