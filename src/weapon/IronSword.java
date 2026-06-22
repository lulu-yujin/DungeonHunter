package weapon;

public class IronSword extends Weapon {

	    public IronSword() {
	        super("Iron Sword", 20, 100);
	    }

	    @Override
	    public int attack() {
	        return damage;
	    }
}