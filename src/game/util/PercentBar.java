package game.util;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;

import java.awt.*;

import static game.Constants.GUI.*;

public class PercentBar implements Addable{

    private GRect total;
    private GRect percent;
    private GLabel label;

    /**
     * @param x the x position of the upper left corner of the bar
     * @param y the y position of the upper left corner of the bar
     * @param rate the win rate between 0 and 1, inclusive
     * */
    public PercentBar(int x, int y, double rate) {

        total = new GRect(x, y, PERCENT_BAR_WIDTH, PERCENT_BAR_HEIGHT);
        total.setFillColor(Color.LIGHT_GRAY);
        total.setFilled(true);

        percent = new GRect(x, y, PERCENT_BAR_WIDTH*rate, PERCENT_BAR_HEIGHT);
        percent.setFillColor(Color.BLACK);
        percent.setFilled(true);

        label = new GLabel( String.format("%.2f", rate*100),
                x+PERCENT_BAR_PADDING, y+PERCENT_BAR_HEIGHT-PERCENT_BAR_PADDING);
        label.setFont(PERCENT_BAR_FONT);
        label.setColor(Color.WHITE);

    }

    /**
     * @param rate the win rate between 0 and 1, inclusive
     * */
    public void changeRate(double rate) {
        label.setFont(String.format("%.2f", rate*100));
    }

    public GObject[] getComponents() {
        return new GObject[] {total, percent, label};
    }

}
