package game.util;

import game.players.AlgorithmicPlayer;
import game.players.HumanPlayer;
import game.players.Player;
import game.players.QLearnPlayer;

/**
 * TODO: docs
 */
public class GameType {
    // TODO: move to constants?
    public final static int HUMAN = 0;
    public final static int ALGORITHM = 1;
    public final static int Q_LEARN = 2;
    public final static int Q_LEARN_NEW = 3;
    public final static int Q_LEARN_RAND = 4;
    private final int[] playerTypes;

    /**
     * TODO: docs
     * @param player1
     * @param player2
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
     * TODO: docs
     * @param type
     * @return
     */
    private boolean invalidGameType(int type) {
        return type != HUMAN && type != ALGORITHM && type != Q_LEARN && type != Q_LEARN_NEW && type != Q_LEARN_RAND;
    }

    /**
     * TODO: docs
     * @return
     */
    public Player[] getPlayers() {
        Player[] players = new Player[2];

        int startPlayer = (int) (Math.random() * 2);

        for (int i = 0; i < 2; i++) {
            switch (playerTypes[i]) {
                case HUMAN -> players[(i + startPlayer) % 2] = new HumanPlayer();
                case ALGORITHM -> players[(i + startPlayer) % 2] = new AlgorithmicPlayer();
                case Q_LEARN -> players[(i + startPlayer) % 2] = new QLearnPlayer();
            }
        }

        return players;
    }
}
