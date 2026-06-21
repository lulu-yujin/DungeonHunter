package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PauseMenu extends VBox {

    private Button resumeButton;
    private Button restartButton;
    private Button menuButton;

    public PauseMenu() {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        setStyle("""
                -fx-background-color:
                rgba(0,0,0,0.75);
                """);

        Label title = new Label("PAUSED");

        resumeButton =
                new Button("Resume");

        restartButton =
                new Button("Restart");

        menuButton =
                new Button("Main Menu");

        getChildren().addAll(
                title,
                resumeButton,
                restartButton,
                menuButton
        );
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMenuButton() {
        return menuButton;
    }
}