package main;

import game.GameManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        // 创建游戏管理器
        GameManager gameManager = new GameManager(primaryStage);

        // 显示开始菜单
        gameManager.showStartMenu();

        // 设置窗口标题
        primaryStage.setTitle("Dungeon Hunters");

        // 禁止调整窗口大小（可选）
        primaryStage.setResizable(false);

        // 显示窗口
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}