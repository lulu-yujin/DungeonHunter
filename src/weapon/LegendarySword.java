
	package weapon;

	public class LegendarySword extends Weapon {

	    public LegendarySword() {
	        super("Legendary Sword", 60, 500);
	    }

	    @Override
	    public int attack() {
	        return damage;
	    }

}
