package game;

public class Constants {
    public class Game {
        public static final int ROWS = 6;
        public static final int COLS = 7;
        public static final int WIN_COND = 4;
        public static final int EMPTY = -1;
        public static final int PLAYER_1 = 0;
        public static final int PLAYER_2 = 1;
    }

    public class GUI {
        public static final int DIAMETER = 100;
        public static final int MARGIN = 10;
        public static final int SPACING = DIAMETER + 2 * MARGIN;
    }

    public class QLearn {
        public static final String QUALITIES_FILE = "src/game/qualities.txt";
    }
}
