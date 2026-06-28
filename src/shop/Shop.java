package shop;

import game.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        setPadding(new Insets(35));
        setAlignment(Pos.CENTER);

        setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1b1b2f, #16213e);"
        );

        Label titleLabel = new Label("SHOP");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 46));
        titleLabel.setTextFill(Color.web("#F5D76E"));
        titleLabel.setStyle(
                "-fx-effect: dropshadow(gaussian, black, 8, 0.6, 2, 2);"
        );

        VBox statusBox = createStatusBox();

        messageLabel = new Label("Choose an item to buy.");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageLabel.setTextFill(Color.WHITE);

        VBox itemBox = new VBox(14);
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setMaxWidth(680);

        HBox ironSwordCard = createProductCard(
                "/shop/iron_sword.png",
                "Iron Sword",
                "Stronger weapon for normal enemies.",
                "Damage: 25",
                "Price: 120 coins",
                "Buy",
                () -> buyWeapon(Weapon.ironSword())
        );

        HBox diamondSwordCard = createProductCard(
                "/shop/diamond_sword.png",
                "Diamond Sword",
                "Powerful weapon for the final level.",
                "Damage: 45",
                "Price: 180 coins",
                "Buy",
                () -> buyWeapon(Weapon.diamondSword())
        );

        HBox potionCard = createProductCard(
                "/shop/potion.png",
                "Health Potion",
                "Restore part of your HP.",
                "Heal: 30 HP",
                "Price: 50 coins",
                "Buy",
                () -> buyPotion(30, 30)
        );

        itemBox.getChildren().addAll(
                ironSwordCard,
                diamondSwordCard,
                potionCard
        );

        Button backButton = createBackButton();

        backButton.setOnAction(e ->
                gameManager.resumeGame()
        );

        getChildren().addAll(
                titleLabel,
                statusBox,
                messageLabel,
                itemBox,
                backButton
        );
    }

    private VBox createStatusBox() {

        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12));
        box.setMaxWidth(680);

        box.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.35);" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #F5D76E;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 16;"
        );

        coinsLabel = new Label();
        hpLabel = new Label();
        weaponLabel = new Label();

        coinsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        hpLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        weaponLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        coinsLabel.setTextFill(Color.GOLD);
        hpLabel.setTextFill(Color.LIGHTGREEN);
        weaponLabel.setTextFill(Color.LIGHTBLUE);

        box.getChildren().addAll(
                coinsLabel,
                hpLabel,
                weaponLabel
        );

        return box;
    }

    private HBox createProductCard(
            String imagePath,
            String name,
            String description,
            String statText,
            String priceText,
            String buttonText,
            Runnable action
    ) {

        HBox card = new HBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(12));
        card.setMaxWidth(680);
        card.setPrefHeight(92);

        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.10);" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: rgba(255, 255, 255, 0.25);" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1.5;"
        );

        StackPane iconBox = createIconBox(imagePath, name);

        VBox textBox = new VBox(4);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 19));
        nameLabel.setTextFill(Color.WHITE);

        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font("Arial", 13));
        descriptionLabel.setTextFill(Color.web("#D6D6D6"));

        Label statLabel = new Label(statText + "     " + priceText);
        statLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statLabel.setTextFill(Color.web("#F5D76E"));

        textBox.getChildren().addAll(
                nameLabel,
                descriptionLabel,
                statLabel
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button buyButton = createBuyButton(buttonText);

        buyButton.setOnAction(e -> action.run());

        card.getChildren().addAll(
                iconBox,
                textBox,
                spacer,
                buyButton
        );

        card.setOnMouseEntered(e ->
                card.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.18);" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #F5D76E;" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-width: 2;"
                )
        );

        card.setOnMouseExited(e ->
                card.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.10);" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.25);" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-width: 1.5;"
                )
        );

        return card;
    }

    private StackPane createIconBox(String imagePath, String name) {

        StackPane iconBox = new StackPane();

        iconBox.setPrefWidth(68);
        iconBox.setPrefHeight(68);
        iconBox.setMinWidth(68);
        iconBox.setMinHeight(68);

        iconBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.35);" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: rgba(255, 255, 255, 0.25);" +
                "-fx-border-radius: 14;"
        );

        Image image = loadImage(imagePath);

        if (image != null) {

            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(52);
            imageView.setFitHeight(52);
            imageView.setPreserveRatio(true);

            iconBox.getChildren().add(imageView);

        } else {

            Label fallback = new Label("?");

            fallback.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            fallback.setTextFill(Color.WHITE);

            iconBox.getChildren().add(fallback);

            System.err.println("[Shop] Missing image for " + name + ": " + imagePath);
        }

        return iconBox;
    }

    private Image loadImage(String path) {

        var stream = getClass().getResourceAsStream(path);

        if (stream == null) {
            return null;
        }

        return new Image(stream);
    }

    private Button createBuyButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(90);
        button.setPrefHeight(38);

        button.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        String normalStyle =
                "-fx-background-color: #F5D76E;" +
                "-fx-text-fill: #222222;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #FFEAA7;" +
                "-fx-text-fill: #111111;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, #F5D76E, 10, 0.4, 0, 0);";

        button.setStyle(normalStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));

        return button;
    }

    private Button createBackButton() {

        Button button = new Button("Back to Game");

        button.setPrefWidth(240);
        button.setPrefHeight(44);

        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        String normalStyle =
                "-fx-background-color: #3C8DBC;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #5DADE2;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, #9ED8FF, 12, 0.5, 0, 0);";

        button.setStyle(normalStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));

        return button;
    }

    private void buyWeapon(Weapon weapon) {

        if (player.buyWeapon(
                weapon.getName(),
                weapon.getPrice(),
                weapon.getDamage()
        )) {

            messageLabel.setText("Bought " + weapon.getName() + "!");
            messageLabel.setTextFill(Color.LIGHTGREEN);

        } else {

            messageLabel.setText("Not enough coins.");
            messageLabel.setTextFill(Color.web("#FF7675"));
        }

        updateLabels();
    }

    private void buyPotion(int price, int healAmount) {

        if (player.buyHealthPotion(price, healAmount)) {

            messageLabel.setText("Bought health potion. HP restored.");
            messageLabel.setTextFill(Color.LIGHTGREEN);

        } else {

            messageLabel.setText("Not enough coins.");
            messageLabel.setTextFill(Color.web("#FF7675"));
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