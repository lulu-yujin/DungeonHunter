package shop;
import java.util.Scanner;
import model.*;

public class main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        Player player = new Player("Hero");
        Shop shop = new Shop();

        while(true) {

            System.out.println("\n====== SHOP ======");
            System.out.println("Coins: " + player.getCoins());

            System.out.println("1. Iron Sword (100)");
            System.out.println("2. Fire Sword (250)");
            System.out.println("3. Legendary Sword (500)");
            System.out.println("0. Exit");

            System.out.print("Choose: ");

            int choice = input.nextInt();

            if(choice == 0) {
                break;
            }

            switch(choice) {

                case 1:

                    if(shop.buyWeapon(player,new IronSword()))
                        System.out.println("Iron Sword Purchased!");
                    else
                        System.out.println("Not enough coins!");

                    break;

                case 2:

                    if(shop.buyWeapon(player,new FireSword()))
                        System.out.println("Fire Sword Purchased!");
                    else
                        System.out.println("Not enough coins!");

                    break;

                case 3:

                    if(shop.buyWeapon(player,new LegendarySword()))
                        System.out.println("Legendary Sword Purchased!");
                    else
                        System.out.println("Not enough coins!");

                    break;

                default:
                    System.out.println("Invalid Choice");
            }

            System.out.println(
                    "Current Weapon: "
                    + player.getCurrentWeapon().getName());
        }

        input.close();
    }
}