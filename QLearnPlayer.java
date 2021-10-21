import java.io.*;
import java.util.*;

public class QLearnPlayer extends AIPlayer {

    private int useQ;
    private ArrayList<Move> moves;

    public QLearnPlayer() {
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

    public void update (double win) throws IOException {

        // Read file line by line
            // insert into tree map

        // for each current move
            // if key already exists, update value
            // else insert new key/value into treemap


        Scanner fileRead = new Scanner(new File("qualities.txt"));

        System.out.println("running update " + win);

        int currInd = 0;
        String gameMove = moves.get(currInd).toString();

        StringBuilder str = new StringBuilder();

        TreeMap<String, Double[]> map = new TreeMap<>();

        while (fileRead.hasNext()) {

            String readLine = fileRead.nextLine();
            int splitInd1 = readLine.indexOf(":");
            int splitInd2 = readLine.indexOf(":", splitInd1+1);
            String readStateMove = readLine.substring(0, splitInd1);

            Boolean updated = false;
            while (currInd < moves.size() && readStateMove.compareTo(gameMove) >= 0) {

                gameMove = moves.get(currInd).toString();

                if (readStateMove.equals(gameMove)) {
                    double total = Double.parseDouble(readLine.substring(splitInd1+1, splitInd2));
                    int count = Integer.parseInt(readLine.substring(splitInd2+1));

                    total += win;
                    count ++;
                    currInd++;
                    updated = true;

                    str.append(gameMove + ":" + total + ":" + count + "\n");

                } else {
                    str.append(gameMove + ":" + win + ":" + 1 + "\n");
                    currInd++;
                }
            }

            if (!updated) {
                str.append(readLine + "\n");
            }

        }
        
        for (int i=currInd; i<moves.size(); i++) {
            gameMove = moves.get(i).toString();
            str.append(gameMove + ":" + win + ":" + 1 + "\n");
        }

        PrintWriter fileWrite = new PrintWriter("qualities.txt");

        fileWrite.print(str.toString());
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
