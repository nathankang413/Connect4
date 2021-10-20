import java.io.IOException;

public class QLearnGame extends ConnectGame {

    public QLearnGame () throws IOException {
        super( new QLearnPlayer[] {new QLearnPlayer(), new QLearnPlayer()} );
    }

    public double playGame(int startPlayer) {
        double result = super.playGame(startPlayer);
        System.out.println("Finished Game");
        for (int i=0; i<2; i++) {
            if (i == result) {
                QLearnPlayer player = (QLearnPlayer) players[i];
                player.update(1);
            } else {
                QLearnPlayer player = (QLearnPlayer) players[i];
                player.update(0);
            }
        }

        return result;
    }

}
