package map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * MapTest — 地图预览入口，运行这个文件可以看到地图窗口
 * MapTest — Launch this file to preview the map in a JavaFX window.
 *
 * 运行方法 / How to run:
 *   确保你的项目已添加 JavaFX 库，然后直接运行 MapTest.java
 *   Make sure JavaFX is on your module path, then run MapTest.java
 *
 *   IntelliJ: 右键 MapTest.java → Run 'MapTest.main()'
 *   命令行 / CLI:
 *     javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.graphics map/*.java
 *     java  --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.graphics map.MapTest
 */
public class MapTest extends Application {

    @Override
    public void start(Stage stage) {

        // 1. 创建地图数据 / Create map data
        MazeMap mazeMap = new MazeMap();

        // 2. 创建 Canvas，尺寸 = 列数×48 × 行数×32 = 640×480
        //    Create Canvas sized COLS*48 × ROWS*32 = 640×480
        int width  = MazeMap.COLS * Tile.TILE_SIZE; // 20 * 48 = 640
        int height = MazeMap.ROWS * Tile.TILE_SIZE; // 15 * 48 = 480
        Canvas canvas = new Canvas(width, height);

        // 3. 创建渲染器并渲染一次 / Create renderer and render once
        MapRenderer renderer = new MapRenderer(mazeMap, canvas);
        renderer.render();
        mazeMap.loadLevel(1); // 切到第二层再渲染
        renderer.render();
        // 4. 构建场景并显示 / Build scene and show
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, width, height);

        stage.setTitle("MazeHunter — 地图预览 / Map Preview");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}