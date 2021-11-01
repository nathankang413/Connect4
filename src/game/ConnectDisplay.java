package game;

import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.graphics.GLabel;
import acm.program.GraphicsProgram;
import game.players.HumanPlayer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class ConnectDisplay extends GraphicsProgram implements MouseListener {
    private Column[] frame;
    private Position[][] positions;
    private TextDisplay text;
    private ConnectGame game;

    public ConnectDisplay(ConnectGame game) {
        this.game = game;
        addMouseListeners();
    }

    /**
     * Initiates Connect4 display and game
     */
    @Override
    public void run() {

        text = new TextDisplay("", Color.RED);
        updatePlayerText();
        add(text);

        // set up frame and positions for display
        frame = new Column[COLS];
        for (int i = 0; i < COLS; i++) {
            frame[i] = new Column(i * SPACING, TEXT_MARGIN);
            add(frame[i]);
        }

        positions = new Position[ROWS][COLS];
        int[][] board = game.getBoard();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j] = new Position(j * SPACING + MARGIN, i * SPACING + MARGIN + TEXT_MARGIN, board[i][j]);
                add(positions[i][j]);
            }
        }

        // if game starts with AI, play all AI moves as necessary
        runAILoop();

        System.out.println("Completed initiation");
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game.checkWin() == EMPTY) {

            // drop the Piece
            int col = (mouseEvent.getX() - MARGIN) / SPACING;
            game.runHumanTurn(col);
            updatePlayerText();
            updateScreen();

            // if next player is AI
            runAILoop();

            if (game.checkWin() != EMPTY)
                updateWinText();
        }

        /*
        TODO: for some reason it only repaints after the entire mousePressed method finishes,
        TODO: so pauses from the AI occur without preceding changes in display
         */
    }

    private void runAILoop() {
        while (game.checkWin() == EMPTY && !(game.getCurrPlayer() instanceof HumanPlayer)) {
            game.runAITurn();
            pause(500);
            updateScreen();
            updatePlayerText();
        }

        if (game.checkWin() != EMPTY)
            updateWinText();
    }

    private void updateScreen() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j].changeColor(game.getBoard()[i][j]);
            }
        }
    }

    private void updatePlayerText() {
        text.setLabel("Player " + (game.getCurrPlayerNum()+1) + "'s Turn");
        switch(game.getCurrPlayerNum()) {
            case PLAYER_1 -> text.setColor(Color.YELLOW);
            case PLAYER_2 -> text.setColor(Color.RED);
        }
    }

    private void updateWinText() {
        text.setColor(Color.BLUE);
        if (game.checkWin() % 1 == 0.5) {
            text.setLabel("It's a tie!!");
        } else {
            switch ((int) game.checkWin()) {
                case PLAYER_1 -> text.setLabel("Player 1 Wins!");
                case PLAYER_2 -> text.setLabel("Player 2 Wins!");
            }
        }
    }

    private class TextDisplay extends GLabel {

        public TextDisplay(String str, Color color) {
            super(str, TEXT_PADDING, TEXT_MARGIN-TEXT_PADDING);
            setFont(FONT);
            setColor(color);
        }

    }

    private class Position extends GOval {
        public Position(int x, int y, int type) {
            super(x, y, DIAMETER, DIAMETER);

            changeColor(type);
        }

        public void changeColor(int type) {
            switch (type) {
                case EMPTY -> setFillColor(Color.LIGHT_GRAY);
                case PLAYER_1 -> setFillColor(Color.YELLOW);
                case PLAYER_2 -> setFillColor(Color.RED);
            }

            setFilled(true);
        }
    }

    private class Column extends GRect {
        public Column(int x, int y) {
            super(x, y, SPACING, ROWS * SPACING);

            setFillColor(Color.BLUE);
            setFilled(true);

            addMouseListeners(new MouseHighlighter(x, y, x+SPACING, y+ROWS*SPACING));
        }

        private class MouseHighlighter extends MouseMotionAdapter {

            private int xLeft, xRight, yUp, yDown;

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