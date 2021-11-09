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
        ConnectDisplay display = ConnectDisplay.getInstance();
        display.start(args);
    }
}

