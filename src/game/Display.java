package game;

import java.awt.Color;
import acm.program.GraphicsProgram;
import acm.graphics.GRect;
import acm.graphics.GOval;

import static game.Constants.Game.*;
import static game.Constants.GUI.*;

public class Display extends GraphicsProgram {
    
    public static void main(String[] args) {
        new Display().start(args);

        System.out.println("here");


    }

    public void run() {

        Column[] frame = new Column[COLS];
        for (int i=0; i<COLS; i++) {
            frame[i] = new Column(i*SPACING, 0);
            add(frame[i]);
        }

        Position[][] positions = new Position[ROWS][COLS];
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {

                positions[i][j] = new Position(j*SPACING + MARGIN, i*SPACING + MARGIN);
                add(positions[i][j]);

            }
        }
    }
}

class Position extends GOval {

    public Position(int x, int y) {
        super(x, y, DIAMETER, DIAMETER);

        setFillColor(Color.LIGHT_GRAY);
        setFilled(true);
    }

}

class Column extends GRect {

    public Column (int x, int y) {
        super(x, y, SPACING, ROWS*(SPACING));

        setFillColor(Color.BLUE);
        setFilled(true);
    }

}