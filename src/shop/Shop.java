package shop;

import java.io.InputStream;

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

/**
 * Displays the shop screen and handles item purchasing.
 */
public class Shop extends VBox {

    // ================= Constants =================

    private static final int WINDOW_CONTENT_WIDTH = 680;

    private static final int CARD_HEIGHT = 92;
    private static final int ICON_BOX_SIZE = 68;
    private static final int ICON_IMAGE_SIZE = 52;

    private static final int BUY_BUTTON_WIDTH = 90;
    private static final int BUY_BUTTON_HEIGHT = 38;

    private static final int BACK_BUTTON_WIDTH = 240;
    private static final int BACK_BUTTON_HEIGHT = 44;

    private static final int POTION_PRICE = 30;
    private static final int POTION_HEAL_AMOUNT = 30;

    private static final String IRON_SWORD_IMAGE = "/shop/iron_sword.png";
    private static final String DIAMOND_SWORD_IMAGE = "/shop/diamond_sword.png";
    private static final String POTION_IMAGE = "/shop/potion.png";

    private static final String BACKGROUND_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #1b1b2f, #16213e);";

    private static final String STATUS_BOX_STYLE =
            "-fx-background-color: rgba(0, 0, 0, 0.35);" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #F5D76E;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 16;";

    private static final String CARD_STYLE =
            "-fx-background-color: rgba(255, 255, 255, 0.10);" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.25);" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1.5;";

    private static final String CARD_HOVER_STYLE =
            "-fx-background-color: rgba(255, 255, 255, 0.18);" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #F5D76E;" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 2;";

    private static final String ICON_BOX_STYLE =
            "-fx-background-color: rgba(0, 0, 0, 0.35);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255, 255, 255, 0.25);" +
            "-fx-border-radius: 14;";

    private static final String BUY_BUTTON_STYLE =
            "-fx-background-color: #F5D76E;" +
            "-fx-text-fill: #222222;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;";

    private static final String BUY_BUTTON_HOVER_STYLE =
            "-fx-background-color: #FFEAA7;" +
            "-fx-text-fill: #111111;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, #F5D76E, 10, 0.4, 0, 0);";

    private static final String BACK_BUTTON_STYLE =
            "-fx-background-color: #3C8DBC;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;";

    private static final String BACK_BUTTON_HOVER_STYLE =
            "-fx-background-color: #5DADE2;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, #9ED8FF, 12, 0.5, 0, 0);";

    // ================= Fields =================

    private final GameManager gameManager;
    private final Player player;

    private Label coinsLabel;
    private Label hpLabel;
    private Label weaponLabel;
    private Label messageLabel;

    // ================= Constructor =================

    public Shop(GameManager gameManager) {

        this.gameManager = gameManager;
        this.player = gameManager.getPlayer();

        setupUI();

        updateLabels();
    }

    // ================= UI Setup =================

    private void setupUI() {

        setSpacing(18);
        setPadding(new Insets(35));
        setAlignment(Pos.CENTER);
        setStyle(BACKGROUND_STYLE);

        Label titleLabel = createTitleLabel();

        VBox statusBox = createStatusBox();

        messageLabel = createMessageLabel();

        VBox itemBox = createItemBox();

        Button backButton = createBackButton();

        backButton.setOnAction(e -> gameManager.resumeGame());

        getChildren().addAll(
                titleLabel,
                statusBox,
                messageLabel,
                itemBox,
                backButton
        );
    }

    private Label createTitleLabel() {

        Label label = new Label("SHOP");

        label.setFont(Font.font("Georgia", FontWeight.BOLD, 46));
        label.setTextFill(Color.web("#F5D76E"));
        label.setStyle(
                "-fx-effect: dropshadow(gaussian, black, 8, 0.6, 2, 2);"
        );

        return label;
    }

    private Label createMessageLabel() {

        Label label = new Label("Choose an item to buy.");

        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);

        return label;
    }

    private VBox createStatusBox() {

        VBox box = new VBox(6);

        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12));
        box.setMaxWidth(WINDOW_CONTENT_WIDTH);
        box.setStyle(STATUS_BOX_STYLE);

        coinsLabel = createStatusLabel(Color.GOLD);
        hpLabel = createStatusLabel(Color.LIGHTGREEN);
        weaponLabel = createStatusLabel(Color.LIGHTBLUE);

        box.getChildren().addAll(
                coinsLabel,
                hpLabel,
                weaponLabel
        );

        return box;
    }

    private Label createStatusLabel(Color color) {

        Label label = new Label();

        label.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        label.setTextFill(color);

        return label;
    }

    private VBox createItemBox() {

        VBox itemBox = new VBox(14);

        itemBox.setAlignment(Pos.CENTER);
        itemBox.setMaxWidth(WINDOW_CONTENT_WIDTH);

        HBox ironSwordCard = createProductCard(
                IRON_SWORD_IMAGE,
                "Iron Sword",
                "Stronger weapon for normal enemies.",
                "Damage: 25",
                "Price: 120 coins",
                () -> buyWeapon(Weapon.ironSword())
        );

        HBox diamondSwordCard = createProductCard(
                DIAMOND_SWORD_IMAGE,
                "Diamond Sword",
                "Powerful weapon for the final level.",
                "Damage: 45",
                "Price: 180 coins",
                () -> buyWeapon(Weapon.diamondSword())
        );

        HBox potionCard = createProductCard(
                POTION_IMAGE,
                "Health Potion",
                "Restore part of your HP.",
                "Heal: " + POTION_HEAL_AMOUNT + " HP",
                "Price: " + POTION_PRICE + " coins",
                () -> buyPotion(POTION_PRICE, POTION_HEAL_AMOUNT)
        );

        itemBox.getChildren().addAll(
                ironSwordCard,
                diamondSwordCard,
                potionCard
        );

        return itemBox;
    }

    // ================= Product Card =================

    private HBox createProductCard(
            String imagePath,
            String name,
            String description,
            String statText,
            String priceText,
            Runnable action
    ) {

        HBox card = new HBox(18);

        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(12));
        card.setMaxWidth(WINDOW_CONTENT_WIDTH);
        card.setPrefHeight(CARD_HEIGHT);
        card.setStyle(CARD_STYLE);

        StackPane iconBox = createIconBox(imagePath, name);

        VBox textBox = createProductTextBox(
                name,
                description,
                statText,
                priceText
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button buyButton = createBuyButton("Buy");

        buyButton.setOnAction(e -> action.run());

        card.getChildren().addAll(
                iconBox,
                textBox,
                spacer,
                buyButton
        );

        card.setOnMouseEntered(e -> card.setStyle(CARD_HOVER_STYLE));
        card.setOnMouseExited(e -> card.setStyle(CARD_STYLE));

        return card;
    }

    private VBox createProductTextBox(
            String name,
            String description,
            String statText,
            String priceText
    ) {

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

        return textBox;
    }

    private StackPane createIconBox(String imagePath, String name) {

        StackPane iconBox = new StackPane();

        iconBox.setPrefWidth(ICON_BOX_SIZE);
        iconBox.setPrefHeight(ICON_BOX_SIZE);
        iconBox.setMinWidth(ICON_BOX_SIZE);
        iconBox.setMinHeight(ICON_BOX_SIZE);
        iconBox.setStyle(ICON_BOX_STYLE);

        Image image = loadImage(imagePath);

        if (image != null) {

            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(ICON_IMAGE_SIZE);
            imageView.setFitHeight(ICON_IMAGE_SIZE);
            imageView.setPreserveRatio(true);

            iconBox.getChildren().add(imageView);

        } else {

            Label fallbackLabel = new Label("?");

            fallbackLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            fallbackLabel.setTextFill(Color.WHITE);

            iconBox.getChildren().add(fallbackLabel);

            System.err.println("[Shop] Missing image for " + name + ": " + imagePath);
        }

        return iconBox;
    }

    private Image loadImage(String path) {

        InputStream stream = getClass().getResourceAsStream(path);

        if (stream == null) {
            return null;
        }

        return new Image(stream);
    }

    // ================= Buttons =================

    private Button createBuyButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(BUY_BUTTON_WIDTH);
        button.setPrefHeight(BUY_BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        setButtonHoverStyle(
                button,
                BUY_BUTTON_STYLE,
                BUY_BUTTON_HOVER_STYLE
        );

        return button;
    }

    private Button createBackButton() {

        Button button = new Button("Back to Game");

        button.setPrefWidth(BACK_BUTTON_WIDTH);
        button.setPrefHeight(BACK_BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        setButtonHoverStyle(
                button,
                BACK_BUTTON_STYLE,
                BACK_BUTTON_HOVER_STYLE
        );

        return button;
    }

    private void setButtonHoverStyle(
            Button button,
            String normalStyle,
            String hoverStyle
    ) {

        button.setStyle(normalStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    // ================= Purchase Logic =================

    private void buyWeapon(Weapon weapon) {

        boolean success = player.buyWeapon(
                weapon.getName(),
                weapon.getPrice(),
                weapon.getDamage()
        );

        if (success) {

            showSuccessMessage("Bought " + weapon.getName() + "!");

        } else {

            showErrorMessage("Not enough coins.");
        }

        updateLabels();
    }

    private void buyPotion(int price, int healAmount) {

        boolean success = player.buyHealthPotion(price, healAmount);

        if (success) {

            showSuccessMessage("Bought health potion. HP restored.");

        } else {

            showErrorMessage("Not enough coins.");
        }

        updateLabels();
    }

    private void showSuccessMessage(String message) {

        messageLabel.setText(message);
        messageLabel.setTextFill(Color.LIGHTGREEN);
    }

    private void showErrorMessage(String message) {

        messageLabel.setText(message);
        messageLabel.setTextFill(Color.web("#FF7675"));
    }

    // ================= Status Update =================

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