package lab1;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by agnesmardh on 2016-11-08.
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

    /** Graphical representation of a coin. */
    private static final GameTile COIN_TILE = new RoundTile(new Color(255, 215,
            0),
            new Color(255, 255, 0), 2.0);

    /** Graphical representation of the head */
    private static final GameTile HEAD_TILE = new RectangularTile(Color.CYAN);

    /** Graphical representation of a blank tile. */
    private static final GameTile BLANK_TILE = new GameTile();

    /** The position of the head. */
    private Position headPos;

    /** The direction of the collector. */
    private Directions direction = Directions.NORTH;

    /** The number of coins found. */
    private int score;

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
                this.direction = Directions.WEST;
                break;
            case KeyEvent.VK_UP:
                this.direction = Directions.NORTH;
                break;
            case KeyEvent.VK_RIGHT:
                this.direction = Directions.EAST;
                break;
            case KeyEvent.VK_DOWN:
                this.direction = Directions.SOUTH;
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
     * @param pos
     *            The position to test.
     * @return true if position is empty.
     */
    private boolean isPositionEmpty(final Position pos) {
        return (getGameboardState(pos) == BLANK_TILE);
    }

    /**
     *
     * @param pos The position to test.
     * @return <code>false</code> if the position is outside the playing field, <code>true</code> otherwise.
     */
    private boolean isOutOfBounds(Position pos) {
        return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
                || pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
    }

    @Override
    public void gameUpdate(final int lastKey) throws GameOverException {
        updateDirection(lastKey);

        // Erase the previous position of the head.
        setGameboardState(this.headPos, BLANK_TILE);

        // Change collector position.
        this.headPos = getNextHeadPos();

        if (isOutOfBounds(this.headPos)) {
            throw new GameOverException(this.score);
        }

        //If a body tile and head tile are in the same pos. throw an exception...

        // Draw collector at new position.
        setGameboardState(this.headPos, HEAD_TILE);

        // Remove the coin at the new collector position (if any)   ????
        if(getGameboardState(headPos) == COIN_TILE) {
            Position oldCoinPos = headPos;
            setGameboardState(oldCoinPos, BLANK_TILE);
            addCoin();
            this.score++;
        }

    }

}
