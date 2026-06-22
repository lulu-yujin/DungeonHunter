package weapon;

public class WoodenSword extends Weapon{
	

	    public WoodenSword() {
	        super("Wooden Sword", 10, 0);
	    }

	    @Override
	    public int attack() {
	        return damage;
	    }
	}
