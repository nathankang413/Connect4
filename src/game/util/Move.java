package game.util;

import static game.Constants.Game.COLS;
import static game.Constants.Game.ROWS;

/**
 * TODO: docs
 */
public record Move (int[][] board, int move) { // TODO: implement usage

    /**
     * TODO: docs
     *
     * @return
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
