package lab1;

import javafx.geometry.Pos;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * Snake
 * <p>
 * Initially one gold coin are randomly placed in the matrix. The turquoise
 * snake aims to collect the coin. Collecting the coin will make the snake
 * grow and the gold coin will appear in a new, randomly selected place. For
 * each coin the snake collects you get a point. The game is over when the
 * snake leaves the game board or eats its own body.
 */

public class SnakeModel extends GameModel {

    public enum Directions {
        EAST(1, 0),
        WEST(-1, 0),
        NORTH(0, -1),
        SOUTH(0, 1),
        NONE(0, 0);

        private final int xDelta;
        private final int yDelta;

        Directions(final int xDelta, final int yDelta) {
            this.xDelta = xDelta;
            this.yDelta = yDelta;
        }

        public int getXDelta() {
            return this.xDelta;
        }

        public int getYDelta() {
            return this.yDelta;
        }
    }

    /**
     * Graphical representation of a coin.
     */
    private static final GameTile COIN_TILE = new RoundTile(new Color(255, 215,
            0),
            new Color(255, 255, 0), 2.0);

    /**
     * Graphical representation of the head
     */
    private static final GameTile HEAD_TILE = new RectangularTile(Color.CYAN);

    /**
     * Graphical representation of the body
     */
    private static final GameTile BODY_TILE = new RectangularTile(Color.BLACK);

    /**
     * Graphical representation of a blank tile.
     */
    private static final GameTile BLANK_TILE = new GameTile();

    /**
     * The position of the head.
     */
    private Position headPos;

    /**
     * The direction of the head.
     */
    private Directions direction = Directions.NORTH;

    /**
     * The number of coins found.
     */
    private int score;

    /**
     * The snake's body.
     */
    private ArrayDeque<Position> body = new ArrayDeque();
    private Position bodyPos;

    /**
     * Create a new model for the snake game.
     */
    public SnakeModel() {
        Dimension size = getGameboardSize();

        // Blank out the whole gameboard
        for (int i = 0; i < size.width; i++) {
            for (int j = 0; j < size.height; j++) {
                setGameboardState(i, j, BLANK_TILE);
            }
        }
        // Insert the head in the middle of the gameboard.
        this.headPos = new Position(size.width / 2, size.height / 2);
        setGameboardState(this.headPos, HEAD_TILE);

        //Inserts one body tile.
        this.bodyPos = new Position(headPos.getX(), headPos.getY() + 1);
        body.add(bodyPos);
        setGameboardState(this.bodyPos, BODY_TILE);

        // Insert a coin into the gameboard.
        addCoin();
    }

    /**
     * Insert another coin into the gameboard.
     */
    private void addCoin() {
        Position newCoinPos;
        Dimension size = getGameboardSize();

        // Loop until a blank position is found and ...
        do {
            newCoinPos = new Position((int) (Math.random() * size.width),
                    (int) (Math.random() * size.height));
        } while (!isPositionEmpty(newCoinPos));

        // ... add a new coin to the empty tile.
        setGameboardState(newCoinPos, COIN_TILE);
        //this.coins.add(newCoinPos);   ???
    }

    /**
     * Update the direction of the collector
     * according to the user's keypress.
     */
    private void updateDirection(final int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (direction != Directions.EAST) {
                    this.direction = Directions.WEST;
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != Directions.SOUTH) {
                    this.direction = Directions.NORTH;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Directions.WEST) {
                    this.direction = Directions.EAST;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Directions.NORTH) {
                    this.direction = Directions.SOUTH;
                }
                break;
            default:
                // Don't change direction if another key is pressed
                break;
        }
    }

    /**
     * Get next position of the head.
     */
    private Position getNextHeadPos() {
        return new Position(
                this.headPos.getX() + this.direction.getXDelta(),
                this.headPos.getY() + this.direction.getYDelta());
    }

    /**
     * Return whether the specified position is empty.
     *
     * @param pos The position to test.
     * @return true if position is empty.
     */
    private boolean isPositionEmpty(final Position pos) {
        return (getGameboardState(pos) == BLANK_TILE);
    }

    /**
     * @param pos The position to test.
     * @return <code>false</code> if the position is outside the playing field, <code>true</code> otherwise.
     */
    private boolean isOutOfBounds(Position pos) {
        return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
                || pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
    }

    /**
     * @return <code>false</code> if the gameboard isn't full, <code>true</code> otherwise.
     */
    private boolean isFull() {

        for (int i = 0; i < this.getGameboardSize().height; i++) {
            for (int j = 0; j < this.getGameboardSize().width; j++) {
                if (getGameboardState(i, j) == BLANK_TILE) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method is called repeatedly so that the
     * game can update its state.
     *
     * @param lastKey The most recent keystroke.
     */
    @Override
    public void gameUpdate(final int lastKey) throws GameOverException {
        Position oldHeadPos = headPos;
        updateDirection(lastKey);

        // Erase the previous position of the head.
        setGameboardState(this.headPos, BLANK_TILE);

        //Erase the last body tile
        setGameboardState(body.peekLast(), BLANK_TILE);

        //Update the body positions
        body.addFirst(headPos);


        // Change head position
        this.headPos = getNextHeadPos();

        if (isOutOfBounds(this.headPos)) {
            throw new GameOverException(this.score);
        }

        if (getGameboardState(headPos) == BODY_TILE) {
            throw new GameOverException(this.score);
        }

        // Remove the coin at the new collector position (if any) and add one
        if (getGameboardState(headPos) == COIN_TILE) {
            body.addFirst(oldHeadPos);
            if (!isFull()) {
                addCoin();
                this.score++;
            } else {
                throw new GameOverException(this.score);
            }
        } else {
            body.removeLast();
        }

        // Draw head at new position.
        setGameboardState(this.headPos, HEAD_TILE);

        //Draw the body at the new Position.
        for (Position p : body) {
            setGameboardState(p, BODY_TILE);
        }
    }

}

