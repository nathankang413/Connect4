package game.util;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;

import java.awt.*;

import static game.Constants.GUI.*;

public class PercentBar implements Addable{

    private final GRect total;
    private final GRect percent;
    private final GLabel rateLabel;
    private final GLabel countLabel;
    private final int posX;
    private final int posY;

    /**
     * @param x the x position of the upper left corner of the bar
     * @param y the y position of the upper left corner of the bar
     * @param rate the win rate in range [0,1]; negative win-rates signify that move has not been played
     * */
    public PercentBar(int x, int y, double rate, int numGames) {
        posX = x;
        posY = y;

        total = new GRect(x, y, PERCENT_BAR_WIDTH, PERCENT_BAR_HEIGHT);
        total.setFillColor(Color.LIGHT_GRAY);
        total.setFilled(true);

        percent = new GRect(x, y, PERCENT_BAR_WIDTH, PERCENT_BAR_HEIGHT);
        percent.setFillColor(Color.BLACK);
        percent.setFilled(true);

        rateLabel = new GLabel("", x+PERCENT_BAR_PADDING, y+PERCENT_BAR_HEIGHT-PERCENT_BAR_PADDING);
        rateLabel.setFont(PERCENT_BAR_FONT);
        rateLabel.setColor(Color.WHITE);

        countLabel = new GLabel("", x+PERCENT_BAR_PADDING+100, y+PERCENT_BAR_HEIGHT-PERCENT_BAR_PADDING);
        countLabel.setFont(PERCENT_BAR_FONT);
        countLabel.setColor(Color.WHITE);

        update(rate, numGames);
    }

    /**
     * @param rate the win rate in range [0,1]; negative win-rates signify that move has not been played
     * */
    public void update(double rate, int numGames) {

        if (rate < 0) {
            percent.setSize(0, PERCENT_BAR_HEIGHT);
            rateLabel.setLabel("Unplayed");
        } else if (rate <= 1) {
            percent.setSize(PERCENT_BAR_WIDTH*rate, PERCENT_BAR_HEIGHT);
            rateLabel.setLabel(String.format("%.2f", rate*100));
        } else {
            throw new IllegalArgumentException("rate " + rate + " should not be greater than 1");
        }

        countLabel.setLabel(String.format("%d", numGames));
        double textWidth = countLabel.getBounds().getWidth();
        countLabel.setLocation(posX+PERCENT_BAR_WIDTH-PERCENT_BAR_PADDING-textWidth,
                posY+PERCENT_BAR_HEIGHT-PERCENT_BAR_PADDING);
    }

    public GObject[] getComponents() {
        return new GObject[] {total, percent, rateLabel, countLabel};
    }

}
