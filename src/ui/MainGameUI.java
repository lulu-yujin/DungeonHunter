package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainGameUI extends BorderPane {

    private GameHUD hud;
    private GridPane mapGrid;

    public MainGameUI() {

        createHUD();
        createMap();

        setTop(hud);
        setCenter(mapGrid);
    }

    private void createHUD() {
        hud = new GameHUD();
    }

    private void createMap() {

        mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);

        int rows = 15;
        int cols = 20;

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < cols; c++) {

                Rectangle tile =
                        new Rectangle(32, 32);

                if (r == 0 || c == 0 ||
                        r == rows - 1 ||
                        c == cols - 1) {

                    tile.setFill(Color.DARKGRAY);
                }
                else {
                    tile.setFill(Color.LIGHTGRAY);
                }

                tile.setStroke(Color.BLACK);

                mapGrid.add(tile, c, r);
            }
        }
    }

    public GameHUD getHud() {
        return hud;
    }
}