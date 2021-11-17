package game.players;

/**
 * A Player interface for the ConnectGame class
 */
public interface Player {

    /**
     * plays one turn of Connect4
     *
     * @param board the current board state
     * @param playerNum the number of the player
     * @return the column in which to drop the piece
     */
    int play(int[][] board, int playerNum);
}