package game.test;

import game.ConnectGame;
import game.ConnectDisplay;
import static game.Constants.Game.*;

/**
 * game.Tester class for ConnectGame
 *
 * @author Nathan Gong and Nathan Kang
 */
class ConnectGameTester_GonKan {
    public static void main(String[] args) {
//        ConnectGame game = new ConnectGame(2, PLAYER_1);
        ConnectDisplay display = ConnectDisplay.getInstance();
        display.start(args);
        System.out.println("here");
//        game.playGame(0, false);

        // Player p = new HumanPlayer();
        // System.out.println(p.play(game.board));
    }
}

