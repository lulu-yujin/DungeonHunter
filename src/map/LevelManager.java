package map;

public class LevelManager {
    private int currentLevel = 1;
    private static final int MAX_LEVEL = 3;
    private MazeMap mazeMap;

    public void goNextLevel() {
        if (currentLevel < MAX_LEVEL) {
            currentLevel++;
            mazeMap.loadLevel(currentLevel); // 切换地图数据
        }
    }

    public boolean isFinalLevel() {
        return currentLevel == MAX_LEVEL;
    }
}