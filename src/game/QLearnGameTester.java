package game;

import java.io.*;

public class QLearnGameTester {
    
    public static void main (String[] args) throws IOException {

        double total;
        int count;


        // // 1000 games QLearn vs QLearn
        // total = 0;
        // count = 1000;
        // for (int i=0; i<1000; i++) {
        //     QLearnGame game = new QLearnGame();
        //     total += game.playGame(0, false);
        // }
        // System.out.println("Player 1 QLearn win rate: " + (1- total/count));


        for (int j=0; j<4; j++) {
            // 1000 games AI vs QLearn
            total = 0;
            count = 1000;
            for (int i=0; i<1000; i++) {
                QLearnGame game = new QLearnGame(false);
                total += game.playGame(i%2, false);
            }
            System.out.println("QLearn win rate: " + (total/count));
        }


        // QLearnGame game = new QLearnGame(false);
        // try {
        //     game.playGame(1);
        // } catch (ClassCastException e) {
        //     System.out.println(e);
        // }

    }

}
