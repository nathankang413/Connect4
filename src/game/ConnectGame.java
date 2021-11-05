package game;

import game.players.AIPlayer;
import game.players.HumanPlayer;
import game.players.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import static game.Constants.Game.*;
import static game.Constants.QLearn.QUALITIES_FILE;

/**
 * A Connect 4 Game
 * Maintains Human and AI players
 */

public class ConnectGame {
    private static ConnectGame instance;
    private final int[][] board; // -1 - empty, 0 - player1, 1 - player2
    protected Player[] players;
    private int currPlayer;
    private ArrayList<Move> currHistory;
    private Map<String, Double[]> fullHistory;

    /**
     * Creates a new ConnectGame object with the given number of players
     * Remaining players are filled with AI
     *
     * @param numHumans should be between 0 and 2, inclusive
     */
    public ConnectGame(int numHumans, int startPlayer) {
        // numHumans: 0 - 2 AI, 1 - 1 Human/AI, 2 - 2 Human
        if (numHumans > 2 || numHumans < 0) {
            throw new IllegalArgumentException("numHumans " + numHumans + "is outside range 0-2");
        }

        currPlayer = startPlayer;

        // initialize correct number of Human/AI Players
        players = new Player[2];
        for (int i = 0; i < 2; i++) {
            if (i < numHumans) {
                players[i] = new HumanPlayer();
            } else {
                players[i] = new AIPlayer();
            }
        }
        // initialize empty board
        board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }

        currHistory = new ArrayList<>();
    }

    protected ConnectGame(Player[] players) {
        this.players = players;

        // initialize empty board
        board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

//    /**
//     * Runs the main game loop
//     *
//     * @param startPlayer the starting player, either 0 or 1
//     */
//    public double playGame(int startPlayer) {
//        return playGame(startPlayer, true);
//    }
//
//    public double playGame(int startPlayer, boolean showBoard) {
//        // game loop
//        currPlayer = startPlayer;
////        while (checkWin() < 0) {
////            if (showBoard) {
////                System.out.println();
////                System.out.println("Player " + (currPlayer + 1) + "'s Turn");
////                printBoard();
////            }
////            try {
////                dropPiece(players[currPlayer].play(board, currPlayer), currPlayer);
////            } catch (IllegalArgumentException e) {
////                if (showBoard)
////                    System.out.println("Player " + (currPlayer + 1) + " played an illegal move.");
////                return 1 - currPlayer;
////            }
////            currPlayer = 1 - currPlayer;
////        }
////
////        if (showBoard) {
////            System.out.println();
////            System.out.println("Player " + (checkWin() + 1) + " Wins!!");
////            printBoard();
////        }
////
//        return checkWin();
//    }

    /**
     * Getter method for board
     *
     * @return board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Displays the text version of the board
     */
    public void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            System.out.print("|");
            for (int j = 0; j < COLS; j++) {
                // show the correct piece
                switch (board[i][j]) {
                    case EMPTY -> System.out.print(" ");
                    case PLAYER_1 -> System.out.print("X");
                    case PLAYER_2 -> System.out.print("O");
                }
                System.out.print("|");
            }
            System.out.println();
        }

        // column labels
        for (int i = 0; i < COLS; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");
    }

    public int getCurrPlayerNum() {
        return currPlayer;
    }

    public Player getCurrPlayer() {
        return players[currPlayer];
    }

//    /**
//     * Adds piece to board given human input
//     * @param col - the column to drop the next piece
//     */
//    public void move(int col) {
//        if (!(players[currPlayer] instanceof HumanPlayer)) {
//            throw new RuntimeException("Move cannot be called when it's the AI Player's turn.");
//        }
//        dropPiece(col, currPlayer);
//        currPlayer = 1 - currPlayer;
//        if (!(players[currPlayer] instanceof HumanPlayer)) {
//            dropPiece(players[currPlayer].play(board, currPlayer), currPlayer);
//            currPlayer = 1 - currPlayer;
//        }
//    }

    // TODO: condense AITurn and HumanTurn into 1 method?
    public void runAITurn() {
        if (players[currPlayer] instanceof HumanPlayer) {
            throw new RuntimeException("runAITurn was called when it is a Human's turn.");
        }
        int col = players[currPlayer].play(board, currPlayer);
        currHistory.add(new Move(boardToString(), col));
        dropPiece(col);
        currPlayer = 1 - currPlayer;
    }

    public void runHumanTurn(int col) {
        if (!(players[currPlayer] instanceof HumanPlayer)) {
            throw new RuntimeException("runHumanTurn was called when it is an AI's turn.");
        }
        currHistory.add(new Move(boardToString(), col));
        dropPiece(col);
        currPlayer = 1 - currPlayer;
    }

    /**
     * Drops a piece in the given column
     *
     * @param col the column to drop the piece in
     * @return the position at which the piece landed  -TODO: remove return?
     */
    public int[] dropPiece(int col) {
        if (board[0][col] != EMPTY) {
            throw new IllegalArgumentException("Column " + (col + 1) + " is full.");
        }
        int i;
        for (i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == EMPTY) {
                board[i][col] = currPlayer;
                break;
            }
        }
        return new int[]{i, col};
    }

    /**
     * Checks the board state for a 4-in-a-row
     *
     * @return -1 for no win, 0 or 1 for which player wins
     */
    public double checkWin() {
        for (int i = 0; i < COLS; i++) {
            if (board[0][i] < 0) break;
            else if (i >= COLS - 1) return 0.5;
        }

        int[][] dirs = {{0, 1}, {1, 1}, {1, 0}, {1, -1}};
        // brute force scanning every position
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // if position has a piece
                if (board[i][j] != EMPTY) {
                    // check in all directions
                    for (int[] dir : dirs) {
                        int count = 1;
                        while (true) {
                            int newPosX = i + count * dir[0];
                            int newPosY = j + count * dir[1];

                            // check out of bounds
                            if (newPosX < 0 || newPosX >= ROWS)
                                break;
                            if (newPosY < 0 || newPosY >= COLS)
                                break;

                            // check the correct piece
                            if (board[i][j] == board[newPosX][newPosY]) {
                                count++;
                            } else {
                                break;
                            }

                            if (count >= WIN_COND) {
                                return board[i][j];
                            }
                        }
                    }
                }
            }
        }

        return -1;
    }

    public void updateHistory() {
        readHistory();

        boolean tie = checkWin() == 0.5;
        boolean winner = true;

        // iterate backwards through currHistory, alternate winning and losing qualities
        // TODO: get rid of magic numbers
        for (int i = currHistory.size() - 1; i >= 0; i--) {
            if (tie) {
                addToHistory(currHistory.get(i).toString(), 0.0);
            } else if (winner) {
                addToHistory(currHistory.get(i).toString(), 1.0);
            } else {
                addToHistory(currHistory.get(i).toString(), -1.0);
            }
            winner = !winner;
        }

        writeHistory();
    }

    private void addToHistory(String key, Double value) {
        if (fullHistory.containsKey(key)) {
            Double[] totalCount = fullHistory.get(key);
            totalCount[0] += value;
            totalCount[1]++;
            fullHistory.put(key, totalCount);
        } else {
            fullHistory.put(key, new Double[]{1.0, value});
        }
    }

    private void readHistory() {
        fullHistory = new HashMap<>();

        try {
            Scanner fileRead = new Scanner(new File(QUALITIES_FILE));

            // Read file line by line - insert into movesMap
            while (fileRead.hasNextLine()) {
                String readLine = fileRead.nextLine();
                String[] splitString = readLine.split(":");
                String moveString = splitString[0];
                Double[] totalCount = {Double.parseDouble(splitString[1]), Double.parseDouble(splitString[2])};

                fullHistory.put(moveString, totalCount);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new IllegalArgumentException("Missing or invalid qualities.txt file");
        }
    }

    private void writeHistory() {
        try {
            PrintWriter fileWrite = new PrintWriter(QUALITIES_FILE);
            TreeMap<String, Double[]> sortedHistory = new TreeMap<>();
            for (Map.Entry<String, Double[]> entry : fullHistory.entrySet()) {
                sortedHistory.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Double[]> entry : sortedHistory.entrySet()) {
                Double[] totalCount = entry.getValue();
                fileWrite.println(entry.getKey() + ":" + totalCount[0] + ":" + totalCount[1]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private String boardToString() {
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

