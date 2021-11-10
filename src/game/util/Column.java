package game.util;

import acm.graphics.GRect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.Constants.GUI.COLUMN_HIGHLIGHT;
import static game.Constants.GUI.SPACING;
import static game.Constants.Game.ROWS;

public class Column extends GRect {
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