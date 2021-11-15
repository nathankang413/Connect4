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
    private final boolean onlyNew;
    private final Map<String, Double[]> movesMap;

    // TODO: move to constants?
    public static final int NORMAL = 0;
    public static final int NEW = 1;
    public static final int RANDOM = 2;

    /**
     *
     * @param logic 0 - normal, 1 - new moves, 2 - random moves
     */
    public QLearnPlayer(int logic) {

        movesMap = new HashMap<>();
        readMovesMap();

        double numMapped = movesMap.size();

        switch (logic) {
            case NORMAL -> {
                onlyNew = false;
                useRand = Math.exp(-numMapped / MAX_STATES);
            }
            case NEW -> {
                onlyNew = true;
                useRand = Math.exp(-numMapped / MAX_STATES);
            }
            case RANDOM -> {
                onlyNew = false;
                useRand = 1;
            }
            default -> throw new IllegalArgumentException("Invalid QLearn logic type: " + logic);
        }
    }

    public QLearnPlayer() {
        this(0);
    }

    public int play(int[][] board, int playerNum) {
        // if can win in one, play it
        for (int i = 0; i < board[0].length; i++) {
            if (checkDrop(board, playerNum, i) == 4) {
                return i;
            }
        }

        // if opponent can win in one, prevent it
        for (int i = 0; i < board[0].length; i++) {
            if (checkDrop(board, 1 - playerNum, i) == 4) {
                return i;
            }
        }

        // if using least-played move
        if (onlyNew) {
            ArrayList<Integer> leastPlayed = new ArrayList<>();
            double min = 999;
            // find least-played moves
            for (int i = 0; i < COLS; i++) {
                String stateMove = convertBoard(board) + "-" + i;
                if (movesMap.containsKey(stateMove)) {
                    int count = (int) (double) movesMap.get(stateMove)[1];
                    if (count < min) {
                        leastPlayed = new ArrayList<>();
                        leastPlayed.add(i);
                        min = count;
                    } else if (count == min) {
                        leastPlayed.add(i);
                    }
                } else {
                    if (min == 0) {
                        leastPlayed.add(i);
                    } else {
                        leastPlayed = new ArrayList<>();
                        leastPlayed.add(i);
                        min = 0;
                    }
                }
            }
            // weed out illegal moves
            for (int i=leastPlayed.size()-1; i>=0; i--) {
                if (checkDrop(board, playerNum, leastPlayed.get(i)) < 0) {
                    leastPlayed.remove(i);
                }
            }

            // get random move from the leastPlayed
            if (leastPlayed.size() <= 0) {
                return getRandomMove(board, playerNum);
            } else {
                return leastPlayed.get((int) (Math.random() * leastPlayed.size()));
            }
        }

        // if using a random value, generate random value
        else if (Math.random() < useRand) {
            return getRandomMove(board, playerNum);
        }

        // if not, check best states
        else {
            // System.out.println("Checking past experience");
            double bestQ = 0;
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
            if (bestQ > 0) {
                return bestMove;
            } else {
                return getRandomMove(board, playerNum);
            }
        }
    }

    private int getRandomMove(int[][] board, int playerNum) {
        int move;
        do { // generate randoms until get a legal move
            move = (int) (Math.random() * COLS);
        } while (checkDrop(board, playerNum, move) < 0);
        return move;
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
            System.out.println("" + e);
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
}
