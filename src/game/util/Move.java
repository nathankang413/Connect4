package game.util;

import static game.Constants.Game.COLS;
import static game.Constants.Game.ROWS;

/**
 * A Move record to store a board state and move
 * For efficiency, mirror board states are treated the same
 */
public record Move (int[][] board, int move) { // TODO: implement usage

    /**
     * Converts the board state and move into a string
     * Mirror boards are converted to the same string, the move remains correct relative to the mirrored board
     *
     * @return a String representation of the board and move
     */
    public String toString() {
        StringBuilder exact = new StringBuilder();
        StringBuilder mirror = new StringBuilder();
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {
                exact.append(board[i][j] + 1);
                mirror.append(board[i][COLS-j-1] + 1);
            }
        }
        return exact.compareTo(mirror) > 0 ? exact + "-" + move : mirror + "-" + (COLS-move-1);
    }

}
