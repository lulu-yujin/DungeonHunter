package enemy;

class Enemies extends Enemy {

    public Enemies(int row, int col) {
        super(row, col, "/slime.png");
    }

    @Override
    public void move() {

        int nextRow = row + ((int) (Math.random() * 3) - 1);
        int nextCol = col + ((int) (Math.random() * 3) - 1);

        row = nextRow;
        col = nextCol;

        updateViewPosition();
    }
}

class Goblin extends Enemy {

    private int dir = 1;

    public Goblin(int row, int col) {
        super(row, col, "/goblin.png");
    }

    @Override
    public void move() {

        col += dir;

        if (col > 18 || col < 1) {
            dir *= -1;
        }

        updateViewPosition();
    }
}

class Skeleton extends Enemy {

    private int frameCount = 0;

    public Skeleton(int row, int col) {
        super(row, col, "/skeleton.png");
    }

    @Override
    public void move() {

        frameCount++;

        if (frameCount % 100 == 0) {
            sprite.setScaleX(sprite.getScaleX() * -1);
        }
    }
}