import java.io.*;
import java.util.*;

public class QLearnPlayer extends AIPlayer {

    private Scanner fileRead;
    private PrintWriter fileWrite;
    private int useQ;
    private ArrayList<Move> moves;

    public QLearnPlayer() throws IOException {
        fileRead = new Scanner(new File("qualities.txt"));
        fileWrite = new PrintWriter("qualities.txt");
        moves = new ArrayList<Move>();
    }

    public int play(int[][] board, int playerNum) {
        
        // some random check

        // if using a random value, generate random value
        if (true) {
            int move = (int) (Math.random() * Constants.COLS);
            moves.add(new Move(convertBoard(board), move));
            return move;
        }
        // if not, check best states
        else {}

        return -1;

    }

    public void update (double win) { // TODO: REDO
        System.out.println("running update");

        int stateLen = Constants.ROWS * Constants.COLS;
        int currInd = 0;

        while (fileRead.hasNext()) {

            // get current Move in the list of game moves
            Move currMove = moves.get(currInd);

            // read move from file and break into components
            String readLine = fileRead.nextLine();
            String stateMove = readLine.substring(0, stateLen+2);

            int breakInd1 = readLine.indexOf(":");
            int breakInd2 = readLine.substring(breakInd1+1).indexOf(":") + breakInd1+1;

            double total = Double.parseDouble(readLine.substring(breakInd1+1, breakInd2));
            int count = Integer.parseInt(readLine.substring(breakInd2+1));

            System.out.println();
            System.out.println("Read state-move:   " + stateMove);
            System.out.println("Current game move: " + currMove);

            // if the read move is in the list of moves, update and write
            if (stateMove.equals(currMove.toString())) {
                
                System.out.println("State-move and game move match");

                total += win;
                count ++;
                currInd++;
                fileWrite.println(stateMove + ":" + total + ":" + count);

            } // if the read move comes after the next game move, add the game move
            else if (stateMove.compareTo(currMove.toString()) > 0) {

                while (stateMove.compareTo(currMove.toString()) > 0) {
                    System.out.println("game move was passed");

                    fileWrite.println(currMove + ":" + win + ":" + 1);
                    currMove = moves.get(++currInd);
                }

                fileWrite.println(readLine);

            } // if the read move was not played in the game, write it back and move on
            else {

                System.out.println("state-move and game move do not match");

                fileWrite.println(readLine);
            }
        }

        for (int i=currInd; i<moves.size(); i++) {
            Move currMove = moves.get(i);
            fileWrite.println(currMove + ":" + win + ":" + 1);
        }

        fileWrite.close();
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

    private class Move {
        private String state;
        private int move;

        public Move (String state, int move) {
            this.state = state;
            this.move = move;
        } 

        public String toString () {
            return state + "-" + move;
        }
    }

}
