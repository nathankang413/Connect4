package game.util;

import acm.graphics.GOval;
import game.ConnectDisplay;

import java.awt.*;

import static game.Constants.GUI.POS_DIAMETER;
import static game.Constants.Game.*;

public class Slot extends GOval {
    public Slot(int x, int y, int type) {
        super(x, y, POS_DIAMETER, POS_DIAMETER);

        changeColor(type);

        ConnectDisplay.getInstance().add(this);
    }

    public void changeColor(int type) {
        switch (type) {
            case EMPTY -> setFillColor(Color.LIGHT_GRAY);
            case PLAYER_1 -> setFillColor(Color.YELLOW);
            case PLAYER_2 -> setFillColor(Color.RED);
        }
        setFilled(true);
    }
}