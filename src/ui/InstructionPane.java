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

public class InstructionPane extends BorderPane {

    private GameManager gameManager;

    public InstructionPane(GameManager gameManager) {
        this.gameManager = gameManager;

        setPadding(new Insets(50));
        setStyle("-fx-background-color: linear-gradient(to bottom, #1C1C1C, #2C3E50);");

        Label title = new Label("Instructions");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 44));
        title.setTextFill(Color.web("#F5E6A8"));
        BorderPane.setAlignment(title, Pos.CENTER);
        setTop(title);

        TextArea text = new TextArea(
                """
                Controls

                W / A / S / D     Move
                SPACE             Attack
                B                 Open shop
                ESC               Pause game

                Goal

                Collect 3 keys in each level.
                Enter the portal after collecting all keys.
                Buy stronger weapons in the shop.
                Defeat enemies and survive.
                Reach the final exit to win.
                """
        );

        text.setEditable(false);
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 18));
        text.setStyle(
                "-fx-control-inner-background: #111111;" +
                "-fx-text-fill: #EEEEEE;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #F5E6A8;" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 2;"
        );

        setCenter(text);

        Button backBtn = createButton("Back to Menu");
        backBtn.setOnAction(e -> gameManager.showStartMenu());

        VBox bottomBox = new VBox(backBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(25, 0, 0, 0));

        setBottom(bottomBox);
    }

    private Button createButton(String text) {
        Button button = new Button(text);

        button.setPrefWidth(220);
        button.setPrefHeight(45);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        button.setStyle(
                "-fx-background-color: #3C8DBC;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );

        return button;
    }
}