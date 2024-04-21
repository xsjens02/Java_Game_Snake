package com.example.snake.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SpeedFood extends Food {
    private Timeline speedUpTime;
    public SpeedFood(double xPos, double yPos, Color color, Canvas canvas) {
        super(xPos, yPos, color, canvas);
    }

    /**
     * scales snake head x2 for 5seconds
     * @param snake object to apply effect on
     */
    @Override
    public void applyEffect(Snake snake) {
        if (snake.getVelocity() == 1) {
            snake.setVelocity(snake.getVelocity() + 1);
            if (speedUpTime == null) {
                setupSpeedUpTime(snake);
            }
            speedUpTime.play();
        }
    }

    private void setupSpeedUpTime(Snake snake) {
        speedUpTime = new Timeline(new KeyFrame(Duration.seconds(5), event -> snake.setVelocity(snake.getVelocity() - 1)));
    }
}
