package game;

import game.ui.ConnectDisplay;

/**
 * Tester class for ConnectGame
 *
 * @author Nathan Gong and Nathan Kang
 */
class Main_GonKan {
    public static void main(String[] args) {
        ConnectDisplay display = ConnectDisplay.getInstance();
        display.start(args);
    }
}

