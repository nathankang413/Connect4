package game;

public class Constants {
    public static class Game {
        public static final int ROWS = 6;
        public static final int COLS = 7;
        public static final int WIN_COND = 4;
        public static final int EMPTY = -1;
        public static final int PLAYER_1 = 0;
        public static final int PLAYER_2 = 1;
    }

    public static class GUI {
        public static final int POS_DIAMETER = 100;
        public static final int POS_MARGIN = 10;
        public static final int SPACING = POS_DIAMETER + 2 * POS_MARGIN;
        public static final int BOARD_WIDTH = SPACING * Game.COLS;

        public static final int TEXT_MARGIN = 100;
        public static final int TEXT_PADDING = 10;
        public static final String TITLE_FONT = "Arial-bold-100";

        public static final int BUTTON_HEIGHT = 50;
        public static final int BUTTON_WIDTH = 160;
        public static final int BUTTON_PADDING = 10;
        public static final String BUTTON_FONT = "Arial-bold-40";
    }

    public static class QLearn {
        public static final String QUALITIES_FILE = "src/game/qualities.txt";
    }
}
