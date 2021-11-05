package game;

import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import game.players.HumanPlayer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import game.util.Button;
import game.util.Slot;
import game.util.TextDisplay;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class ConnectDisplay extends GraphicsProgram implements MouseListener {
    private Column[] frame;
    private Slot[][] positions;
    private TextDisplay title;
    private ConnectGame game;
    private static ConnectDisplay instance;

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
        frame = new Column[COLS];
        for (int i = 0; i < COLS; i++) {
            frame[i] = new Column(i * SPACING, TEXT_MARGIN);
        }

        positions = new Slot[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j] = new Slot(j * SPACING + POS_MARGIN, i * SPACING + POS_MARGIN + TEXT_MARGIN, EMPTY);
            }
        }

        // buttons
        for (int i=0; i<=2; i++) {
            new PlayButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN + i*(BUTTON_HEIGHT+BUTTON_PADDING), 2-i);
        }

//        Button butt = new PlayButton(BOARD_WIDTH+BUTTON_PADDING, TEXT_MARGIN, 1);
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

            if (game.checkWin() != EMPTY)
                updateWinText();
                game.updateHistory();
        }
    }

    private void runAILoop() {
        while (game.checkWin() == EMPTY && !(game.getCurrPlayer() instanceof HumanPlayer)) {
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
        title.setLabel("Player " + (game.getCurrPlayerNum()+1) + "'s Turn");
        switch(game.getCurrPlayerNum()) {
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
        public PlayButton(int x, int y, String str) {
            super(x, y, str);
        }

        private int numPlayers;

        public PlayButton(int x, int y, int numPlayers) {
            super(x, y, numPlayers + " Players");

            this.numPlayers = numPlayers;
        }

        protected void buttonAction() {
            game = new ConnectGame(numPlayers, (int) (Math.random() + 0.5));
            updateScreen();
            updatePlayerText();
            runAILoop();
        }
    }

    private class Column extends GRect {
        public Column(int x, int y) {
            super(x, y, SPACING, ROWS * SPACING);

            setFillColor(Color.BLUE);
            setFilled(true);

            addMouseListeners(new MouseHighlighter(x, y, x+SPACING, y+ROWS*SPACING));
            add(this);
        }

        private class MouseHighlighter extends MouseMotionAdapter {
            private final int xLeft, xRight, yUp, yDown;

            public MouseHighlighter(int xLeft, int yUp, int xRight, int yDown) {
                super();

                this.xLeft = xLeft;
                this.xRight = xRight;
                this.yUp = yUp;
                this.yDown = yDown;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getX() > xLeft && e.getX() < xRight && e.getY() > yUp && e.getY() < yDown) {
                    setFillColor(new Color(51,153,255));
                } else {
                    setFillColor(Color.BLUE);
                }
            }
        }
    }
}