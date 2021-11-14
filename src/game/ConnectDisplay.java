package game;

import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import acm.program.ProgramMenuBar;
import game.players.HumanPlayer;
import game.util.*;
import game.util.Button;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class ConnectDisplay extends GraphicsProgram implements MouseListener, ActionListener {
    private static ConnectDisplay instance;
    private Column[] frame;
    private Slot[][] positions;
    private TextDisplay title;
    private PercentBar[] winRates;
    private ConnectGame game;
    private final Timer aiTimer;
    private GameType gameType;

    private ConnectDisplay() {
        addMouseListeners();
        gameType = new GameType(0, 0);
        aiTimer = new Timer(AI_DELAY, this);
    }

    public static ConnectDisplay getInstance() {
        if (instance == null) {
            instance = new ConnectDisplay();
        }
        return instance;
    }

    /**
     * Initiates Connect4 display and game
     */
    @Override
    public void run() {

        initMenuBar();

        this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        pause(WINDOW_RESIZE_WAIT);

        title = new TextDisplay("Connect 4", Color.BLUE);
        add(title);

        // set up frame and positions for display
        frame = new Column[COLS];
        for (int j = 0; j < COLS; j++) {
            frame[j] = new Column(j * SPACING, TEXT_MARGIN);
            add(frame[j]);
        }

        positions = new Slot[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j] = new Slot(j * SPACING + POS_MARGIN, i * SPACING + POS_MARGIN + TEXT_MARGIN, EMPTY);
                add(positions[i][j]);
            }
        }

        add(new PlayButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN));
        add(new DatabaseButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN + 3*(BUTTON_HEIGHT + BUTTON_PADDING)));

        // win rate bars underneath the buttons
        for (int i=0; i<COLS; i++) {
            winRates[i] = new PercentBar(BOARD_WIDTH + BUTTON_PADDING,
                    TEXT_MARGIN + 4 * (BUTTON_HEIGHT + BUTTON_PADDING) + i * (PERCENT_BAR_HEIGHT + PERCENT_BAR_PADDING),
                    0.5); // TODO: simplify y-value, remove magic number
            add(winRates[i]); // TODO: should be toggled on and off
        }

    }

    private void initMenuBar() {
        ProgramMenuBar menuBar = getMenuBar();
        JMenu menu = new JMenu("Game Settings");
        menuBar.add(menu);

        JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem("AI Delay");
        cbItem.setSelected(true);
        cbItem.addActionListener(e -> aiTimer.setDelay(AI_DELAY - aiTimer.getDelay()));
        menu.add(cbItem);

        menu.addSeparator();

        ButtonGroup gameTypes = new ButtonGroup();

        // TODO: simplify?
        String[] names = new String[] {"2 Players", "1 Player (vs Algo)",
                "1 Player (vs QLearn)", "Algo v Algo", "Algo v QLearn",
                "QLearn v QLearn"};

        for (int i=0; i<6; i++) {
            JRadioButtonMenuItem button = new JRadioButtonMenuItem(names[i]);
            if (i==0) {
                button.setSelected(true);
            }
            switch(i) { // TODO: simplify?
                case 0 -> button.addActionListener(e -> gameType = new GameType(0, 0));
                case 1 -> button.addActionListener(e -> gameType = new GameType(0, 1));
                case 2 -> button.addActionListener(e -> gameType = new GameType(0, 2));
                case 3 -> button.addActionListener(e -> gameType = new GameType(1, 1));
                case 4 -> button.addActionListener(e -> gameType = new GameType(1, 2));
                case 5 -> button.addActionListener(e -> gameType = new GameType(2, 2));
            }
            gameTypes.add(button);
            menu.add(button);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game != null && game.checkWin() == EMPTY) {
            // determine the chosen column
            int col = -1;
            for (int j = 0; j < COLS; j++) {
                if (frame[j].contains(mouseEvent.getX(), mouseEvent.getY())) {
                    col = j;
                }
            }
            if (col == -1) return; // if click not on board

            // drop the piece
            game.runHumanTurn(col);

            // update screen
            updatePlayerText();
            updateScreen();

            // handle win
            if (game.checkWin() != EMPTY) {
                handleWin();
            }

            // reset ai timer
            aiTimer.restart();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        runAI();
    }

    private void runAI() {
        if (!(game.currentPlayer() instanceof HumanPlayer)) {
            game.runAITurn();
            updateScreen();
            updatePlayerText();

            if (game.checkWin() != EMPTY) {
                handleWin();
            }
        }
    }

    private void handleWin() {
        if (game.checkWin() == EMPTY) throw new RuntimeException("handleWin was called when no player has won yet.");
        aiTimer.stop();
        updateWinText();
        game.updateHistory();
    }

    private void updateScreen() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j].changeColor(game.getBoard()[i][j]);
            }
        }
    }

    private void updatePlayerText() {
        title.setLabel("Player " + (game.currentPlayerNum() + 1) + "'s Turn");
        switch (game.currentPlayerNum()) {
            case PLAYER_1 -> title.setColor(Color.YELLOW);
            case PLAYER_2 -> title.setColor(Color.RED);
        }
    }

    private void updateWinText() {
        title.setColor(Color.BLUE);
        if (game.checkWin() % 1 == 0.5) {
            title.setLabel("It's a tie!!");
        } else {
            switch ((int) game.checkWin()) {
                case PLAYER_1 -> title.setLabel("Player 1 Wins!");
                case PLAYER_2 -> title.setLabel("Player 2 Wins!");
            }
        }
    }

    private void add(Addable obj) {
        for (GObject component : obj.getComponents()) {
            add(component);
        }
    }

    // TODO: convert to menu
    private class PlayButton extends Button {

        public PlayButton(int x, int y) {
            super(x, y, "Play Game", Color.GREEN);
        }

        protected void buttonAction() {
            game = new ConnectGame(gameType.getPlayers());
            updateScreen();
            updatePlayerText();
//            runAILoop();
            aiTimer.start();
        }
    }

    private class DatabaseButton extends Button {

        public DatabaseButton(int x, int y) {
            super (x, y, "", Color.CYAN);
        }

        protected void buttonAction() {
            // TODO: should create a new game with live PercentBars
        }
    }
}