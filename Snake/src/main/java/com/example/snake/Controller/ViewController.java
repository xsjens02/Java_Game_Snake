package com.example.snake.Controller;

import com.example.snake.Model.Board;
import com.example.snake.Model.Direction;
import com.example.snake.Model.Snake;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    @FXML
    private Pane pane;
    @FXML
    private Label lblScore, lblBodyCount;
    @FXML
    private Label lblStart;
    private Board board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board = new Board();
        pane.getChildren().add(board.getCanvas());

        pane.setStyle("-fx-background-color: black;");

        pane.sceneProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null && newValue.getOnKeyPressed() == null) {
                newValue.setOnKeyPressed(event -> handleKeyPress(event.getCode(), board.getSnake()));
            }
        }));

        lblScore.textProperty().bind(board.scoreProperty().asString("Score: %d"));
        lblBodyCount.textProperty().bind(board.getSnake().bodySizeProperty().asString("Body count: %d"));
        lblStart.visibleProperty().bind(board.gameProperty().not());
    }

    private void handleKeyPress(KeyCode code, Snake snake) {
        if (code == KeyCode.ENTER) {
            board.startGame();
        } else if (code == KeyCode.UP && snake.getDirection() != Direction.DOWN) {
            snake.setDirection(Direction.UP);
        } else if (code == KeyCode.LEFT && snake.getDirection() != Direction.RIGHT) {
            snake.setDirection(Direction.LEFT);
        } else if (code == KeyCode.RIGHT && snake.getDirection() != Direction.LEFT) {
            snake.setDirection(Direction.RIGHT);
        } else if (code == KeyCode.DOWN && snake.getDirection() != Direction.UP) {
            snake.setDirection(Direction.DOWN);
        }
    }
}
