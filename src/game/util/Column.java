package game.util;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import game.ConnectDisplay;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static game.Constants.GUI.COLUMN_HIGHLIGHT;
import static game.Constants.GUI.SPACING;
import static game.Constants.Game.ROWS;

public class Column extends GRect {
    public Column(int x, int y) {
        super(x, y, SPACING, ROWS * SPACING);

        ConnectDisplay display = ConnectDisplay.getInstance();
        setFillColor(Color.BLUE);
        setFilled(true);
        display.addMouseListeners(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (getBounds().contains(new GPoint(e.getX(), e.getY()))) {
                    setFillColor(COLUMN_HIGHLIGHT);
                } else {
                    setFillColor(Color.BLUE);
                }
            }
        });
        display.add(this);
    }
}