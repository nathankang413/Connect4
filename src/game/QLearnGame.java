package game;

import java.io.IOException;

public class QLearnGame extends ConnectGame {

    public QLearnGame() throws IOException {
        super(new QLearnPlayer[]{new QLearnPlayer(), new QLearnPlayer()} );
    }

    public QLearnGame(boolean human) throws IOException {
        super(new Player[]{human ? new HumanPlayer() : new AIPlayer(), new QLearnPlayer() });
    }

    public double playGame(int startPlayer) {
        double result = super.playGame(startPlayer);
        System.out.println("Finished Game");
        for (int i = 0; i < 2; i++) {
            try {
                QLearnPlayer player = (QLearnPlayer) players[i];
                player.update(Math.abs(i-result));
            } catch (ClassCastException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return result;
    }

}
