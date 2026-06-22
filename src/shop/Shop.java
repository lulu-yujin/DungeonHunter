package shop;

public class Shop {
	 public boolean buyWeapon(Player player,
             Weapon weapon) {

if(player.getCoins() >= weapon.getPrice()) {

player.spendCoins(weapon.getPrice());

player.setCurrentWeapon(weapon);

return true;
}

return false;
}

public boolean buyPotion(Player player,
             Potion potion) {

if(player.getCoins() >= potion.getPrice()) {

player.spendCoins(potion.getPrice());

player.heal(potion.getHealAmount());

return true;
}

return false;
}
}
