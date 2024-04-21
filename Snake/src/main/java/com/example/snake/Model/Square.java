package com.example.snake.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {
    public static double squareHeight = 20;
    public static double squareWidth = 20;
    public Square(double xPos, double yPos) {
        super(xPos, yPos, squareWidth, squareHeight);
    }
}
