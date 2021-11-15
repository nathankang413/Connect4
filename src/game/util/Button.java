package game.util;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.Constants.GUI.*;

/**
 * TODO: docs
 */
public abstract class Button implements Addable{
    GRect buttonShape;
    GLabel buttonText;

    /**
     * TODO: docs
     * @param x
     * @param y
     * @param str
     * @param color
     */
    public Button(int x, int y, String str, Color color) {

        buttonShape = new GRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonShape.setFillColor(color);
        buttonShape.setFilled(true);

        buttonShape.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (
                    e.getX() > x &&
                    e.getX() < x + BUTTON_WIDTH &&
                    e.getY() > y &&
                    e.getY() < y + BUTTON_HEIGHT
                ) {
                    buttonAction();
                }
            }
        });

        buttonText = new GLabel(str, x + BUTTON_PADDING, y + BUTTON_HEIGHT - BUTTON_PADDING);
        buttonText.setFont(BUTTON_FONT);
    }

    /**
     * TODO: docs
     */
    protected abstract void buttonAction();

    /**
     * TODO: docs
     * @return
     */
    @Override
    public GObject[] getComponents() {
        return new GObject[] {buttonShape, buttonText};
    }
}