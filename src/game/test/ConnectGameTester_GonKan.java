package game.test;

import game.ConnectGame;
import game.Display;

/**
 * game.Tester class for ConnectGame
 * TODO: rename to ConnectGameTester_GonKan for submission
 *
 * @author Nathan Gong and Nathan Kang
 */
class ConnectGameTester_GonKan {
    public static void main(String[] args) {
        ConnectGame game = new ConnectGame(2);
        Display display = new Display(game);
        display.start(args);
        System.out.println("here");
//        game.playGame(0, false);

        // Player p = new HumanPlayer();
        // System.out.println(p.play(game.board));
    }
}

