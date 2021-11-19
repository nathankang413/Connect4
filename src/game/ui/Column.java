package game.ui;

import acm.graphics.GRect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.util.Constants.GUI.COLUMN_HIGHLIGHT;
import static game.util.Constants.GUI.SPACING;
import static game.util.Constants.Game.ROWS;

/**
 * An individual Column in the Connect4 game
 */
public class Column extends GRect {
    /**
     * @param x x-position of the column
     * @param y y-position of column
     */
    public Column(int x, int y) {
        super(x, y, SPACING, ROWS * SPACING);

        setFillColor(Color.BLUE);
        setFilled(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setFillColor(COLUMN_HIGHLIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setFillColor(Color.BLUE);
            }
        });
    }
}