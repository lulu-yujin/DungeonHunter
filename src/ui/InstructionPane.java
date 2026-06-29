package ui;

import game.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Displays the instruction screen for controls and game goals.
 */
public class InstructionPane extends BorderPane {

    // ================= Constants =================

    private static final int WINDOW_PADDING = 50;

    private static final String BACKGROUND_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #1C1C1C, #2C3E50);";

    private static final String TEXT_AREA_STYLE =
            "-fx-control-inner-background: #111111;" +
            "-fx-text-fill: #EEEEEE;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #F5E6A8;" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 2;";

    private static final String BUTTON_STYLE =
            "-fx-background-color: #3C8DBC;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;";

    private static final String BUTTON_HOVER_STYLE =
            "-fx-background-color: #5DADE2;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;";

    // ================= Fields =================

    private final GameManager gameManager;

    // ================= Constructor =================

    public InstructionPane(GameManager gameManager) {

        this.gameManager = gameManager;

        setupUI();
    }

    // ================= UI Setup =================

    private void setupUI() {

        setPadding(new Insets(WINDOW_PADDING));
        setStyle(BACKGROUND_STYLE);

        Label titleLabel = createTitleLabel();

        TextArea instructionText = createInstructionTextArea();

        VBox bottomBox = createBottomBox();

        setTop(titleLabel);
        setCenter(instructionText);
        setBottom(bottomBox);
    }

    private Label createTitleLabel() {

        Label label = new Label("Instructions");

        label.setFont(Font.font("Georgia", FontWeight.BOLD, 44));
        label.setTextFill(Color.web("#F5E6A8"));

        BorderPane.setAlignment(label, Pos.CENTER);

        return label;
    }

    private TextArea createInstructionTextArea() {

        TextArea textArea = new TextArea(getInstructionText());

        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setFont(Font.font("Arial", 18));
        textArea.setStyle(TEXT_AREA_STYLE);

        return textArea;
    }

    private String getInstructionText() {

        return String.join(
                "\n",
                "Controls",
                "",
                "W / A / S / D     Move",
                "SPACE             Attack",
                "B                 Open shop",
                "ESC               Pause game",
                "",
                "Goal",
                "",
                "Collect 3 keys in each level.",
                "Enter the portal after collecting all keys.",
                "Buy stronger weapons in the shop.",
                "Defeat enemies and survive.",
                "Reach the final exit to win."
        );
    }

    private VBox createBottomBox() {

        Button backButton = createButton("Back to Menu");

        backButton.setOnAction(e -> gameManager.showStartMenu());

        VBox box = new VBox(backButton);

        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25, 0, 0, 0));

        return box;
    }

    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(220);
        button.setPrefHeight(45);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        button.setStyle(BUTTON_STYLE);

        button.setOnMouseEntered(e -> button.setStyle(BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));

        return button;
    }
}