package com.example.snake.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SizeFood extends Food {
    private Timeline sizeUpTime;
    public SizeFood(double xPos, double yPos, Color color, Canvas canvas) {
        super(xPos, yPos, color, canvas);
    }

    /**
     * speeds up snake velocity for 5seconds
     * @param snake object to apply effect on
     */
    @Override
    public void applyEffect(Snake snake) {
        if (!snake.headScaled) {
            snake.scaleHead();
        }
        if (sizeUpTime == null) {
            setupSizeUpTime(snake);
        }
        sizeUpTime.play();
    }

    private void setupSizeUpTime(Snake snake) {
        sizeUpTime = new Timeline(new KeyFrame(Duration.seconds(5), event -> snake.resetHeadScale()));
    }
}
