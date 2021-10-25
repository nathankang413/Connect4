package game;

import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.graphics.GLabel;
import acm.program.GraphicsProgram;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class Display extends GraphicsProgram implements MouseListener {
    private Position[][] positions;

    public Display() {
        addMouseListeners();
    }

    public void run() {

        // TODO: IN PROGRESS
        TextDisplay label = new TextDisplay("This is a label");
        label.setFont("Arial-bold-100");
        add(label);
        label.setLabel("New Labe");

        Column[] frame = new Column[COLS];
        for (int i = 0; i < COLS; i++) {
            frame[i] = new Column(i * SPACING, TEXT_MARGIN);
            add(frame[i]);
        }

        positions = new Position[ROWS][COLS];
        int[][] board = ConnectGame.getInstance().getBoard();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j] = new Position(j * SPACING + MARGIN, i * SPACING + MARGIN + TEXT_MARGIN, board[i][j]);
                add(positions[i][j]);
            }
        }
    }

    public void updateScreen() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j].changeColor(ConnectGame.getInstance().getBoard()[i][j]);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int col = (mouseEvent.getX() - MARGIN) / SPACING;
        ConnectGame.getInstance().move(col);
        updateScreen();
    }

    private class TextDisplay extends GLabel {

        public TextDisplay(String str) {
            super(str, TEXT_PADDING, TEXT_MARGIN-TEXT_PADDING);
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
            super(x, y, SPACING, ROWS * (SPACING));

            setFillColor(Color.BLUE);
            setFilled(true);
        }
    }
}