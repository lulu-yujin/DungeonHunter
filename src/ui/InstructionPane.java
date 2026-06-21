package ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

public class InstructionPane extends BorderPane {

    public InstructionPane() {

        Label title =
                new Label("Instructions");

        TextArea text =
                new TextArea(
                """
                W A S D : Move

                J : Attack

                E : Open Shop

                ESC : Pause

                Kill monsters
                Earn coins
                Buy weapons
                Defeat Boss
                Escape Maze
                """
        );

        text.setEditable(false);

        Button backBtn =
                new Button("Back");

        setTop(title);
        setCenter(text);
        setBottom(backBtn);
    }
}