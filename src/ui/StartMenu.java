package ui;

import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class StartMenu extends VBox {
    
    // 提供公共方法方便外部调整
    private Label title;
    private Button startBtn;
    private Button instructionBtn;
    private Button exitBtn;
    
    public StartMenu() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setStyle("-fx-background-color: #2c3e50; -fx-padding: 50px;");
        
        // 创建标题
        title = new Label("Dungeon Hunter");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setTextFill(Color.web("#ecf0f1"));
        
        // 创建按钮
        startBtn = createStyledButton("Start Game");
        instructionBtn = createStyledButton("Instructions");
        exitBtn = createStyledButton("Exit");
        
        getChildren().addAll(title, startBtn, instructionBtn, exitBtn);
    }
    
    // 按钮样式工厂方法
    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-padding: 12px 40px;" +
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;"
        );
        // 悬停效果（通过事件监听）
        btn.setOnMouseEntered(e -> 
            btn.setStyle(btn.getStyle() + "-fx-background-color: #2980b9;")
        );
        btn.setOnMouseExited(e -> 
            btn.setStyle(btn.getStyle().replace("-fx-background-color: #2980b9;", "-fx-background-color: #3498db;"))
        );
        return btn;
    }
    
    // Getter方法供外部获取按钮实例
    public Button getStartBtn() { return startBtn; }
    public Button getInstructionBtn() { return instructionBtn; }
    public Button getExitBtn() { return exitBtn; }
    
    // 动态修改标题颜色的方法
    public void setTitleColor(String color) {
        title.setStyle("-fx-text-fill: " + color + ";");
    }
}