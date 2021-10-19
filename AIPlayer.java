/**
 An AI Player which determines moves algorithmically
 */
public class AIPlayer implements Player {

    /**
     Plays one turn of connect for
     @param board the game board
     @return the column to drop the piece, zero-indexed
     */
    public int play(int[][] board, int playerNum) {
        // if any drop will make connect 4, do it
        for (int i=0; i<board[0].length; i++) {
            if (checkDrop(board, playerNum, i) == 4)
                return i;
        }

        // otherwise, drop at random
        return (int) (Math.random() * board[0].length);
    }

    /**
      TODO
      @param board
      @param playerNum
      @param col
      @return
     */
    private int checkDrop(int[][] board, int playerNum, int col) {

        // if piece would be out of bounds
        if (board[0][col] > -1) return 0;

        int[] dropPos = new int[2];
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][col] == -1) {
                dropPos[0] = i;
                dropPos[1] = col;
                break;
            }
        }
        
        int maxConnect = 0;
        int[][] dirs = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        for (int[] dir : dirs) {
            int count = 1;
            do {
                int[] newPos = {dropPos[0] + dir[0], dropPos[1] + dir[1]};

                // check out of bounds
                if (newPos[0] >= board.length || newPos[0] < 0) break;
                if (newPos[1] >=board[newPos[0]].length || newPos[1] < 0) break;

                // check if the piece is correct
                if (board[newPos[0]][newPos[1]] == playerNum) {
                    count++;
                }

            } while (count < 4);

            if (count > maxConnect) {
                maxConnect = count;
            }
        }

        return 0;

    }

}
