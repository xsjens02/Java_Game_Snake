package com.example.snake.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.List;
import java.util.Random;

public class Food {
    private final double initX;
    private final double initY;
    private Square square;
    private Color color;
    private final GraphicsContext gc;
    private Timeline visibility;

    /**
     * constructs food object
     * @param initPosX initial x coordinate of food object
     * @param initPosY initial y coordinate of food object
     * @param color color of food object
     * @param canvas location to draw food object on
     */
    public Food(double initPosX, double initPosY, Color color, Canvas canvas) {
        this.initX = initPosX;
        this.initY = initPosY;
        this.gc = canvas.getGraphicsContext2D();
        this.color = color;
        initFood();
    }

    public Square getSquare() {
        return this.square;
    }

    public Color getColor() {
        return this.color;
    }

    public void setSquare(Square square) {
        this.square = square;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * method for moving food object on canvas according to other objects present
     * @param snake object present on board
     * @param foodList of food objects present on board
     */
    public void moveFood(Snake snake, List<Food> foodList){
        if (visibility != null) {
            visibility.stop();
        }
        clearFood();
        Random ranNum = new Random();

        double newValueX, newValueY;

        do {
            newValueX = ranNum.nextInt(Board.columns) * Square.squareWidth;
            newValueY = ranNum.nextInt(Board.rows) * Square.squareHeight;
        } while (isSpaceOccupied(newValueX,newValueY,snake,foodList));

        square.setX(newValueX);
        square.setY(newValueY);

        drawFood();
        if (visibility == null) {
            setupVisibility();
        }
        visibility.play();
    }

    /**
     * method to check if square on board is occupied by other object
     * @param xPos x coordinates of square object in question
     * @param yPos y coordinates of square object in question
     * @param snake object to check if occupying coordinates for square
     * @param foodList of food objects to check if occupying coordinates for square
     * @return true if space is occupied by other objects
     */
    private boolean isSpaceOccupied(double xPos, double yPos, Snake snake, List<Food> foodList) {
        for (Square bodyPart : snake.getBody()) {
            if (bodyPart.getX() == xPos && bodyPart.getY() == yPos) {
                return true;
            }
        }
        for (Food food : foodList) {
            if (food.getSquare().getX() == xPos && food.getSquare().getY() == yPos) {
                return true;
            }
        }
        return false;
    }

    /**
     * initializes timeline to hide food object on canvas after 5seconds visible
     */
    private void setupVisibility() {
        visibility = new Timeline(new KeyFrame(Duration.seconds(5), event -> clearFood()));
    }

    /**
     * initializes/reset food object to initial values
     */
    public void initFood() {
        if (square == null) {
            square = new Square(initX, initY);
        } else {
            square.setX(initX);
            square.setY(initY);
        }
        if (visibility != null) {
            visibility.stop();
        }
        drawFood();
    }

    /**
     * method to apply a special effect on snake object
     * @param snake object to apply effect on
     */
    public void applyEffect(Snake snake){}

    public void drawFood() {
        double x = square.getX();
        double y = square.getY();
        double w = square.getWidth();
        double h = square.getHeight();

        gc.setFill(color);
        gc.fillRect(x,y,w,h);
    }

    public void clearFood() {
        double x = square.getX();
        double y = square.getY();
        double w = square.getWidth();
        double h = square.getHeight();

        gc.clearRect(x,y,w,h);
    }
}
