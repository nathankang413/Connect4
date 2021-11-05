package game.util;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import game.ConnectDisplay;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.Constants.GUI.*;
import static game.Constants.GUI.BUTTON_HEIGHT;

public abstract class Button extends GRect {
    GLabel buttonText;

    public Button(int x, int y, String str) {
        super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);

        ConnectDisplay display = ConnectDisplay.getInstance();
        setFillColor(Color.GREEN);
        setFilled(true);
        display.add(this);

        buttonText = new GLabel(str, x+ BUTTON_PADDING, y+BUTTON_HEIGHT- BUTTON_PADDING);
        buttonText.setFont(BUTTON_FONT);
        display.add(buttonText);

        display.addMouseListeners(new ButtonClick(x, x + BUTTON_WIDTH, y, y + BUTTON_HEIGHT));
    }

    protected abstract void buttonAction();

    private class ButtonClick extends MouseAdapter {
        int xLeft, xRight, yUp, yDown;

        public ButtonClick(int xLeft, int xRight, int yUp, int yDown) {
            this.xLeft = xLeft;
            this.xRight = xRight;
            this.yUp = yUp;
            this.yDown = yDown;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getX() > xLeft && e.getX() < xRight && e.getY() > yUp && e.getY() < yDown)
                buttonAction();
        }
    }
}