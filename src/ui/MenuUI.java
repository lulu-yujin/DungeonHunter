package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class MenuUI extends StackPane {

    public MenuUI(Runnable onStart, Runnable onInstructions, Runnable onExit) {
        // 1. 设置背景贴图 (请将 menu_bg.png 放入 resources 目录)
        // Image bgImage = new Image(getClass().getResourceAsStream("/resources/menu_bg.png"));
        // BackgroundImage background = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        // this.setBackground(new Background(background));
        
        // 临时背景色占位
        this.setStyle("-fx-background-color: #FFFCF2;");

        // 2. 创建按钮 (可以用图片按钮，这里用样式化的普通按钮示范)
        Button btnStart = createStyledButton("Start Game");
        Button btnInstructions = createStyledButton("Instructions");
        Button btnExit = createStyledButton("Exit");

        // 3. 绑定事件
        btnStart.setOnAction(e -> onStart.run());
        btnInstructions.setOnAction(e -> onInstructions.run());
        btnExit.setOnAction(e -> onExit.run());

        // 4. 布局
        VBox menuBox = new VBox(20, btnStart, btnInstructions, btnExit);
        menuBox.setAlignment(Pos.CENTER);
        
        this.getChildren().add(menuBox);
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 20));
        // 这里可以通过 CSS 注入按钮贴图
        btn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-padding: 10 30; -fx-background-radius: 5;");
        return btn;
    }
}