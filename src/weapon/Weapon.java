package weapon;

public class Weapon {

    private String name;
    private int damage;
    private int price;

    public Weapon(String name, int damage, int price) {
        this.name = name;
        this.damage = damage;
        this.price = price;
    }

    public int attack() {
        return damage;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getPrice() {
        return price;
    }

    public static Weapon woodenSword() {
        return new Weapon("Wooden Sword", 10, 0);
    }

    public static Weapon ironSword() {
        return new Weapon("Iron Sword", 25, 120);
    }

    public static Weapon diamondSword() {
        return new Weapon("Diamond Sword", 45, 300);
    }
}