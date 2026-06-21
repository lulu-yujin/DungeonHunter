package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.GamePanel;
import ui.MenuUI;

public class Main extends Application {
	
	private static final int GAME_WIDTH = 1024;
    private static final int GAME_HEIGHT =768;
    
    private GamePanel gamePanel;
    private MenuUI menuUI;
    private StackPane root;
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // 创建根布局
        root = new StackPane();
        
        // 创建菜单
        menuUI = new MenuUI(
            this::startGame,      // onStart
            this::showInstructions, // onInstructions
            this::exitGame       // onExit
        );
        menuUI.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
        
        // 默认显示菜单
        root.getChildren().add(menuUI);
        
        // 创建场景
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        
        Player player = new Player(1, 1);
        CollisionManager collisionManager = new CollisionManager();
        GameManager gameManager = new GameManager(player, collisionManager);

        scene.setOnKeyPressed(event -> {
            gameManager.handleKeyPressed(event);
        });
        // 设置舞台
        primaryStage.setTitle("Dungeon Hunter");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            if (gamePanel != null) {
                gamePanel.stopGame();
            }
        });
        
        // 居中显示
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    // 开始游戏
    private void startGame() {
        // 移除菜单
        root.getChildren().remove(menuUI);
        
        // 创建游戏面板（如果已存在则重新创建）
        if (gamePanel != null) {
            gamePanel.stopGame();
        }
        gamePanel = new GamePanel();
        root.getChildren().add(gamePanel);
        
        // 启动游戏线程
        gamePanel.startGameThread();
        
        // 请求焦点以便接收键盘事件
        gamePanel.requestFocus();
    }
    
    // 显示说明（可以创建说明界面或弹窗）
    private void showInstructions() {
        System.out.println("Instructions: Use arrow keys to move, space to attack");
        // 这里可以创建一个简单的弹窗或切换到说明界面
        // 例如使用Alert:
        /*
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("How to Play");
        alert.setContentText("Use arrow keys to move\nPress space to attack");
        alert.showAndWait();
        */
    }
    
    // 退出游戏
    private void exitGame() {
        if (gamePanel != null) {
            gamePanel.stopGame();
        }
        primaryStage.close();
    }
    
    // 返回主菜单（如果游戏中有返回功能）
    public void returnToMenu() {
        if (gamePanel != null) {
            gamePanel.stopGame();
            root.getChildren().remove(gamePanel);
            gamePanel = null;
        }
        root.getChildren().add(menuUI);
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        if (gamePanel != null) {
            gamePanel.stopGame();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}