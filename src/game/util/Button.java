package game.util;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.Constants.GUI.*;

/**
 * A button with text that performs an action when clicked
 */
public abstract class Button implements Addable {
    GRect buttonShape;
    GLabel buttonText;

    /**
     * @param x     - x position of button
     * @param y     - y position of button
     * @param label - text label of button
     * @param color - button color
     */
    public Button(int x, int y, String label, Color color) {

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

        buttonText = new GLabel(label, x + BUTTON_PADDING, y + BUTTON_HEIGHT - BUTTON_PADDING);
        buttonText.setFont(BUTTON_FONT);
    }

    /**
     * The action that's performed when button is clicked
     */
    protected abstract void buttonAction();

    @Override
    public GObject[] getComponents() {
        return new GObject[]{buttonShape, buttonText};
    }
}