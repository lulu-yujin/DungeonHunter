package weapon;

/**
 * Represents a weapon that the player can use.
 * Each weapon has a name, damage value and price.
 */
public class Weapon {

    // ================= Fields =================

    private final String name;
    private final int damage;
    private final int price;

    // ================= Constructor =================

    public Weapon(String name, int damage, int price) {

        this.name = name;
        this.damage = damage;
        this.price = price;
    }


    // ================= Getters =================

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getPrice() {
        return price;
    }

    // ================= Factory Methods =================

    public static Weapon woodenSword() {
        return new Weapon("Wooden Sword", 10, 0);
    }

    public static Weapon ironSword() {
        return new Weapon("Iron Sword", 25, 120);
    }

    public static Weapon diamondSword() {
        return new Weapon("Diamond Sword", 45, 180);
    }
}