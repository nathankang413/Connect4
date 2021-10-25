package game.players;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.HashMap;

import static game.Constants.Game.*;
import static game.Constants.QLearn.*;

public class QLearnPlayer extends AIPlayer {
    private static final int MAX_STATES = 10000;
    private final double useRand;
    private final ArrayList<Move> gameMoves;
    private final Map<String, Double[]> movesMap;

    public QLearnPlayer() {
        gameMoves = new ArrayList<>();

        movesMap = new HashMap<>();
        readMovesMap();

        double numMapped = movesMap.size();
        useRand = Math.exp(-numMapped / MAX_STATES);
    }

    public int play(int[][] board, int playerNum) {
        // if can win in one, play it
        for (int i = 0; i < board[0].length; i++) {
            if (checkDrop(board, playerNum, i) == 4) {
                gameMoves.add(new Move(convertBoard(board), i));
                return i;
            }
        }

        // if opponent can win in one, prevent it
        for (int i = 0; i < board[0].length; i++) {
            if (checkDrop(board, 1 - playerNum, i) == 4) {
                gameMoves.add(new Move(convertBoard(board), i));
                return i;
            }
        }

        // if using a random value, generate random value
        if (Math.random() < useRand) {
            // System.out.println("Using random move");
            int move = (int) (Math.random() * COLS);
            gameMoves.add(new Move(convertBoard(board), move));
            return move;
        }
        // if not, check best states
        else {
            // System.out.println("Checking past experience");
            double bestQ = -1;
            int bestMove = -1;
            for (int i = 0; i < COLS; i++) {
                String stateMove = convertBoard(board) + "-" + i;
                if (movesMap.containsKey(stateMove)) {
                    double averageQ = movesMap.get(stateMove)[0] / movesMap.get(stateMove)[1];
                    if (averageQ > bestQ) {
                        bestMove = i;
                        bestQ = averageQ;
                    }
                }
            }
            if (bestMove >= 0) {
                // System.out.println("Using past experience");
                gameMoves.add(new Move(convertBoard(board), bestMove));
                return bestMove;
            } else {
                // System.out.println("Couldn't find past experience");
                int move = (int) (Math.random() * COLS);
                gameMoves.add(new Move(convertBoard(board), move));
                return move;
            }
        }
    }

    public void update(double win) throws IOException {
        readMovesMap();

        // loss = -1, tie = 0, win = 1
        win = win * 2 - 1;

        // for each current move
        // if key already exists, update value
        // else insert new key/value into treemap
        for (Move move : gameMoves) {
            if (movesMap.containsKey(move.toString())) {
                Double[] totalCount = movesMap.get(move.toString());
                totalCount[0] += win;
                totalCount[1]++;
                movesMap.put(move.toString(), totalCount);
            } else {
                movesMap.put(move.toString(), new Double[]{win, 1.0});
            }
        }

        TreeMap<String, Double[]> treeMap = new TreeMap<>();
        for (Map.Entry<String, Double[]> entry : movesMap.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        // write movesMap to file
        PrintWriter fileWrite = new PrintWriter(QUALITIES_FILE);
        for (Map.Entry<String, Double[]> entry : treeMap.entrySet()) {
            Double[] totalCount = entry.getValue();
            fileWrite.println(entry.getKey() + ":" + totalCount[0] + ":" + totalCount[1]);
        }
        fileWrite.close();
    }

    private void readMovesMap() {
        try {
            Scanner fileRead = new Scanner(new File(QUALITIES_FILE));

            // Read file line by line - insert into movesMap
            while (fileRead.hasNextLine()) {
                String readLine = fileRead.nextLine();
                String[] splitString = readLine.split(":");
                String moveString = splitString[0];
                Double[] totalCount = {Double.parseDouble(splitString[1]), Double.parseDouble(splitString[2])};

                movesMap.put(moveString, totalCount);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new IllegalArgumentException("Missing or invalid qualities.txt file");
        }
    }

    private String convertBoard(int[][] board) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                str.append(board[i][j] + 1);
            }
        }
        return str.toString();
    }

    private record Move(String state, int move) {
        public String toString() {
            return state + "-" + move;
        }
    }
}
