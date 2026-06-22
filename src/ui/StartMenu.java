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

public class StartMenu extends VBox {

    private GameManager gameManager;

    public StartMenu(GameManager gameManager) {
        this.gameManager = gameManager;

        setAlignment(Pos.CENTER);
        setSpacing(22);
        setPadding(new Insets(60));

        setStyle(
                "-fx-background-color: linear-gradient(to bottom, #141E30, #243B55);"
        );

        Label title = new Label("DUNGEON HUNTER");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 56));
        title.setTextFill(Color.web("#F5E6A8"));
        title.setStyle(
                "-fx-effect: dropshadow(gaussian, black, 8, 0.6, 2, 2);"
        );

        Label subtitle = new Label("Collect keys. Defeat monsters. Escape the maze.");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#D6D6D6"));

        Button startBtn = createMenuButton("Start Game");
        Button instructionBtn = createMenuButton("Instructions");
        Button exitBtn = createMenuButton("Exit");

        startBtn.setOnAction(e -> gameManager.startGame());
        instructionBtn.setOnAction(e -> gameManager.showInstructions());
        exitBtn.setOnAction(e -> Platform.exit());

        getChildren().addAll(
                title,
                subtitle,
                startBtn,
                instructionBtn,
                exitBtn
        );
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);

        button.setPrefWidth(260);
        button.setPrefHeight(48);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        String normalStyle =
                "-fx-background-color: #3C8DBC;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #9ED8FF;" +
                "-fx-border-width: 1.5;" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #5DADE2;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, #9ED8FF, 12, 0.5, 0, 0);";

        button.setStyle(normalStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));

        return button;
    }
}