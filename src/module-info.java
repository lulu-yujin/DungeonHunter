module Game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // 添加这一行，允许 javafx.graphics 通过反射访问 map 包
    opens map to javafx.graphics;

    // 如果用了 FXML，还需要开放给 javafx.fxml
    // opens map to javafx.graphics, javafx.fxml;
}