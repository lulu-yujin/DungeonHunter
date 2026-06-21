package ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GamePanel extends Pane {
    
    final int tileSize = 48; 
    final int screenCol = 20;
    final int screenRow = 15;
    final int screenWidth = tileSize * screenCol; 
    final int screenHeight = tileSize * screenRow; 
    
    private Canvas canvas;
    private GraphicsContext gc;
    
    //UI部分
    private GameHUD hud;
    private PauseMenu pauseMenu;
    private GameOverUI gameOverUI;
    
    private AnimationTimer gameLoop;
    private boolean running = false;
    

    
    public GamePanel() {
        // 创建画布
        canvas = new Canvas(screenWidth, screenHeight);
        gc = canvas.getGraphicsContext2D();
        
        // 添加画布到面板
        getChildren().add(canvas);
        
        // 设置面板大小
        setPrefSize(screenWidth, screenHeight);
        
        // 设置背景色（JavaFX方式）
        setStyle("-fx-background-color: white;");
        
        // 确保可以获得焦点（用于键盘事件）
        setFocusTraversable(true);
        
        createHUD();
        createPauseMenu();
        createGameOverUI();
    }
    
    //创建HUD
    private void createHUD() {

        hud = new GameHUD();

        hud.setLayoutX(10);
        hud.setLayoutY(10);

        getChildren().add(hud);
    }
    
    //创建暂停菜单
    private void createPauseMenu() {

        pauseMenu = new PauseMenu();

        pauseMenu.setPrefSize(
                screenWidth,
                screenHeight
        );

        pauseMenu.setVisible(false);

        getChildren().add(pauseMenu);
    }
    
    //创建结束界面
    private void createGameOverUI() {

        gameOverUI = new GameOverUI();

        gameOverUI.setPrefSize(
                screenWidth,
                screenHeight
        );

        gameOverUI.setVisible(false);

        getChildren().add(gameOverUI);
    }
    
    public void showPauseMenu() {

        pauseMenu.setVisible(true);
    }
    
    public void hidePauseMenu() {

        pauseMenu.setVisible(false);
    }
    
    public void showWinScreen() {

        gameOverUI.setWin();

        gameOverUI.updateStats(
                20,
                500
        );

        gameOverUI.setVisible(true);
    }
    
    public void showGameOverScreen() {

        gameOverUI.setLose();

        gameOverUI.updateStats(
                12,
                300
        );

        gameOverUI.setVisible(true);
    }
    
    public void startGameThread() {
        if (gameLoop != null) {
            return;
        }
        
        running = true;
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            private final long targetTime = 1000000000L / 60; // 60 FPS
            
            @Override
            public void handle(long now) {
                if (!running) {
                    stop();
                    return;
                }
                
                if (now - lastUpdate >= targetTime) {
                    update();    // 更新游戏逻辑
                    repaint();   // 重绘画面
                    lastUpdate = now;
                }
            }
        };
        
        gameLoop.start();
    }
    
    public void stopGame() {
        running = false;
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
    }
    
    public void update() {
        
    }
    
    public void repaint() {
        // 清空画布（相当于Swing的super.paintComponent）
        gc.clearRect(0, 0, screenWidth, screenHeight);
        
        // 绘制白色背景
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, screenWidth, screenHeight);
        
        // 这里相当于Swing的paintComponent方法
        paintComponent(gc);
    }
    
    private void paintComponent(GraphicsContext gc) {

        gc.setFill(Color.BLACK);

        
        gc.fillRect(100, 100, tileSize, tileSize);

    }
    
    // 获取屏幕尺寸的方法（供Main类使用）
    public int getScreenWidth() {
        return screenWidth;
    }
    
    public int getScreenHeight() {
        return screenHeight;
    }
}
