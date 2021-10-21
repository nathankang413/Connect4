package game;

import java.io.*;
import java.util.*;

public class QLearnPlayer extends AIPlayer {

    private double useRand;
    private ArrayList<Move> gameMoves;
    private TreeMap<String, Double[]> movesMap;
    private static final int MAX_STATES = 5000;

    public QLearnPlayer() throws IOException {
        gameMoves = new ArrayList<>();

        movesMap = new TreeMap<>();
        readFile();

        double numMapped = movesMap.size();
        useRand = Math.exp(- numMapped / MAX_STATES);
    }

    public int play(int[][] board, int playerNum) {
        
        // if using a random value, generate random value
        if (Math.random() < useRand) {
            System.out.println("Using random move");
            int move = (int) (Math.random() * Constants.COLS);
            gameMoves.add(new Move(convertBoard(board), move));
            return move;
        }
        // if not, check best states
        else {
            System.out.println("Checking past experience");
            double bestQ = 0;
            int bestMove = -1;
            for (int i=0; i<Constants.COLS; i++) {
                String stateMove = convertBoard(board)+"-"+i;
                if (movesMap.containsKey(stateMove)) {
                    double averageQ = movesMap.get(stateMove)[0] / movesMap.get(stateMove)[1];
                    if (averageQ > bestQ) {
                        bestMove = i;
                        bestQ = averageQ;
                    }
                }
            }
            if (bestMove >= 0) {
                System.out.println("Using past experience");
                gameMoves.add(new Move(convertBoard(board), bestMove));
                return bestMove;
            }
            else {
                System.out.println("Couldn't find past experience");
                int move = (int) (Math.random() * Constants.COLS);
                gameMoves.add(new Move(convertBoard(board), move));
                return move; 
            }
        }
    }

    public void update (double win) throws IOException {

        readFile();

        // for each current move
            // if key already exists, update value
            // else insert new key/value into treemap
        for (Move move : gameMoves) {
            if (movesMap.containsKey(move.toString())) {
                Double[] totalCount = movesMap.get(move.toString());
                totalCount[0] += win;
                totalCount[1] ++;
                movesMap.put(move.toString(), totalCount);
            } else {
                movesMap.put(move.toString(), new Double[] {win, 1.0});
            }
        }

        // write Treemap to file
        PrintWriter fileWrite = new PrintWriter("game/qualities.txt");
        for (String key : movesMap.keySet()) {
            Double[] totalCount = movesMap.get(key);
            fileWrite.println(key + ":" + totalCount[0] + ":" + totalCount[1]);
        }
        fileWrite.close();

    }

    private void readFile() throws IOException {

        Scanner fileRead = new Scanner(new File("game/qualities.txt"));

        // Read file line by line - insert into tree map
        while (fileRead.hasNextLine()) {
            String readLine = fileRead.nextLine();
            String[] splitString = readLine.split(":");
            String moveString = splitString[0];
            Double[] totalCount = {Double.parseDouble(splitString[1]), Double.parseDouble(splitString[2])};

            movesMap.put(moveString, totalCount);
        }
    }

    private String convertBoard (int[][] board) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < Constants.ROWS; i++) {
            for (int j = 0; j < Constants.COLS; j++) {
                str.append(board[i][j] + 1);

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
