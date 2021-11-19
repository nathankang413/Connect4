package game.ui;

import acm.graphics.GOval;

import java.awt.*;

import static game.util.Constants.GUI.POS_DIAMETER;
import static game.util.Constants.Game.*;

/**
 * Individual slot in Connect 4 game
 */
public class Slot extends GOval {
    /**
     * @param x    x position of slot
     * @param y    y position of slot
     * @param type whether slot is empty, filled by player 1, or filled by player 2
     */
    public Slot(int x, int y, int type) {
        super(x, y, POS_DIAMETER, POS_DIAMETER);
        updateColor(type);
    }

    /**
     * Updates the color of the slot given a type
     *
     * @param type the type of the slot (whether it's empty or filled by either player)
     */
    public void updateColor(int type) {
        switch (type) {
            case EMPTY -> setFillColor(Color.LIGHT_GRAY);
            case PLAYER_1 -> setFillColor(Color.YELLOW);
            case PLAYER_2 -> setFillColor(Color.RED);
        }
        setFilled(true);
    }
}