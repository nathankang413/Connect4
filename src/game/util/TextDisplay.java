package game.util;

import acm.graphics.GLabel;
import game.ConnectDisplay;

import java.awt.*;

import static game.Constants.GUI.*;

public class TextDisplay extends GLabel {
    public TextDisplay(String str, Color color) {
        super(str, TEXT_PADDING, TEXT_MARGIN-TEXT_PADDING);
        setFont(TITLE_FONT);
        setColor(color);
        ConnectDisplay.getInstance().add(this);
    }
}