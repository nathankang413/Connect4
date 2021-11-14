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
    private final Map<String, Double[]> movesMap;

    public QLearnPlayer(boolean onlyRand) {

        movesMap = new HashMap<>();
        readMovesMap();

        double numMapped = movesMap.size();
        if (onlyRand) {
            useRand = 1;
        } else {
            useRand = Math.exp(-numMapped / MAX_STATES);
        }
    }

    public QLearnPlayer() {
        this(false);
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

        // if using a random value, generate random value
        if (Math.random() < useRand) {
            return getRandomMove(board, playerNum);
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
