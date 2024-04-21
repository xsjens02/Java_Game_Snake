module com.example.snake {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.snake to javafx.fxml;
    exports com.example.snake;
    exports com.example.snake.Controller;
    opens com.example.snake.Controller to javafx.fxml;
    exports com.example.snake.Model;
    opens com.example.snake.Model to javafx.fxml;
}