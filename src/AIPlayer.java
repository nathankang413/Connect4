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

        // find the drops which will make the biggest connect
        int[] bestDrops = new int[7];
        int numDrops = 0;
        int bestConnect = 0;
        
        for (int i = 0; i < board[0].length; i++) {
            int connect = checkDrop(board, playerNum, i);
            if (connect == 4) return i;
            else if (connect > bestConnect) {
                bestConnect = connect;
                numDrops = 1;
                bestDrops[0] = i;
            } else if (connect == bestConnect) {
                bestDrops[numDrops++] = i;
            }
        }

        int randInd = (int) (Math.random() * numDrops);
        return bestDrops[randInd];
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
        int[][] dirs = {{0, 1}, {1, 1}, {1, 0}, {1, -1}};

        // check in all directions
        for (int[] dir : dirs) { 

            int count = 1;

            while (true) {
                
                int newPosX = dropPos[0] + count * dir[0];
                int newPosY = dropPos[1] + count * dir[1];

                // check out of bounds
                if (newPosX < 0 || newPosX >= Constants.ROWS) 
                    break;
                if (newPosY < 0 || newPosY >= Constants.COLS)
                    break;
                
                // check the correct piece
                if (board[newPosX][newPosY] == playerNum) {
                    count++;
                } else {
                    break;
                }

                if (count >= Constants.WIN_COND) {
                    return 4;
                }

            }

            // update maxConnect if necessary
            if (count > maxConnect) 
                maxConnect = count;
        }

        return maxConnect;

    }

}
