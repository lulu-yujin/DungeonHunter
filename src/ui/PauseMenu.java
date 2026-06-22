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

public class PauseMenu extends VBox {

    private GameManager gameManager;

    public PauseMenu(GameManager gameManager) {
        this.gameManager = gameManager;

        setSpacing(22);
        setPadding(new Insets(60));
        setAlignment(Pos.CENTER);

        setStyle("-fx-background-color: linear-gradient(to bottom, #000000, #2C3E50);");

        Label title = new Label("PAUSED");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 52));
        title.setTextFill(Color.web("#F5E6A8"));

        Button resumeButton = createButton("Resume");
        Button restartButton = createButton("Restart");
        Button menuButton = createButton("Main Menu");

        resumeButton.setOnAction(e -> gameManager.resumeGame());
        restartButton.setOnAction(e -> gameManager.restartGame());
        menuButton.setOnAction(e -> gameManager.showStartMenu());

        getChildren().addAll(
                title,
                resumeButton,
                restartButton,
                menuButton
        );
    }

    private Button createButton(String text) {
        Button button = new Button(text);

        button.setPrefWidth(240);
        button.setPrefHeight(48);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        String normalStyle =
                "-fx-background-color: #34495E;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #95A5A6;" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1.5;" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #5D6D7E;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: white;" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;";

        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));

        return button;
    }
}