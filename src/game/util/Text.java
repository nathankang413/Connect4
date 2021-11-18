package game.util;

import acm.graphics.GLabel;

import java.awt.*;

import static game.Constants.GUI.*;

/**
 * Displays text
 */
public class Text extends GLabel {
    /**
     * @param str   the text to be displayed
     * @param color the color of the text
     */
    public Text(String str, Color color) {
        super(str, TEXT_PADDING, TEXT_MARGIN - TEXT_PADDING);
        setFont(TITLE_FONT);
        setColor(color);
    }
}