package map;

public class TestPath {
    public static void main(String[] args) {
        String workDir = System.getProperty("user.dir");
        System.out.println("当前工作目录: " + workDir);
        
        // 测试相对路径
        java.io.File testFile = new java.io.File("res/map/floor.png");
        System.out.println("相对路径: res/map/floor.png");
        System.out.println("绝对路径: " + testFile.getAbsolutePath());
        System.out.println("文件存在? " + testFile.exists());
    }
}