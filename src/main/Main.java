package main;

import game.GameManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // 创建游戏管理器
        GameManager gameManager = new GameManager(stage);

        // 显示开始菜单
        gameManager.showStartMenu();

        // 设置窗口标题
        stage.setTitle("Dungeon Hunters");
        stage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}