package ui;

import game.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Displays the pause menu during the game.
 */
public class PauseMenu extends VBox {

    // ================= Constants =================

    private static final int MENU_SPACING = 22;
    private static final int MENU_PADDING = 60;

    private static final int BUTTON_WIDTH = 240;
    private static final int BUTTON_HEIGHT = 48;

    private static final String BACKGROUND_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #000000, #2C3E50);";

    private static final String BUTTON_STYLE =
            "-fx-background-color: #34495E;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #95A5A6;" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1.5;" +
            "-fx-cursor: hand;";

    private static final String BUTTON_HOVER_STYLE =
            "-fx-background-color: #5D6D7E;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: white;" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;";

    // ================= Fields =================

    private final GameManager gameManager;

    // ================= Constructor =================

    public PauseMenu(GameManager gameManager) {

        this.gameManager = gameManager;

        setupUI();
    }

    // ================= UI Setup =================

    private void setupUI() {

        setSpacing(MENU_SPACING);
        setPadding(new Insets(MENU_PADDING));
        setAlignment(Pos.CENTER);
        setStyle(BACKGROUND_STYLE);

        Label titleLabel = createTitleLabel();

        Button resumeButton = createButton("Resume");
        Button restartButton = createButton("Restart");
        Button menuButton = createButton("Main Menu");

        resumeButton.setOnAction(e -> gameManager.resumeGame());
        restartButton.setOnAction(e -> gameManager.restartGame());
        menuButton.setOnAction(e -> gameManager.showStartMenu());

        getChildren().addAll(
                titleLabel,
                resumeButton,
                restartButton,
                menuButton
        );
    }

    private Label createTitleLabel() {

        Label label = new Label("PAUSED");

        label.setFont(Font.font("Georgia", FontWeight.BOLD, 52));
        label.setTextFill(Color.web("#F5E6A8"));

        return label;
    }

    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        button.setStyle(BUTTON_STYLE);

        button.setOnMouseEntered(e -> button.setStyle(BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));

        return button;
    }
}