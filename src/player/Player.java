package player;

/**
 * Stores player data and handles player-related behaviours,
 * including movement, health, attack, coins, weapons and keys.
 */
public class Player {

    // ================= Constants =================

    private static final int DEFAULT_ROW = 1;
    private static final int DEFAULT_COL = 1;

    private static final int DEFAULT_MAX_HP = 100;
    private static final int DEFAULT_DAMAGE = 10;
    private static final String DEFAULT_WEAPON = "Wooden Sword";

    private static final int REQUIRED_KEYS = 3;

    // ================= Direction Enum =================

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

    // ================= Position =================

    private int row;
    private int col;

    // ================= Player Stats =================

    private int maxHp;
    private int hp;
    private int coins;
    private int damage;
    private int kills;
    private int keyCount;

    // ================= Equipment =================

    private String weaponName;

    // ================= State =================

    private Direction direction;

    private boolean attacking;
    private boolean moving;

    private int walkFrameIndex;

    // ================= Constructor =================

    public Player() {
        this(DEFAULT_ROW, DEFAULT_COL);
    }

    public Player(int row, int col) {

        this.row = row;
        this.col = col;

        resetStats();

        this.direction = Direction.DOWN;
        this.attacking = false;
        this.moving = false;
        this.walkFrameIndex = 0;
    }

    // ================= Position and Movement =================

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

        nextWalkFrame();
    }

    public void face(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void stopMoving() {
        this.moving = false;
    }

    // ================= Animation State =================

    public int getWalkFrameIndex() {
        return walkFrameIndex;
    }

    public void nextWalkFrame() {
        walkFrameIndex++;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void startAttack() {
        attacking = true;
    }

    public void stopAttack() {
        attacking = false;
    }

    // ================= Attack Logic =================

    /**
     * Returns the row of the tile directly in front of the player.
     */
    public int getAttackRow() {
        return row + direction.getDRow();
    }

    /**
     * Returns the column of the tile directly in front of the player.
     */
    public int getAttackCol() {
        return col + direction.getDCol();
    }

    /**
     * The player can only hit an enemy standing in the tile directly in front.
     */
    public boolean isEnemyInAttackRange(int enemyRow, int enemyCol) {

        return enemyRow == getAttackRow()
                && enemyCol == getAttackCol();
    }

    // ================= Health =================

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int damage) {

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }
    }

    public void heal(int amount) {

        hp += amount;

        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    // ================= Coins =================

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {

        if (amount > 0) {
            coins += amount;
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

    // ================= Weapon =================

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

    // ================= Kills =================

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    // ================= Keys =================

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

    // ================= Reset Methods =================

    /**
     * Resets only the position and temporary state when entering a new level.
     */
    public void resetToSpawn(int row, int col) {

        setPosition(row, col);

        direction = Direction.DOWN;
        attacking = false;
        moving = false;
        walkFrameIndex = 0;

        keyCount = 0;
    }

    /**
     * Resets all player data when starting a new game.
     */
    public void resetForNewGame(int row, int col) {

        this.row = row;
        this.col = col;

        resetStats();

        direction = Direction.DOWN;
        attacking = false;
        moving = false;
        walkFrameIndex = 0;
    }

    private void resetStats() {

        maxHp = DEFAULT_MAX_HP;
        hp = DEFAULT_MAX_HP;
        coins = 0;
        damage = DEFAULT_DAMAGE;
        kills = 0;
        keyCount = 0;

        weaponName = DEFAULT_WEAPON;
    }
}