package game.util;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import game.ConnectDisplay;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.Constants.GUI.*;

public abstract class Button extends GRect {
    GLabel buttonText;

    public Button(int x, int y, String str, Color color) {
        super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);

        ConnectDisplay display = ConnectDisplay.getInstance();
        setFillColor(color);
        setFilled(true);
        display.add(this); // TODO: remove display dependency

        buttonText = new GLabel(str, x + BUTTON_PADDING, y + BUTTON_HEIGHT - BUTTON_PADDING);
        buttonText.setFont(BUTTON_FONT);
        display.add(buttonText); // TODO: remove display dependency

        addMouseListener(new MouseAdapter() {
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
    }

    protected abstract void buttonAction();
}