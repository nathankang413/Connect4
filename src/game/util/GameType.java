package game.util;

import game.players.*;

public class GameType {
    // TODO: move to constants?
    public final static int HUMAN = 0;
    public final static int ALGORITHM = 1;
    public final static int Q_LEARN = 2;
    public final static int Q_LEARN_NEW = 3;
    public final static int Q_LEARN_RAND = 4;
    private final int[] playerTypes;

    public GameType(int player1, int player2) {

        // TODO: remove magic numbers
        if (player1 > 4 || player1 < 0) {
            throw new IllegalArgumentException("player1 type " + player1 + " is invalid.");
        }
        if (player2 > 4 || player2 < 0) {
            throw new IllegalArgumentException("player2 type " + player2 + " is invalid.");
        }

        playerTypes = new int[] {player1, player2};
    }

    public Player[] getPlayers() {

        Player[] players = new Player[2];

        int startPlayer = (int) (Math.random() + 0.5);

        for (int i=0; i<2; i++) {
            switch (playerTypes[i]) {
                case HUMAN -> players[(i + startPlayer) % 2] = new HumanPlayer();
                case ALGORITHM -> players[(i + startPlayer) % 2] = new AlgorithmicPlayer();
                case Q_LEARN -> players[(i + startPlayer) % 2] = new QLearnPlayer(QLearnPlayer.NORMAL);
                case Q_LEARN_NEW -> players[(i + startPlayer) % 2] = new QLearnPlayer(QLearnPlayer.NEW);
                case Q_LEARN_RAND -> players[(i + startPlayer) % 2] = new QLearnPlayer(QLearnPlayer.RANDOM);
            }
        }

        return players;
    }

}
