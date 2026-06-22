package weapon;

public abstract class Weapon {
	

	    protected String name;
	    protected int damage;
	    protected int price;

	    public Weapon(String name, int damage, int price) {
	        this.name = name;
	        this.damage = damage;
	        this.price = price;
	    }

	    public abstract int attack();

	    public String getName() {
	        return name;
	    }

	    public int getDamage() {
	        return damage;
	    }

	    public int getPrice() {
	        return price;
	    }
	}

