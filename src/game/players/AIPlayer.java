package game.players;

import static game.Constants.Game.*;

/**
 * TODO: docs
 */
public abstract class AIPlayer implements Player {
    /**
     * Determines the "score" for a given drop. The score is defined as the largest streak of pieces this drop will form.
     *
     * @param board     - the board
     * @param playerNum - number of current player
     * @param col       - the column of the drop
     * @return the score
     */
    protected int checkDrop(int[][] board, int playerNum, int col) {
        // if piece would be out of bounds
        if (board[0][col] > -1) return -1;

        int[] dropPos = new int[2];
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][col] == -1) {
                dropPos[0] = i;
                dropPos[1] = col;
                break;
            }
        }

        int maxConnect = 0;
        int[][] dirs = {{0, 1}, {1, 1}, {1, 0}, {1, -1}};

        // check in all directions
        for (int[] dir : dirs) {
            int count = 1;

            for (int i = -1; i < 2; i += 2) { // go in both directions
                int distance = 1;
                while (true) {
                    int newPosX = dropPos[0] + distance * dir[0] * i;
                    int newPosY = dropPos[1] + distance * dir[1] * i;

                    // check out of bounds
                    if (newPosX < 0 || newPosX >= ROWS)
                        break;
                    if (newPosY < 0 || newPosY >= COLS)
                        break;

                    // check the correct piece
                    if (board[newPosX][newPosY] == playerNum) {
                        distance++;
                        count++;
                    } else {
                        break;
                    }
                    if (count >= WIN_COND) {
                        return 4;
                    }
                }
            }

            // update maxConnect if necessary
            if (count > maxConnect) {
                maxConnect = count;
            }
        }

        return maxConnect;
    }
}
