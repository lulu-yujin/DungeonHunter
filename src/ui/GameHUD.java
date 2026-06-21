package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class GameHUD extends HBox {

    private Label hpLabel;
    private Label coinLabel;
    private Label weaponLabel;
    private Label killLabel;

    public GameHUD() {

        setSpacing(30);
        setPadding(new Insets(10));

        hpLabel = new Label("HP: 100");
        coinLabel = new Label("Coins: 0");
        weaponLabel = new Label("Weapon: Wooden Sword");
        killLabel = new Label("Kills: 0");

        getChildren().addAll(
                hpLabel,
                coinLabel,
                weaponLabel,
                killLabel
        );
    }

    public void updateHP(int hp) {
        hpLabel.setText("HP: " + hp);
    }

    public void updateCoins(int coins) {
        coinLabel.setText("Coins: " + coins);
    }

    public void updateWeapon(String weapon) {
        weaponLabel.setText("Weapon: " + weapon);
    }

    public void updateKills(int kills) {
        killLabel.setText("Kills: " + kills);
    }
}