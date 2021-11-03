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
    private TextDisplay title;
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

        title = new TextDisplay("", Color.RED);
        updatePlayerText();
//        add(title);

        // set up frame and positions for display
        frame = new Column[COLS];
        for (int i = 0; i < COLS; i++) {
            frame[i] = new Column(i * SPACING, TEXT_MARGIN);
//            add(frame[i]);
        }

        positions = new Position[ROWS][COLS];
        int[][] board = game.getBoard();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j] = new Position(j * SPACING + POS_MARGIN, i * SPACING + POS_MARGIN + TEXT_MARGIN, board[i][j]);
//                add(positions[i][j]);
            }
        }

        // buttons
        Button butt = new PlayButton(BOARD_WIDTH+BUTTON_PADDING, TEXT_MARGIN, "Play");
//        add(butt);

        // if game starts with AI, play all AI moves as necessary
        runAILoop();

        System.out.println("Completed initiation");
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game.checkWin() == EMPTY) {

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

    private abstract class Button extends GRect {

        GLabel buttonText;

        public Button (int x, int y, String str) {
            super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);

            setFillColor(Color.GREEN);
            setFilled(true);
            add(this);

            buttonText = new GLabel(str, x+ BUTTON_PADDING, y+BUTTON_HEIGHT- BUTTON_PADDING);
            buttonText.setFont(BUTTON_FONT);
            add(buttonText);

            addMouseListeners(new ButtonClick(x, x+BUTTON_WIDTH, y, y+BUTTON_HEIGHT));
        }

        protected abstract void buttonAction();

        private class ButtonClick extends MouseAdapter {
            int xLeft, xRight, yUp, yDown;

            public ButtonClick(int xLeft, int xRight, int yUp, int yDown) {
                this.xLeft = xLeft;
                this.xRight = xRight;
                this.yUp = yUp;
                this.yDown = yDown;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() > xLeft && e.getX() < xRight && e.getY() > yUp && e.getY() < yDown)
                    buttonAction();
            }
        }
    }

    private class PlayButton extends Button {

        public PlayButton(int x, int y, String str) {
            super(x, y, str);
        }

        protected void buttonAction() {
            // TODO
            System.out.println("yep");
        }

    }

    private class TextDisplay extends GLabel {

        public TextDisplay(String str, Color color) {
            super(str, TEXT_PADDING, TEXT_MARGIN-TEXT_PADDING);
            setFont(TITLE_FONT);
            setColor(color);
            add(this);
        }

    }

    private class Position extends GOval {
        public Position(int x, int y, int type) {
            super(x, y, POS_DIAMETER, POS_DIAMETER);

            changeColor(type);

            add(this);
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