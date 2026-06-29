package ui;

import game.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import player.Player;

/**
 * Displays the final result screen after the player wins or loses.
 */
public class GameOverUI extends VBox {

    // ================= Constants =================

    private static final int CARD_WIDTH = 620;

    private static final int BUTTON_WIDTH = 190;
    private static final int BUTTON_HEIGHT = 48;

    private static final int STAT_CARD_WIDTH = 125;
    private static final int STAT_CARD_HEIGHT = 86;

    private static final String WIN_BACKGROUND_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #102B1F, #1E8449);";

    private static final String LOSE_BACKGROUND_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #1A0000, #641E16);";

    private static final String MAIN_CARD_STYLE =
            "-fx-background-color: rgba(0, 0, 0, 0.45);" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: rgba(255, 255, 255, 0.25);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 24;" +
            "-fx-effect: dropshadow(gaussian, black, 18, 0.55, 0, 6);";

    private static final String STAT_CARD_STYLE =
            "-fx-background-color: rgba(255, 255, 255, 0.10);" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.22);" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1.5;";

    private static final String MAIN_BUTTON_STYLE =
            "-fx-background-color: #F5D76E;" +
            "-fx-text-fill: #222222;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;";

    private static final String MAIN_BUTTON_HOVER_STYLE =
            "-fx-background-color: #FFEAA7;" +
            "-fx-text-fill: #111111;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, #F5D76E, 12, 0.45, 0, 0);";

    private static final String SECOND_BUTTON_STYLE =
            "-fx-background-color: rgba(255, 255, 255, 0.14);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255, 255, 255, 0.35);" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1.5;" +
            "-fx-cursor: hand;";

    private static final String SECOND_BUTTON_HOVER_STYLE =
            "-fx-background-color: rgba(255, 255, 255, 0.24);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #F5D76E;" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;";

    // ================= Fields =================

    private final GameManager gameManager;
    private final boolean win;

    // ================= Constructor =================

    public GameOverUI(GameManager gameManager, boolean win) {

        this.gameManager = gameManager;
        this.win = win;

        setupUI();
    }

    // ================= UI Setup =================

    private void setupUI() {

        setAlignment(Pos.CENTER);
        setPadding(new Insets(50));
        setSpacing(0);
        setStyle(win ? WIN_BACKGROUND_STYLE : LOSE_BACKGROUND_STYLE);

        VBox mainCard = createMainCard();

        Label iconLabel = createIconLabel();

        Label resultLabel = createResultLabel();

        Label subtitleLabel = createSubtitleLabel();

        HBox statsBox = createStatsBox();

        HBox buttonBox = createButtonBox();

        mainCard.getChildren().addAll(
                iconLabel,
                resultLabel,
                subtitleLabel,
                statsBox,
                buttonBox
        );

        getChildren().add(mainCard);
    }

    private VBox createMainCard() {

        VBox card = new VBox(24);

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(CARD_WIDTH);
        card.setStyle(MAIN_CARD_STYLE);

        return card;
    }

    private Label createIconLabel() {

        Label label = new Label(win ? "★" : "☠");

        label.setFont(Font.font("Georgia", FontWeight.BOLD, 54));
        label.setTextFill(win ? Color.web("#F5D76E") : Color.web("#FF7675"));

        return label;
    }

    private Label createResultLabel() {

        Label label = new Label(win ? "YOU WIN" : "GAME OVER");

        label.setFont(Font.font("Georgia", FontWeight.BOLD, 58));
        label.setTextFill(Color.web("#F5E6A8"));
        label.setStyle(
                "-fx-effect: dropshadow(gaussian, black, 8, 0.7, 2, 2);"
        );

        return label;
    }

    private Label createSubtitleLabel() {

        String text = win
                ? "You escaped the dungeon successfully."
                : "You were defeated in the dungeon.";

        Label label = new Label(text);

        label.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        label.setTextFill(Color.web("#E8E8E8"));

        return label;
    }

    // ================= Stats =================

    private HBox createStatsBox() {

        HBox box = new HBox(14);

        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 6, 0));

        Player player = gameManager.getPlayer();

        if (player == null) {
            return box;
        }

        box.getChildren().addAll(
                createStatCard("Kills", String.valueOf(player.getKills()), "#FF7675"),
                createStatCard("Coins", String.valueOf(player.getCoins()), "#F5D76E"),
                createStatCard("Weapon", player.getWeaponName(), "#74B9FF"),
                createStatCard("Damage", String.valueOf(player.getDamage()), "#55EFC4")
        );

        return box;
    }

    private VBox createStatCard(String title, String value, String color) {

        VBox card = new VBox(6);

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(STAT_CARD_WIDTH);
        card.setPrefHeight(STAT_CARD_HEIGHT);
        card.setStyle(STAT_CARD_STYLE);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.web("#D6D6D6"));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        valueLabel.setTextFill(Color.web(color));
        valueLabel.setWrapText(true);
        valueLabel.setMaxWidth(110);
        valueLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                titleLabel,
                valueLabel
        );

        return card;
    }

    // ================= Buttons =================

    private HBox createButtonBox() {

        HBox buttonBox = new HBox(18);

        buttonBox.setAlignment(Pos.CENTER);

        Button replayButton = createMainButton("Play Again");
        Button menuButton = createSecondButton("Main Menu");

        replayButton.setOnAction(e -> gameManager.restartGame());
        menuButton.setOnAction(e -> gameManager.showStartMenu());

        buttonBox.getChildren().addAll(
                replayButton,
                menuButton
        );

        return buttonBox;
    }

    private Button createMainButton(String text) {

        Button button = createBaseButton(text);

        setButtonHoverStyle(
                button,
                MAIN_BUTTON_STYLE,
                MAIN_BUTTON_HOVER_STYLE
        );

        return button;
    }

    private Button createSecondButton(String text) {

        Button button = createBaseButton(text);

        setButtonHoverStyle(
                button,
                SECOND_BUTTON_STYLE,
                SECOND_BUTTON_HOVER_STYLE
        );

        return button;
    }

    private Button createBaseButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));

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
}