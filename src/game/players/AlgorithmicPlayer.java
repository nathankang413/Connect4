package game.players;

import java.util.ArrayList;
import java.util.List;

import static game.Constants.Game.WIN_COND;

/**
 * An AI Player which determines moves algorithmically
 */
public class AlgorithmicPlayer extends AIPlayer {
    @Override
    public int play(int[][] board, int playerNum) {
        // find the drops which will make the biggest connect
        List<Integer> bestDrops = new ArrayList<>();
        int bestConnect = 0;

        // if it can win in 1 move, play that move; 
        // otherwise, store the best consecutive made with any move
        for (int i = 0; i < board[0].length; i++) {
            int connect = checkDrop(board, playerNum, i);
            if (connect == 4) return i;
            else if (connect > bestConnect) {
                bestConnect = connect;
                bestDrops.clear();
                bestDrops.add(i);
            } else if (connect == bestConnect) {
                bestDrops.add(i);
            }
        }

        // if the opponent can win in one, prevent it
        for (int i = 0; i < board[0].length; i++) {
            if (checkDrop(board, 1 - playerNum, i) == WIN_COND) {
                return i;
            }
        }

        int randomIndex = (int) (Math.random() * bestDrops.size());
        return bestDrops.get(randomIndex);
    }
}
