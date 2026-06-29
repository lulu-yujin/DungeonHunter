package ui;

import game.GameManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Displays the main menu of the game.
 */
public class StartMenu extends VBox {

    // ================= Constants =================

    private static final int MENU_SPACING = 22;
    private static final int MENU_PADDING = 60;

    private static final int BUTTON_WIDTH = 260;
    private static final int BUTTON_HEIGHT = 48;

    private static final String BACKGROUND_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #141E30, #243B55);";

    private static final String TITLE_EFFECT_STYLE =
            "-fx-effect: dropshadow(gaussian, black, 8, 0.6, 2, 2);";

    private static final String BUTTON_STYLE =
            "-fx-background-color: #3C8DBC;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #9ED8FF;" +
            "-fx-border-width: 1.5;" +
            "-fx-cursor: hand;";

    private static final String BUTTON_HOVER_STYLE =
            "-fx-background-color: #5DADE2;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, #9ED8FF, 12, 0.5, 0, 0);";

    // ================= Fields =================

    private final GameManager gameManager;

    // ================= Constructor =================

    public StartMenu(GameManager gameManager) {

        this.gameManager = gameManager;

        setupUI();
    }

    // ================= UI Setup =================

    private void setupUI() {

        setAlignment(Pos.CENTER);
        setSpacing(MENU_SPACING);
        setPadding(new Insets(MENU_PADDING));
        setStyle(BACKGROUND_STYLE);

        Label titleLabel = createTitleLabel();

        Label subtitleLabel = createSubtitleLabel();

        Button startButton = createMenuButton("Start Game");
        Button instructionButton = createMenuButton("Instructions");
        Button exitButton = createMenuButton("Exit");

        startButton.setOnAction(e -> gameManager.startGame());
        instructionButton.setOnAction(e -> gameManager.showInstructions());
        exitButton.setOnAction(e -> Platform.exit());

        getChildren().addAll(
                titleLabel,
                subtitleLabel,
                startButton,
                instructionButton,
                exitButton
        );
    }

    private Label createTitleLabel() {

        Label label = new Label("DUNGEON HUNTER");

        label.setFont(Font.font("Georgia", FontWeight.BOLD, 56));
        label.setTextFill(Color.web("#F5E6A8"));
        label.setStyle(TITLE_EFFECT_STYLE);

        return label;
    }

    private Label createSubtitleLabel() {

        Label label = new Label("Collect keys. Defeat monsters. Escape the maze.");

        label.setFont(Font.font("Arial", 18));
        label.setTextFill(Color.web("#D6D6D6"));

        return label;
    }

    private Button createMenuButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        button.setStyle(BUTTON_STYLE);

        button.setOnMouseEntered(e -> button.setStyle(BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));

        return button;
    }
}