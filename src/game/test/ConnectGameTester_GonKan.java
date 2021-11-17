package game.test;

import game.ConnectDisplay;

import javax.swing.*;

import static game.Constants.QLearn.QUALITIES_FILE;

/**
 * game.Tester class for ConnectGame
 *
 * @author Nathan Gong and Nathan Kang
 */
class ConnectGameTester_GonKan {
    public static void main(String[] args) {
        System.out.println("Please select the qualities.txt file");
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            QUALITIES_FILE = fileChooser.getSelectedFile();
            ConnectDisplay display = ConnectDisplay.getInstance();
            display.start(args);
        } else {
            System.out.println("You did not choose any file.");
        }
    }
}

