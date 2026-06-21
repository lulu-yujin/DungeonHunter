package ui;

import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class StartMenu extends VBox {

    public StartMenu() {

        setAlignment(Pos.CENTER);
        setSpacing(20);

        Label title = new Label("MAZE HUNTER");

        Button startBtn = new Button("Start Game");
        Button instructionBtn = new Button("Instructions");
        Button exitBtn = new Button("Exit");

        getChildren().addAll(
                title,
                startBtn,
                instructionBtn,
                exitBtn
        );
    }
}