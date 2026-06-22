package ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class GamePanel extends BorderPane {

    private final int TILE_SIZE = 48;
    private final int SCREEN_WIDTH = 960;
    private final int SCREEN_HEIGHT = 720;

    private Canvas canvas;
    private GraphicsContext gc;

    // 测试图片
    private Image playerImage;
    private Image slimeImage;
    private Image wallImage;

    // 测试坐标
    private int playerX = 100;
    private int playerY = 100;

    private AnimationTimer gameLoop;

    public GamePanel() {

        canvas = new Canvas(
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );

        gc = canvas.getGraphicsContext2D();

        getChildren().add(canvas);

        loadImages();

        setupKeyboard();

        startGameLoop();
    }

    private void loadImages() {

        playerImage =
                new Image(
                        getClass()
                        .getResourceAsStream(
                                "/player/player_down.png"
                        )
                );

        slimeImage =
                new Image(
                        getClass()
                        .getResourceAsStream(
                                "/monster/slime.png"
                        )
                );

        wallImage =
                new Image(
                        getClass()
                        .getResourceAsStream(
                                "/tiles/wall.png"
                        )
                );
    }

    public void startGameLoop() {

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                update();

                render();
            }
        };

        gameLoop.start();
    }

    private void update() {

    }

    private void render() {

        gc.clearRect(
                0,
                0,
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );

        drawMap();

        drawPlayer();

        drawMonsters();
    }

    private void drawMap() {

        for(int row=0;row<22;row++) {

            for(int col=0;col<32;col++) {

                gc.drawImage(
                        wallImage,
                        col * TILE_SIZE,
                        row * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );
            }
        }
    }

    private void drawPlayer() {

        gc.drawImage(
                playerImage,
                playerX,
                playerY,
                TILE_SIZE,
                TILE_SIZE
        );
    }

    private void drawMonsters() {

        gc.drawImage(
                slimeImage,
                300,
                200,
                TILE_SIZE,
                TILE_SIZE
        );

        gc.drawImage(
                slimeImage,
                500,
                300,
                TILE_SIZE,
                TILE_SIZE
        );
    }

    private void setupKeyboard() {

        setFocusTraversable(true);

        setOnKeyPressed(e -> {

            switch (e.getCode()) {

                case W -> playerY -= 5;

                case S -> playerY += 5;

                case A -> playerX -= 5;

                case D -> playerX += 5;
            }
        });
    }
}