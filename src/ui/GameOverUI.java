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

public class GameOverUI extends VBox {

    private GameManager gameManager;
    private boolean win;

    public GameOverUI(GameManager gameManager, boolean win) {

        this.gameManager = gameManager;
        this.win = win;

        setupUI();
    }

    private void setupUI() {

        setAlignment(Pos.CENTER);
        setPadding(new Insets(50));
        setSpacing(0);

        if (win) {
            setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #102B1F, #1E8449);"
            );
        } else {
            setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #1A0000, #641E16);"
            );
        }

        VBox mainCard = new VBox(24);
        mainCard.setAlignment(Pos.CENTER);
        mainCard.setPadding(new Insets(40));
        mainCard.setMaxWidth(620);

        mainCard.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.45);" +
                "-fx-background-radius: 24;" +
                "-fx-border-color: rgba(255, 255, 255, 0.25);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 24;" +
                "-fx-effect: dropshadow(gaussian, black, 18, 0.55, 0, 6);"
        );

        Label iconLabel = new Label(win ? "★" : "☠");
        iconLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 54));
        iconLabel.setTextFill(win ? Color.web("#F5D76E") : Color.web("#FF7675"));

        Label resultLabel = new Label(win ? "YOU WIN" : "GAME OVER");
        resultLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 58));
        resultLabel.setTextFill(Color.web("#F5E6A8"));
        resultLabel.setStyle(
                "-fx-effect: dropshadow(gaussian, black, 8, 0.7, 2, 2);"
        );

        Label subtitleLabel = new Label(
                win
                        ? "You escaped the dungeon successfully."
                        : "You were defeated in the dungeon."
        );
        subtitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        subtitleLabel.setTextFill(Color.web("#E8E8E8"));

        HBox statsBox = createStatsBox();

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

        mainCard.getChildren().addAll(
                iconLabel,
                resultLabel,
                subtitleLabel,
                statsBox,
                buttonBox
        );

        getChildren().add(mainCard);
    }

    private HBox createStatsBox() {

        HBox box = new HBox(14);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 6, 0));

        Player player = gameManager.getPlayer();

        if (player == null) {
            return box;
        }

        VBox killsCard = createStatCard(
                "Kills",
                String.valueOf(player.getKills()),
                "#FF7675"
        );

        VBox coinsCard = createStatCard(
                "Coins",
                String.valueOf(player.getCoins()),
                "#F5D76E"
        );

        VBox weaponCard = createStatCard(
                "Weapon",
                player.getWeaponName(),
                "#74B9FF"
        );

        VBox damageCard = createStatCard(
                "Damage",
                String.valueOf(player.getDamage()),
                "#55EFC4"
        );

        box.getChildren().addAll(
                killsCard,
                coinsCard,
                weaponCard,
                damageCard
        );

        return box;
    }

    private VBox createStatCard(String title, String value, String color) {

        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(125);
        card.setPrefHeight(86);

        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.10);" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: rgba(255, 255, 255, 0.22);" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1.5;"
        );

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

    private Button createMainButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(190);
        button.setPrefHeight(48);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        String normalStyle =
                "-fx-background-color: #F5D76E;" +
                "-fx-text-fill: #222222;" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #FFEAA7;" +
                "-fx-text-fill: #111111;" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, #F5D76E, 12, 0.45, 0, 0);";

        button.setStyle(normalStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));

        return button;
    }

    private Button createSecondButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(190);
        button.setPrefHeight(48);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        String normalStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.14);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: rgba(255, 255, 255, 0.35);" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1.5;" +
                "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.24);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #F5D76E;" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;";

        button.setStyle(normalStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));

        return button;
    }
}