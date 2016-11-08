package lab1;

import java.awt.*;

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

    /** The position of the collector. */
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
        // Insert the collector in the middle of the gameboard.
        this.headPos = new Position(size.width / 2, size.height / 2);
        setGameboardState(this.headPos, HEAD_TILE);

        // Insert coins into the gameboard.
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
     * Return whether the specified position is empty.
     *
     * @param pos
     *            The position to test.
     * @return true if position is empty.
     */
    private boolean isPositionEmpty(final Position pos) {
        return (getGameboardState(pos) == BLANK_TILE);
    }

    @Override
    public void gameUpdate(final int lastKey) throws GameOverException {

    }
}
