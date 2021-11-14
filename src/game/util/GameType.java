package game.util;

import game.players.*;

public class GameType {

    private final int[] playerTypes;

    public GameType(int player1, int player2) {
        // 0 - human, 1 - Algo, 2 - QLearn, 3 - QLearn-onlyRand

        if (player1 > 3 || player1 < 0) {
            throw new IllegalArgumentException("player1 type " + player1 + " is invalid.");
        }
        if (player2 > 3 || player2 < 0) {
            throw new IllegalArgumentException("player2 type " + player2 + " is invalid.");
        }

        playerTypes = new int[] {player1, player2};
    }

    public Player[] getPlayers() {

        Player[] players = new Player[2];

        int startPlayer = (int) (Math.random() + 0.5);

        for (int i=0; i<2; i++) {
            switch (playerTypes[i]) {
                case 0 -> players[(i + startPlayer) % 2] = new HumanPlayer();
                case 1 -> players[(i + startPlayer) % 2] = new AlgorithmicPlayer();
                case 2 -> players[(i + startPlayer) % 2] = new QLearnPlayer();
                case 3 -> players[(i + startPlayer) % 2] = new QLearnPlayer(true);
            }
        }

        return players;
    }

}
