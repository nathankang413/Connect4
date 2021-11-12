package game;

import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import game.players.HumanPlayer;
import game.util.*;
import game.util.Button;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Timer;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class ConnectDisplay extends GraphicsProgram implements MouseListener, ActionListener {
    private static ConnectDisplay instance;
    private Column[] frame;
    private Slot[][] positions;
    private TextDisplay title;
    private PercentBar[] winRates;
    private ConnectGame game;
    private Timer timer;

    private ConnectDisplay() {
        addMouseListeners();
        timer = new Timer(DELAY, this);
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

        // buttons - TODO: convert to menu
        for (int i = 0; i <= 2; i++) {
            new PlayButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN + i * (BUTTON_HEIGHT + BUTTON_PADDING), 2 - i);
        }
        new DatabaseButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN + 3*(BUTTON_HEIGHT + BUTTON_PADDING));

        // win rate bars underneath the buttons
        for (int i=0; i<COLS; i++) {
            winRates[i] = new PercentBar(BOARD_WIDTH + BUTTON_PADDING,
                    TEXT_MARGIN + 4 * (BUTTON_HEIGHT + BUTTON_PADDING) + i * (PERCENT_BAR_HEIGHT + PERCENT_BAR_PADDING),
                    0.5); // TODO: simplify y-value, remove magic number
            add(winRates[i]); // TODO: should be toggled on and off
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game != null && game.checkWin() == EMPTY) {
            // drop the piece
            int col = -1;
            for (int j = 0; j < COLS; j++) {
                if (frame[j].contains(mouseEvent.getX(), mouseEvent.getY())) {
                    col = j;
                }
            }

            if (col == -1) return; // if click not on board
            game.runHumanTurn(col);
            // TODO: commented out b/c runAI also called handleWin - should be checked
//            if (game.checkWin() != EMPTY) {
//                handleWin();
//            }
            updatePlayerText();
            updateScreen();
            timer.restart();
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
        }
        if (game.checkWin() != EMPTY) {
            handleWin();
        }
    }

    private void handleWin() {
        if (game.checkWin() == EMPTY) throw new RuntimeException("handleWin was called when no player has won yet.");
        timer.stop();
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

    private class PlayButton extends Button {
        private final int numPlayers;

        public PlayButton(int x, int y, int numPlayers) {
            super(x, y, numPlayers + " Player" + (numPlayers != 1 ? "s" : ""), Color.GREEN);

            this.numPlayers = numPlayers;
        }

        protected void buttonAction() {
            game = new ConnectGame(numPlayers, (int) (Math.random() + 0.5));
            updateScreen();
            updatePlayerText();
//            runAILoop();
            timer.start();
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