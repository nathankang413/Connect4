package game.players;

import game.DatabaseIO;

import java.util.ArrayList;
import java.util.Map;

import static game.Constants.Game.COLS;

/**
 * TODO: docs
 */
public class QLearnPlayer extends AIPlayer {
    // TODO: move to constants?
    public static final int NORMAL = 0;
    public static final int NEW = 1;
    public static final int RANDOM = 2;
    private static final int MAX_STATES = 10000;
    private final double useRand;
    private final boolean onlyNew;
    private final Map<String, Double[]> movesMap;

    /**
     * TODO: docs
     *
     * @param logic 0 - normal, 1 - new moves, 2 - random moves
     */
    public QLearnPlayer(int logic) {

        movesMap = DatabaseIO.readHistory();

        double numMapped = movesMap.size();

        switch (logic) {
            case NORMAL -> {
                onlyNew = false;
                useRand = Math.exp(-numMapped / MAX_STATES);
            }
            case NEW -> {
                onlyNew = true;
                useRand = Math.exp(-numMapped / MAX_STATES);
            }
            case RANDOM -> {
                onlyNew = false;
                useRand = 1;
            }
            default -> throw new IllegalArgumentException("Invalid QLearn logic type: " + logic);
        }
    }

    /**
     * TODO: docs
     */
    public QLearnPlayer() {
        this(0);
    }

    /**
     * TODO: docs
     *
     * @param board
     * @param playerNum
     * @return
     */
    public int play(int[][] board, int playerNum) {
        // if can win in one, play it
        for (int i = 0; i < board[0].length; i++) {
            if (checkDrop(board, playerNum, i) == 4) {
                return i;
            }
        }

        // if opponent can win in one, prevent it
        for (int i = 0; i < board[0].length; i++) {
            if (checkDrop(board, 1 - playerNum, i) == 4) {
                return i;
            }
        }

        // if using least-played move
        if (onlyNew) {
            return getNewMove(board, playerNum);
        }

        // if using a random value, generate random value
        else if (Math.random() < useRand) {
            return getRandomMove(board, playerNum);
        }

        // if not, check best states
        else {
            return getBestMove(board, playerNum);
        }
    }

    /**
     * TODO: docs
     *
     * @param board
     * @param playerNum
     * @return
     */
    private int getBestMove(int[][] board, int playerNum) {
        // System.out.println("Checking past experience");
        double bestQ = 0;
        int bestMove = -1;
        for (int i = 0; i < COLS; i++) {
            String stateMove = DatabaseIO.boardToDatabaseString(board) + "-" + i;
            if (movesMap.containsKey(stateMove)) {
                double averageQ = movesMap.get(stateMove)[0] / movesMap.get(stateMove)[1];
                if (averageQ > bestQ) {
                    bestMove = i;
                    bestQ = averageQ;
                }
            }
        }
        if (bestQ > 0) {
            return bestMove;
        } else {
            return getRandomMove(board, playerNum);
        }
    }

    /**
     * TODO: docs
     *
     * @param board
     * @param playerNum
     * @return
     */
    private int getNewMove(int[][] board, int playerNum) {
        ArrayList<Integer> leastPlayed = new ArrayList<>();
        double min = 999;
        // find least-played moves
        for (int i = 0; i < COLS; i++) {
            String stateMove = DatabaseIO.boardToDatabaseString(board) + "-" + i;
            if (movesMap.containsKey(stateMove)) {
                int count = (int) (double) movesMap.get(stateMove)[1];
                if (count < min) {
                    leastPlayed = new ArrayList<>();
                    leastPlayed.add(i);
                    min = count;
                } else if (count == min) {
                    leastPlayed.add(i);
                }
            } else {
                if (min == 0) {
                    leastPlayed.add(i);
                } else {
                    leastPlayed = new ArrayList<>();
                    leastPlayed.add(i);
                    min = 0;
                }
            }
        }
        // weed out illegal moves
        for (int i = leastPlayed.size() - 1; i >= 0; i--) {
            if (checkDrop(board, playerNum, leastPlayed.get(i)) < 0) {
                leastPlayed.remove(i);
            }
        }

        // get random move from the leastPlayed
        if (leastPlayed.size() <= 0) {
            return getRandomMove(board, playerNum);
        } else {
            return leastPlayed.get((int) (Math.random() * leastPlayed.size()));
        }
    }

    /**
     * TODO: docs
     *
     * @param board
     * @param playerNum
     * @return
     */
    private int getRandomMove(int[][] board, int playerNum) {
        int move;
        do { // generate randoms until get a legal move
            move = (int) (Math.random() * COLS);
        } while (checkDrop(board, playerNum, move) < 0);
        return move;
    }
}
