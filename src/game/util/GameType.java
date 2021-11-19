package game.util;

import game.players.AlgorithmicPlayer;
import game.players.HumanPlayer;
import game.players.Player;
import game.players.QLearnPlayer;

/**
 * A helper class to generate players for Connect4
 */
public class GameType {
    public final static int HUMAN = 0;
    public final static int ALGORITHM = 1;
    public final static int Q_LEARN = 2;
    public final static int Q_LEARN_NEW = 3;
    public final static int Q_LEARN_RAND = 4;

    private final int[] playerTypes;

    /**
     * Creates a new GameType with the given types of players
     *
     * @param player1 type of player 1
     * @param player2 type of player 2
     */
    public GameType(int player1, int player2) {
        if (invalidGameType(player1)) {
            throw new IllegalArgumentException("player1 type " + player1 + " is invalid.");
        }
        if (invalidGameType(player2)) {
            throw new IllegalArgumentException("player2 type " + player2 + " is invalid.");
        }

        playerTypes = new int[]{player1, player2};
    }

    /**
     * Checks whether the given player type is valid
     *
     * @param type the given player type
     * @return true if the type is invalid
     */
    private boolean invalidGameType(int type) {
        return type != HUMAN && type != ALGORITHM && type != Q_LEARN && type != Q_LEARN_NEW && type != Q_LEARN_RAND;
    }

    /**
     * Generates the correct players types in random order
     *
     * @return an array of the correct players in random order
     */
    public Player[] getPlayers() {
        Player[] players = new Player[2];

        int startPlayer = (int) (Math.random() * 2);

        for (int i = 0; i < 2; i++) {
            switch (playerTypes[i]) {
                case HUMAN -> players[(i + startPlayer) % 2] = new HumanPlayer();
                case ALGORITHM -> players[(i + startPlayer) % 2] = new AlgorithmicPlayer();
                case Q_LEARN -> players[(i + startPlayer) % 2] = new QLearnPlayer();
                case Q_LEARN_NEW -> players[(i + startPlayer) % 2] = new QLearnPlayer(GameType.Q_LEARN_NEW);
                case Q_LEARN_RAND -> players[(i + startPlayer) % 2] = new QLearnPlayer(GameType.Q_LEARN_RAND);
            }
        }

        return players;
    }
}
