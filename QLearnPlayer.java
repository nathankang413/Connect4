import java.io.*;
import java.util.*;

public class QLearnPlayer extends AIPlayer {

    private Scanner fileRead;
    private PrintWriter fileWrite;
    private int useQ;
    private ArrayList<State> states;

    public QLearnPlayer() throws IOException {
        fileRead = new Scanner(new File("qualities.txt"));
        fileWrite = new PrintWriter("qualities.txt");
        states = new ArrayList<State>();
    }

    public int play(int[][] board, int playerNum) {
        
        // some random check

        // if using a random value, generate random value
        if (true) {
            int move = (int) (Math.random() * Constants.COLS);
            states.add(new State(convertBoard(board), move));
            return move;
        }
        // if not, check best states
        else {}

        return -1;

    }

    public void update (int win) {
        System.out.println("running update");
        for (State state : states) {
            System.out.println(state + ":" + win);
            fileWrite.println(state + ":" + win); // TODO: DOESN'T WORK
        }
    }

    private String convertBoard (int[][] board) {
        StringBuilder str = new StringBuilder();
        for (int i=0; i<Constants.ROWS; i++) {
            for (int j=0; j<Constants.COLS; j++) {
                str.append(board[i][j]+1);

            }
        }
        return str.toString();
    }

    private class State {
        private String state;
        private int move;

        public State (String state, int move) {
            this.state = state;
            this.move = move;
        } 

        public String toString () {
            return state + "-" + move;
        }
    }

}
