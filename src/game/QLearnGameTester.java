package game;

import java.io.*;

public class QLearnGameTester {
    
    public static void main (String[] args) throws IOException {

        double total = 0;
        int count = 1000;

        for (int i=0; i<1000; i++) {
            QLearnGame game = new QLearnGame();
            total += game.playGame(0);
        }

        System.out.println("Player 1 win rate: " + (1- total/count));

    }

}
