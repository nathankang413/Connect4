package game;

import game.players.AIPlayer;
import game.players.HumanPlayer;
import game.players.Player;
import game.players.QLearnPlayer;

import java.io.IOException;

public class QLearnGame extends ConnectGame {

    public QLearnGame() {
        super(new QLearnPlayer[]{new QLearnPlayer(), new QLearnPlayer()});
    }

    public QLearnGame(boolean human) {
        super(new Player[]{human ? new HumanPlayer() : new AIPlayer(), new QLearnPlayer()});
    }

    public double playGame(int startPlayer) {
        return playGame(startPlayer, true);
    }

    public double playGame(int startPlayer, boolean showBoard) {
        double result = super.playGame(startPlayer, showBoard);
        // System.out.println("Finished Game");
        for (int i = 0; i < 2; i++) {
            try {
                QLearnPlayer player = (QLearnPlayer) players[i];
                player.update(Math.abs(1 - i - result));
            } catch (ClassCastException e) {
                if (showBoard) {
                    System.out.println(e);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return result;
    }

}
