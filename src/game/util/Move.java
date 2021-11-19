package game.util;

import static game.util.Constants.Game.COLS;
import static game.util.Constants.Game.ROWS;

/**
 * A Move record to store a board state and move
 * For efficiency, mirror board states are treated the same
 */
public record Move(int[][] board, int move) implements Comparable<Move> {
    /**
     * Creates a new Move to store the given board and move
     *
     * @param board the current game board
     * @param move  the move made
     */
    public Move(int[][] board, int move) {
        this.board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, COLS);
        }
        this.move = move;
    }

    /**
     * Creates a Move object from its String representation
     *
     * @param str the String representation
     * @return the Move object
     */
    public static Move fromString(String str) {
        String[] split = str.split("-");
        String boardStr = split[0];
        int[][] board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = Character.getNumericValue(boardStr.charAt(i * COLS + j)) - 1;
            }
        }
        int move = Integer.parseInt(split[1]);
        return new Move(board, move);
    }

    /**
     * Converts the board state and move into a string
     * Mirror boards are converted to the same string, the move remains correct relative to the mirrored board
     *
     * @return a String representation of the board and move
     */
    public String toString() {
        StringBuilder exact = new StringBuilder();
        StringBuilder mirror = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                exact.append(board[i][j] + 1);
                mirror.append(board[i][COLS - j - 1] + 1);
            }
        }
        return exact.compareTo(mirror) <= 0 ? exact + "-" + move : mirror + "-" + (COLS - move - 1);
    }

    @Override
    public int hashCode() { // used in HashMap
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move m)) return false;
        return this.toString().equals(m.toString());
    }

    @Override
    public int compareTo(Move o) {
        return this.toString().compareTo(o.toString());
    }
}
