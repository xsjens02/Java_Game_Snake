package com.example.snake.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;

public class Snake {
    private final double initX;
    private final double initY;
    private final int initBodySize;
    private final Direction initDirection;
    private Square head;
    public boolean headScaled;
    private ArrayList<Square> body;
    private final IntegerProperty bodySize;
    private Color color;
    private Direction direction;
    private int velocity;
    private Timeline bodyLoop;
    private boolean bodyLoopActive;
    private final GraphicsContext gc;

    /**
     * constructs a snake object
     * @param initPosX initial x coordinate of head
     * @param initPosY initial y coordinate of head
     * @param initBodySize initial size of body vertical below from head
     * @param color color of snake body
     * @param direction initial direction of snake when game starts
     * @param canvas location to draw snake on
     */
    public Snake(double initPosX, double initPosY, int initBodySize, Color color, Direction direction, Canvas canvas) {
        this.initX = initPosX;
        this.initY = initPosY;
        this.initBodySize = initBodySize;
        this.initDirection = direction;
        this.gc = canvas.getGraphicsContext2D();
        this.body = new ArrayList<>();
        this.bodySize = new SimpleIntegerProperty();
        this.color = color;
        initSnake();
    }

    public Square getHead() {
        return this.head;
    }
    public ArrayList<Square> getBody() {
        return this.body;
    }
    public IntegerProperty bodySizeProperty() {
        return this.bodySize;
    }
    public Color getColor() {
        return this.color;
    }
    public Direction getDirection() {
        return this.direction;
    }
    public int getVelocity() {
        return this.velocity;
    }
    public Timeline getBodyLoop() {
        return this.bodyLoop;
    }

    public void setHead(Square head) {
        this.head = head;
    }
    public void setBody(ArrayList<Square> body) {
        this.body = body;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    /**
     * method for moving snake on canvas according to direction
     */
    public void moveSnake() {
        clearSnake();
        if (!bodyLoopActive) {
            startBodyLoop();
        }
        for (int i = 0; i < velocity; i++) {
            rearrangeBody();
        }
        drawSnake();
    }

    /**
     * arranges every square of snake to follow tail
     */
    public void rearrangeBody(){
        for (int i = body.size() - 1; i > 0; i--) {
            Square currentPart = body.get(i);
            Square nextPart = body.get(i - 1);

            currentPart.setX(nextPart.getX());
            currentPart.setY(nextPart.getY());
        }
        switch (direction) {
            case UP -> head.setY(head.getY() - Square.squareHeight);
            case LEFT -> head.setX(head.getX() - Square.squareWidth);
            case RIGHT -> head.setX(head.getX() + Square.squareWidth);
            case DOWN -> head.setY(head.getY() + Square.squareHeight);
        }
    }

    /**
     * adds a square at the end of snake
     */
    public void addBody(){
        Square last = body.get(body.size() - 1);
        Square secondLast = body.get(body.size() - 2);
        if (last.getX() < secondLast.getX()) {
            body.add(new Square(last.getX() - Square.squareWidth, last.getY()));
        } else if (last.getX() > secondLast.getX()) {
            body.add(new Square(last.getX() + Square.squareWidth, last.getY()));
        } else if (last.getY() < secondLast.getY()) {
            body.add(new Square(last.getX() - Square.squareHeight, last.getY()));
        } else if (last.getY() > secondLast.getY()) {
            body.add(new Square(last.getX() + Square.squareHeight, last.getY()));
        }
        bodySize.set(bodySize.get() + 1);
    }

    /**
     * starts a timeline that adds one square to snakes body every 5second
     */
    private void startBodyLoop() {
        if (bodyLoop == null) {
            bodyLoop = new Timeline(new KeyFrame(Duration.seconds(5), event -> this.addBody()));
            bodyLoop.setCycleCount(Timeline.INDEFINITE);
        }
        bodyLoop.play();
        bodyLoopActive = true;
    }

    /**
     * stops the timeline that adds one square to snakes body every 5second
     */
    private void endBodyLoop() {
        bodyLoop.stop();
        bodyLoopActive = false;
    }

    /**
     * scales the snakes head x2
     */
    public void scaleHead() {
        head.setWidth(head.getWidth() * 2);
        head.setHeight(head.getHeight() * 2);
        headScaled = true;
    }

    /**
     * reset scaling of snakes head
     */
    public void resetHeadScale() {
        clearSnake();
        if (headScaled) {
            head.setWidth(Square.squareWidth);
            head.setHeight(Square.squareHeight);
            headScaled = false;
        }
        drawSnake();
    }

    /**
     * initializes/resets snake to initial values
     */
    public void initSnake() {
        if (!body.isEmpty()) {
            body.clear();
        }
        if (bodyLoopActive) {
            endBodyLoop();
        }
        this.bodyLoopActive = false;
        Square initSquare = new Square(initX,initY);
        body.add(initSquare);
        this.head = body.get(0);
        this.headScaled = false;
        for (int i = 1; i <= initBodySize; i++) {
            Square bodyPart = new Square(initX, initY + (Square.squareHeight * i));
            body.add(bodyPart);
        }
        bodySize.set(body.size());
        this.direction = initDirection;
        this.velocity = 1;
        drawSnake();
    }

    /**
     * draws snake on canvas according to snakes coordinates
     */
    public void drawSnake() {
        for (Square bodyPart : body) {
            double x = bodyPart.getX();
            double y = bodyPart.getY();
            double w = bodyPart.getWidth();
            double h = bodyPart.getHeight();

            gc.setFill(this.color);
            gc.fillRect(x, y, w, h);
        }
    }

    /**
     * clears snake on canvas
     */
    public void clearSnake() {
        for (Square bodyPart : body) {
            double x = bodyPart.getX();
            double y = bodyPart.getY();
            double w = bodyPart.getWidth();
            double h = bodyPart.getHeight();

            gc.clearRect(x, y, w, h);
        }
    }
}
