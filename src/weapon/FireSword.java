package weapon;

public class FireSword extends Weapon{
	

	    public FireSword() {
	        super("Fire Sword", 35, 250);
	    }

	    @Override
	    public int attack() {
	        return damage;
	    }
	}
