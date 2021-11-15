package game.util;

import acm.graphics.GOval;

import java.awt.*;

import static game.Constants.GUI.POS_DIAMETER;
import static game.Constants.Game.*;

/**
 * TODO: docs
 */
public class Slot extends GOval {

    /**
     * TODO: docs
     * @param x
     * @param y
     * @param type
     */
    public Slot(int x, int y, int type) {
        super(x, y, POS_DIAMETER, POS_DIAMETER);
        changeColor(type);
    }

    /**
     * TODO: docs
     * @param type
     */
    public void changeColor(int type) {
        switch (type) {
            case EMPTY -> setFillColor(Color.LIGHT_GRAY);
            case PLAYER_1 -> setFillColor(Color.YELLOW);
            case PLAYER_2 -> setFillColor(Color.RED);
        }
        setFilled(true);
    }
}