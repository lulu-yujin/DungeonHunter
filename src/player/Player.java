package player;

public class Player {

    public enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        private final int dRow;
        private final int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }

        public int getDRow() {
            return dRow;
        }

        public int getDCol() {
            return dCol;
        }
    }

    public enum PlayerAction {
    	    NONE,
        PICK_KEY,
        NEED_KEY,
        NEXT_LEVEL,
        WIN
    }

    private int row;
    private int col;

    private int maxHp;
    private int hp;
    private int coins;
    private int damage;
    private int kills;

    private String weaponName;
    private Direction direction;
    private int keyCount;
    private static final int REQUIRED_KEYS = 3;

    // Player image path placeholders
    // The actual image files can be placed in res/player
    private String spriteUp;
    private String spriteDown;
    private String spriteLeft;
    private String spriteRight;

    private boolean moving;

    public Player() {
        this(1, 1);
    }

    public Player(int row, int col) {
        this.row = row;
        this.col = col;

        this.maxHp = 100;
        this.hp = 100;
        this.coins = 0;
        this.damage = 10;
        this.kills = 0;
        this.keyCount = 0;

        this.weaponName = "Wooden Sword";
        this.direction = Direction.DOWN;
        this.moving = false;

        this.spriteUp = "/player/player_up.png";
        this.spriteDown = "/player/player_down.png";
        this.spriteLeft = "/player/player_left.png";
        this.spriteRight = "/player/player_right.png";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getNextRow(Direction direction) {
        return row + direction.getDRow();
    }

    public int getNextCol(Direction direction) {
        return col + direction.getDCol();
    }

    public void move(Direction direction) {
        this.direction = direction;
        this.row += direction.getDRow();
        this.col += direction.getDCol();
        this.moving = true;
    }

    public void face(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void stopMoving() {
        this.moving = false;
    }

    public boolean isMoving() {
        return moving;
    }

    public String getCurrentSpritePath() {
        if (direction == Direction.UP) {
            return spriteUp;
        } else if (direction == Direction.DOWN) {
            return spriteDown;
        } else if (direction == Direction.LEFT) {
            return spriteLeft;
        } else if (direction == Direction.RIGHT) {
            return spriteRight;
        }

        return spriteDown;
    }

    public void setSpriteUp(String spriteUp) {
        this.spriteUp = spriteUp;
    }

    public void setSpriteDown(String spriteDown) {
        this.spriteDown = spriteDown;
    }

    public void setSpriteLeft(String spriteLeft) {
        this.spriteLeft = spriteLeft;
    }

    public void setSpriteRight(String spriteRight) {
        this.spriteRight = spriteRight;
    }

    public void setSprites(String up, String down, String left, String right) {
        this.spriteUp = up;
        this.spriteDown = down;
        this.spriteLeft = left;
        this.spriteRight = right;
    }

    public int getAttackRow() {
        return row + direction.getDRow();
    }

    public int getAttackCol() {
        return col + direction.getDCol();
    }

    public boolean isEnemyInAttackRange(int enemyRow, int enemyCol) {
        return enemyRow == getAttackRow() && enemyCol == getAttackCol();
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void heal(int amount) {
        this.hp += amount;

        if (this.hp > maxHp) {
            this.hp = maxHp;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        if (amount > 0) {
            this.coins += amount;
        }
    }

    public boolean spendCoins(int amount) {
        if (amount <= 0) {
            return true;
        }

        if (coins >= amount) {
            coins -= amount;
            return true;
        }

        return false;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public int getDamage() {
        return damage;
    }

    public void setWeapon(String weaponName, int damage) {
        this.weaponName = weaponName;
        this.damage = damage;
    }

    public boolean buyWeapon(String weaponName, int price, int damage) {
        if (spendCoins(price)) {
            setWeapon(weaponName, damage);
            return true;
        }

        return false;
    }

    public boolean buyHealthPotion(int price, int healAmount) {
        if (spendCoins(price)) {
            heal(healAmount);
            return true;
        }

        return false;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }
    
    public int getKeyCount() {
        return keyCount;
    }

    public int getRequiredKeys() {
        return REQUIRED_KEYS;
    }

    public boolean hasEnoughKeys() {
        return keyCount >= REQUIRED_KEYS;
    }

    public void pickUpKey() {
        keyCount++;
    }

    public void clearKeys() {
        keyCount = 0;
    }

    public boolean useKeysForNextLevel() {
        if (hasEnoughKeys()) {
            keyCount = 0;
            return true;
        }

        return false;
    }
    
    public PlayerAction checkCurrentTile(char tile) {
        if (tile == 'K') {
            pickUpKey();
            return PlayerAction.PICK_KEY;
        }

        if (tile == 'T') {
            if (hasEnoughKeys()) {
                return PlayerAction.NEXT_LEVEL;
            } else {
                return PlayerAction.NEED_KEY;
            }
        }

        if (tile == 'E') {
            return PlayerAction.WIN;
        }

        return PlayerAction.NONE;
    }

    public void resetToSpawn(int row, int col) {
        setPosition(row, col);
        this.direction = Direction.DOWN;
        this.moving = false;
        this.keyCount = 0;
    }

    public void resetForNewGame(int row, int col) {
        this.row = row;
        this.col = col;

        this.maxHp = 100;
        this.hp = 100;
        this.coins = 0;
        this.damage = 10;
        this.kills = 0;

        this.weaponName = "Wooden Sword";
        this.direction = Direction.DOWN;
        this.moving = false;
        this.keyCount = 0;
    }
}
