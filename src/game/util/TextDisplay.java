package game.util;

import acm.graphics.GLabel;

import java.awt.*;

import static game.Constants.GUI.*;

/**
 * TODO: docs
 */
public class TextDisplay extends GLabel {

    /**
     * TODO: docs
     * @param str
     * @param color
     */
    public TextDisplay(String str, Color color) {
        super(str, TEXT_PADDING, TEXT_MARGIN-TEXT_PADDING);
        setFont(TITLE_FONT);
        setColor(color);
    }
}