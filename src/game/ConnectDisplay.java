package game;

import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import game.players.HumanPlayer;
import game.util.*;
import game.util.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class ConnectDisplay extends GraphicsProgram implements MouseListener {
    private static ConnectDisplay instance;
    private Column[] frame;
    private Slot[][] positions;
    private TextDisplay title;
    private ConnectGame game;

    private ConnectDisplay() {
        addMouseListeners();
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

        // set up frame and positions for display
        for (int j = 0; j < COLS; j++) {
            new Column(j * SPACING, TEXT_MARGIN);
        }

        positions = new Slot[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j] = new Slot(j * SPACING + POS_MARGIN, i * SPACING + POS_MARGIN + TEXT_MARGIN, EMPTY);
            }
        }

        // buttons
        for (int i = 0; i <= 2; i++) {
            new PlayButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN + i * (BUTTON_HEIGHT + BUTTON_PADDING), 2 - i);
        }
        new DatabaseButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN + 3*(BUTTON_HEIGHT + BUTTON_PADDING));

        // TODO: instantiate percent bars to be added on DatabaseButton press

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game != null && game.checkWin() == EMPTY) {

            // drop the Piece
            int col = (mouseEvent.getX() - POS_MARGIN) / SPACING;
            if (col >= 0 && col < COLS)
                game.runHumanTurn(col);
            updatePlayerText();
            updateScreen();

            // if next player is AI
            runAILoop();

            if (game.checkWin() != EMPTY) {
                updateWinText();
                game.updateHistory();
            }
        }
    }

    private void runAILoop() {
        while (game.checkWin() == EMPTY && !(game.currentPlayer() instanceof HumanPlayer)) {
            game.runAITurn();
            pause(100);
            updateScreen();
            updatePlayerText();
        }

        if (game.checkWin() != EMPTY)
            updateWinText();

        /*
        TODO: for some reason it only repaints after the entire mousePressed method finishes,
        TODO: so pauses from the AI occur without preceding changes in display
        TODO: especially annoying for AI v AI games
         */
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

    private class PlayButton extends Button {
        private final int numPlayers;

        public PlayButton(int x, int y, int numPlayers) {
            super(x, y, numPlayers + " Players", Color.GREEN);

            this.numPlayers = numPlayers;
        }

        protected void buttonAction() {
            game = new ConnectGame(numPlayers, (int) (Math.random() + 0.5));
            updateScreen();
            updatePlayerText();
            runAILoop();
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