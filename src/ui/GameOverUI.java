package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverUI extends VBox {

    private Label resultLabel;
    private Label statsLabel;

    private Button replayButton;
    private Button menuButton;

    public GameOverUI() {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        setStyle("""
                -fx-background-color:
                rgba(0,0,0,0.85);
                """);

        resultLabel =
                new Label("GAME OVER");

        statsLabel =
                new Label();

        replayButton =
                new Button("Play Again");

        menuButton =
                new Button("Main Menu");

        getChildren().addAll(
                resultLabel,
                statsLabel,
                replayButton,
                menuButton
        );
    }

    public void setWin() {
        resultLabel.setText("YOU WIN");
    }

    public void setLose() {
        resultLabel.setText("GAME OVER");
    }

    public void updateStats(
            int kills,
            int coins) {

        statsLabel.setText(
                "Kills: " + kills +
                "\nCoins: " + coins
        );
    }
}