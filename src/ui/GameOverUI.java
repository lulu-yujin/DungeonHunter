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
import player.Player;

public class GameOverUI extends VBox {

    private GameManager gameManager;

    public GameOverUI(GameManager gameManager, boolean win) {
        this.gameManager = gameManager;

        setSpacing(22);
        setPadding(new Insets(60));
        setAlignment(Pos.CENTER);

        if (win) {
            setStyle("-fx-background-color: linear-gradient(to bottom, #145A32, #1E8449);");
        } else {
            setStyle("-fx-background-color: linear-gradient(to bottom, #2C0000, #641E16);");
        }

        Label resultLabel = new Label(win ? "YOU WIN" : "GAME OVER");
        resultLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 56));
        resultLabel.setTextFill(Color.web("#F5E6A8"));

        Label statsLabel = new Label(getStatsText());
        statsLabel.setFont(Font.font("Arial", 18));
        statsLabel.setTextFill(Color.WHITE);
        statsLabel.setAlignment(Pos.CENTER);

        Button replayButton = createButton("Play Again");
        Button menuButton = createButton("Main Menu");

        replayButton.setOnAction(e -> gameManager.restartGame());
        menuButton.setOnAction(e -> gameManager.showStartMenu());

        getChildren().addAll(
                resultLabel,
                statsLabel,
                replayButton,
                menuButton
        );
    }

    private String getStatsText() {
        Player player = gameManager.getPlayer();

        if (player == null) {
            return "";
        }

        return "Kills: " + player.getKills()
                + "\nCoins: " + player.getCoins()
                + "\nWeapon: " + player.getWeaponName()
                + "\nDamage: " + player.getDamage();
    }

    private Button createButton(String text) {
        Button button = new Button(text);

        button.setPrefWidth(240);
        button.setPrefHeight(48);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        button.setStyle(
                "-fx-background-color: #F5E6A8;" +
                "-fx-text-fill: #222222;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;"
        );

        return button;
    }
}