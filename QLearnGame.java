import java.io.IOException;

public class QLearnGame extends ConnectGame {

    public QLearnGame () {
        super( new QLearnPlayer[] {new QLearnPlayer(), new QLearnPlayer()} );
    }

    public double playGame(int startPlayer) {
        double result = super.playGame(startPlayer);
        System.out.println("Finished Game");
        for (int i=0; i<2; i++) {
            try {
            QLearnPlayer player = (QLearnPlayer) players[i];
            player.update(Math.abs(i-result));
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return result;
    }

}
