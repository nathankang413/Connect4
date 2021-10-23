package game;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static game.Constants.Game.*;

public class QLearnGameTester {
    public static void main(String[] args) throws IOException {
        double total;
        int count;
        int tieCount;

        // 1000 games QLearn vs QLearn
        total = 0;
        count = 1000;
        tieCount = 0;
        for (int i = 0; i < 1000; i++) {
            QLearnGame game = new QLearnGame();
            double result = game.playGame(PLAYER_1, false);
            total += result;
            if (result == 0.5) {
                tieCount++;
            }
        }
        System.out.println("Player 1 QLearn win rate: " + (1 - total / count));
        System.out.println("Ties: " + tieCount);

        // 1000 games AI vs QLearn
        total = 0;
        count = 1000;
        for (int i = 0; i < 1000; i++) {
            QLearnGame game = new QLearnGame(false);
            double result = game.playGame(PLAYER_1, false);
            total += result;
            if (result == 0.5) {
                tieCount++;
            }
        }
        System.out.println("QLearn win rate: " + (total / count));
        System.out.println("Ties: " + tieCount);

        // Count number of state-move pairs recorded
        Scanner s = new Scanner(new File("game/qualities.txt"));
        int countQ = 0;
        while (s.hasNextLine()) {
            countQ++;
            s.nextLine();
        }
        System.out.println("Positions reached: " + countQ);

        // QLearnGame game = new QLearnGame(true);
        // try {
        //     game.playGame((int) (Math.random()+0.5));
        // } catch (ClassCastException e) {
        //     System.out.println(e);
        // }
    }
}
