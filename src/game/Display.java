package game;

import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

import java.awt.*;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class Display extends GraphicsProgram {
    private int[][] board; // TODO: use something else to get the actual board state in the game

    public static void main(String[] args) {
        new Display().start(args);
        System.out.println("here");

    }

    private Display() {
        board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = i % 2;
            }
        }
    }

    public void run() {
        Column[] frame = new Column[COLS];
        for (int i = 0; i < COLS; i++) {
            frame[i] = new Column(i * SPACING, 0);
            add(frame[i]);
        }

        Position[][] positions = new Position[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                positions[i][j] = new Position(j * SPACING + MARGIN, i * SPACING + MARGIN, board[i][j]);
                add(positions[i][j]);

            }
        }
    }

    private class Position extends GOval {
        public Position(int x, int y, int type) {
            super(x, y, DIAMETER, DIAMETER);

            if (type == EMPTY)
                setFillColor(Color.LIGHT_GRAY);
            else if (type == PLAYER_1)
                setFillColor(Color.YELLOW);
            else
                setFillColor(Color.RED);
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