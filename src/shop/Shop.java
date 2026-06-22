package shop;

import game.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import player.Player;
import weapon.Weapon;

public class Shop extends VBox {

    private GameManager gameManager;
    private Player player;

    private Label coinsLabel;
    private Label hpLabel;
    private Label weaponLabel;
    private Label messageLabel;

    public Shop(GameManager gameManager) {
        this.gameManager = gameManager;
        this.player = gameManager.getPlayer();

        setupUI();
        updateLabels();
    }

    private void setupUI() {
        setSpacing(18);
        setPadding(new Insets(40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #222222;");

        Label titleLabel = new Label("SHOP");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.WHITE);

        coinsLabel = new Label();
        coinsLabel.setFont(new Font("Arial", 18));
        coinsLabel.setTextFill(Color.GOLD);

        hpLabel = new Label();
        hpLabel.setFont(new Font("Arial", 18));
        hpLabel.setTextFill(Color.LIGHTGREEN);

        weaponLabel = new Label();
        weaponLabel.setFont(new Font("Arial", 18));
        weaponLabel.setTextFill(Color.LIGHTBLUE);

        messageLabel = new Label("Choose an item to buy.");
        messageLabel.setFont(new Font("Arial", 16));
        messageLabel.setTextFill(Color.WHITE);

        Button woodenSwordButton = createButton("Wooden Sword - 0 coins | Damage 10");
        Button ironSwordButton = createButton("Iron Sword - 120 coins | Damage 25");
        Button diamondSwordButton = createButton("Diamond Sword - 300 coins | Damage 45");
        Button potionButton = createButton("Health Potion - 50 coins | Heal 30");
        Button backButton = createButton("Back to Game");

        woodenSwordButton.setOnAction(e ->
                buyWeapon(Weapon.woodenSword())
        );

        ironSwordButton.setOnAction(e ->
                buyWeapon(Weapon.ironSword())
        );

        diamondSwordButton.setOnAction(e ->
                buyWeapon(Weapon.diamondSword())
        );

        potionButton.setOnAction(e ->
                buyPotion(50, 30)
        );

        backButton.setOnAction(e ->
                gameManager.resumeGame()
        );

        getChildren().addAll(
                titleLabel,
                coinsLabel,
                hpLabel,
                weaponLabel,
                messageLabel,
                woodenSwordButton,
                ironSwordButton,
                diamondSwordButton,
                potionButton,
                backButton
        );
    }

    private Button createButton(String text) {
        Button button = new Button(text);

        button.setPrefWidth(420);
        button.setPrefHeight(45);
        button.setFont(new Font("Arial", 16));

        return button;
    }

    private void buyWeapon(Weapon weapon) {
        if (player.buyWeapon(
                weapon.getName(),
                weapon.getPrice(),
                weapon.getDamage()
        )) {
            messageLabel.setText("Bought " + weapon.getName() + "!");
        } else {
            messageLabel.setText("Not enough coins.");
        }

        updateLabels();
    }

    private void buyPotion(int price, int healAmount) {
        if (player.buyHealthPotion(price, healAmount)) {
            messageLabel.setText("Bought health potion. HP restored.");
        } else {
            messageLabel.setText("Not enough coins.");
        }

        updateLabels();
    }

    private void updateLabels() {
        coinsLabel.setText("Coins: " + player.getCoins());

        hpLabel.setText(
                "HP: "
                        + player.getHp()
                        + "/"
                        + player.getMaxHp()
        );

        weaponLabel.setText(
                "Weapon: "
                        + player.getWeaponName()
                        + " | Damage: "
                        + player.getDamage()
        );
    }
}