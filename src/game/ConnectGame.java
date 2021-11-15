package game;

import game.players.HumanPlayer;
import game.players.Player;

import java.util.*;

import static game.Constants.Game.*;

/**
 * A Connect 4 Game
 * Maintains Human and AI players
 */

public class ConnectGame {
    private static ConnectGame instance;
    protected Player[] players;
    private int[][] board; // -1 - empty, 0 - player1, 1 - player2
    private int currPlayer;
    private ArrayList<Move> currHistory;
    private Map<String, Double[]> fullHistory;

    /**
     * Creates a new ConnectGame object with the given players
     *
     * @param players contains the two players
     */
    public ConnectGame(Player[] players) {
        this.players = players;

        initBoard();
    }

    private void initBoard() {
        // initialize empty board
        board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }

        currHistory = new ArrayList<>();
    }

    /**
     * Getter method for board
     *
     * @return board
     */
    public int[][] getBoard() {
        return board;
    }

    public int currentPlayerNum() {
        return currPlayer;
    }

    public Player currentPlayer() {
        return players[currPlayer];
    }

    private void runTurn(int col) {
        currHistory.add(new Move(DatabaseIO.boardToDatabaseString(board), col));
        try {
            dropPiece(col);
            currPlayer = 1 - currPlayer;
        } catch (IllegalArgumentException e) {
            System.out.println("" + e);
        }
    }

    public void runAITurn() {
        if (players[currPlayer] instanceof HumanPlayer) {
            throw new RuntimeException("runAITurn was called when it is a Human's turn.");
        }
        int col = players[currPlayer].play(board, currPlayer);
        runTurn(col);
    }

    public void runHumanTurn(int col) {
        if (!(players[currPlayer] instanceof HumanPlayer)) {
            throw new RuntimeException("runHumanTurn was called when it is an AI's turn.");
        }
        runTurn(col);
    }


    /**
     * Drops a piece in the given column
     *
     * @param col the column to drop the piece in
     */
    public void dropPiece(int col) {
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

        DatabaseIO.writeHistory(fullHistory);
    }

    public double[][] getWinRates() {
        if (fullHistory == null) {
            fullHistory = DatabaseIO.readHistory();
        }

        double[][] winRates = new double[COLS][2];
        for (int i = 0; i < COLS; i++) {
            String key = DatabaseIO.boardToDatabaseString(board) + "-" + i;
            if (fullHistory.containsKey(key)) {
                double totalQ = fullHistory.get(key)[0];
                double count = fullHistory.get(key)[1];

                double winRate = (totalQ + count) / 2 / count;

                winRates[i][0] = winRate;
                winRates[i][1] = count;

            } else {
                winRates[i][0] = -1;
                winRates[i][1] = 0;
            }
        }

        return winRates;
    }

    private void addToHistory(String key, double value) {
        // TODO: some bug with moves not being added when AI plays
        if (fullHistory.containsKey(key)) {
            Double[] totalCount = fullHistory.get(key);
            totalCount[0] += value;
            totalCount[1]++;
            fullHistory.put(key, totalCount);
        } else {
            fullHistory.put(key, new Double[]{value, 1.0});
        }
    }

    private record Move(String state, int move) {
        public String toString() {
            return state + "-" + move;
        }
    }
}

