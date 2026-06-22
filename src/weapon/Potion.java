package weapon;

public class Potion {
    private String name;
    private int healAmount;
    private int price;

    public Potion(String name, int healAmount, int price) {
        this.name = name;
        this.healAmount = healAmount;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public int getPrice() {
        return price;
    }
}
