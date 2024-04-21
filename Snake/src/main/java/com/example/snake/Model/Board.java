package com.example.snake.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static int columns = 30;
    public static int rows = 30;
    public static int boardWidth = (int) (Square.squareWidth * columns);
    public static int boardHeight = (int) (Square.squareHeight * rows);
    private Canvas canvas;
    private Snake snake;
    private List<Food> foodList;
    private final IntegerProperty score;
    private Timeline gameLoop;
    private final BooleanProperty gameLoopActive;
    private Timeline insaneLoop;
    private boolean insaneMode;
    private Rotate rotateTransform;
    private final GraphicsContext gc;

    /**
     * constructs a board
     */
    public Board() {
        canvas = new Canvas(boardWidth, boardHeight);
        gc = canvas.getGraphicsContext2D();
        this.score = new SimpleIntegerProperty();
        this.gameLoopActive = new SimpleBooleanProperty();
        initGame();
    }
    public Canvas getCanvas() {
        return this.canvas;
    }

    public Snake getSnake() {
        return this.snake;
    }
    public List<Food> getFoodList() {
        return this.foodList;
    }
    public IntegerProperty scoreProperty() {
        return this.score;
    }
    public BooleanProperty gameProperty() {
        return gameLoopActive;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
    public void setSnake(Snake snake) {
        this.snake = snake;
    }
    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    /**
     * method to start game
     */
    public void startGame() {
        if (!gameLoopActive.get()) {
            gameLoopActive.set(true);
            startGameLoop();
        }
    }

    /**
     * starts game loop that arranges snake, food and handles game logic
     */
    private void startGameLoop() {
        if (gameLoop == null) {
            gameLoop = new Timeline();
            gameLoop.getKeyFrames().add(new KeyFrame(Duration.millis(100), event -> {
                snake.moveSnake();
                if (score.get() != 0 && score.get() % 5 == 0) {
                    startInsaneMode();
                }
                if (collideFood()) {
                    snake.addBody();
                    score.set(score.get() + 1);
                }
                if (collideEdge() || collideBody()) {
                    if (insaneMode) {
                        endInsaneMode();
                    }
                    gameLoop.stop();
                    initGame();
                }
            }));
            gameLoop.setCycleCount(Timeline.INDEFINITE);
        }
        gameLoop.play();
    }

    /**
     * method to check if snake head collides with any of board edges
     * @return true if collision with any of edges
     */
    private boolean collideEdge() {
        return snake.getHead().getY() < 0 || snake.getHead().getY() >= boardHeight || snake.getHead().getX() < 0 || snake.getHead().getX() >= boardWidth;
    }

    /**
     * method to check if snake head collides with own body
     * @return true if collision with body
     */
    private boolean collideBody() {
        for (int i = 1; i < snake.getBody().size(); i++) {
            Square bodyPart = snake.getBody().get(i);
            if (snake.getHead().getX() == bodyPart.getX() && snake.getHead().getY() == bodyPart.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * method to check if snake collides with any food objects and from that take action
     * @return true if snake collides with any food objects
     */
    private boolean collideFood() {
        for (Square bodyPart : snake.getBody()) {
            for (Food food : foodList) {
                if (snake.headScaled && bodyPart == snake.getHead()) {
                    if (snake.getDirection() == Direction.UP || snake.getDirection() == Direction.DOWN) {
                        if ((bodyPart.getX() + 20) == food.getSquare().getX() && bodyPart.getY() == food.getSquare().getY()) {
                            handleFoodCollision(food);
                            return true;
                        }
                    } else if (snake.getDirection() == Direction.LEFT || snake.getDirection() == Direction.RIGHT) {
                        if (bodyPart.getX() == food.getSquare().getX() && (bodyPart.getY() - 20) == food.getSquare().getY()) {
                            handleFoodCollision(food);
                            return true;
                        }
                    }
                }
                if (bodyPart.getX() == food.getSquare().getX() && bodyPart.getY() == food.getSquare().getY()) {
                    handleFoodCollision(food);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * method to apply effect from food on snake
     * @param food to apply effect from
     */
    private void handleFoodCollision(Food food) {
        food.applyEffect(snake);
        food.moveFood(snake, foodList);
    }

    /**
     * starts insane loop that rotates board 90degrees for 5seconds
     */
    private void startInsaneMode() {
        if (!insaneMode) {
            if (rotateTransform == null) {
                rotateTransform = new Rotate(90, boardWidth / 2.0, boardHeight / 2.0);
            }
            canvas.getTransforms().add(rotateTransform);

            if (insaneLoop == null) {
                insaneLoop = new Timeline();
                insaneLoop.getKeyFrames().add(new KeyFrame(Duration.seconds(3), event -> {
                    canvas.getTransforms().remove(rotateTransform);
                    score.set(score.get() + 1);
                    insaneMode = false;
                }));
            }
            insaneLoop.play();
            insaneMode = true;
        }
    }

    /**
     * method to stop insane loop
     */
    private void endInsaneMode() {
        if (insaneMode) {
            canvas.getTransforms().remove(rotateTransform);
            insaneLoop.stop();
            insaneMode = false;
        }
    }

    /**
     * method to initialize snake
     */
    private void initializeSnake() {
        if (snake == null) {
            this.snake = new Snake(100,400, 3, Color.GREEN, Direction.UP, canvas);
        } else {
            snake.initSnake();
        }
    }

    /**
     * method to initialize food objects
     */
    private void initializeFood() {
        if (foodList == null) {
            this.foodList = new ArrayList<>();

            Food food = new Food(400,200, Color.RED, canvas);
            SpeedFood speedFood = new SpeedFood(300,200,Color.BLUE, canvas);
            SizeFood sizeFood = new SizeFood(200,500,Color.CYAN, canvas);

            foodList.add(food);
            foodList.add(speedFood);
            foodList.add(sizeFood);
        } else {
            for (Food food : foodList) {
                food.initFood();
            }
        }
    }

    /**
     * method to initialize all game objects
     */
    private void initGame() {
        gc.clearRect(0,0, boardWidth, boardHeight);
        initializeSnake();
        initializeFood();
        score.set(0);
        gameLoopActive.set(false);
    }
}
