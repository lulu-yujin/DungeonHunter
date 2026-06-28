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
        super(row, col, "slime", "/enemy/slime.png", mazeMap, 30, 5, 20);
    }

    @Override
    public void move() {
        // Slime does not move.
        // It only plays idle animation and left/right attack animation.
    }
}

class Goblin extends Enemy {

    private int dir = 1;

    public Goblin(int row, int col, MazeMap mazeMap) {
        super(row, col, "goblin", "/enemy/goblin.png", mazeMap, 50, 10, 35);
    }

    @Override
    public void move() {

        int nextCol = col + dir;

        if (canMoveTo(row, nextCol)) {

            setDirectionByDelta(0, dir);

            col = nextCol;
            
            nextWalkFrame();

        } else {

            dir *= -1;

            nextCol = col + dir;

            if (canMoveTo(row, nextCol)) {

                setDirectionByDelta(0, dir);

                col = nextCol;
                
                nextWalkFrame();
            }
        }

        updateViewPosition();
    }
}

class Skeleton extends Enemy {

    public Skeleton(int row, int col, MazeMap mazeMap) {
        super(row, col, "skeleton", "/enemy/skeleton.png", mazeMap, 120, 15, 0);
    }

    @Override
    public void move() {

        int random = (int) (Math.random() * 4);

        int dRow = 0;
        int dCol = 0;

        if (random == 0) {
            dRow = -1;
        } else if (random == 1) {
            dRow = 1;
        } else if (random == 2) {
            dCol = -1;
        } else {
            dCol = 1;
        }

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
}