package game;

import game.players.HumanPlayer;
import game.players.Player;
import game.util.Move;
import game.util.MoveMetrics;

import java.util.ArrayList;
import java.util.Map;

import static game.Constants.Game.*;

/**
 * A Connect4 Game
 * Maintains Human and AI players
 */
public class ConnectGame {
    protected Player[] players;
    private int[][] board;
    private int currPlayer;
    private ArrayList<Move> currHistory;
    private Map<Move, MoveMetrics> fullHistory;

    /**
     * Creates a new ConnectGame object with the given players
     *
     * @param players contains the two players
     */
    public ConnectGame(Player[] players) {
        this.players = players;

        initBoard();
    }

    /**
     * Creates an empty board
     */
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
     * @return the current board state
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * @return the current player number
     */
    public int currentPlayerNum() {
        return currPlayer;
    }

    /**
     * @return the current Player object
     */
    public Player getCurrentPlayer() {
        return players[currPlayer];
    }

    /**
     * @return the Player not currently playing
     */
    public Player getOtherPlayer() {
        return players[1-currPlayer];
    }

    /**
     * Drops a piece at the given column and increments the player
     *
     * @param col the column in which to drop the piece
     */
    private void runTurn(int col) {
        currHistory.add(new Move(board, col));
        try {
            dropPiece(col);
            currPlayer = 1 - currPlayer;
        } catch (IllegalArgumentException e) {
            System.out.println("" + e);
        }
    }

    /**
     * Gets the AI move and runs a turn
     */
    public void runAITurn() {
        if (players[currPlayer] instanceof HumanPlayer) {
            throw new RuntimeException("runAITurn was called when it is a Human's turn.");
        }
        int col = players[currPlayer].play(board, currPlayer);
        runTurn(col);
    }

    /**
     * Runs a turn with the given column
     *
     * @param col the column in which to drop the piece
     */
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
     * @return -1 for no win, 0 or 1 for which player wins, 0.5 for tie
     */
    public double checkWin() {  // TODO: Magic numbers?
        // check for tie
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

    /**
     * Saves the game to the qualities file
     */
    public void updateHistory() {
        boolean tie = checkWin() == 0.5;
        boolean winner = true;

        // iterate backwards through currHistory, alternate winning and losing qualities
        for (int i = currHistory.size() - 1; i >= 0; i--) {
            if (tie) {
                addToHistory(currHistory.get(i), TIE);
            } else if (winner) {
                addToHistory(currHistory.get(i), WIN);
            } else {
                addToHistory(currHistory.get(i), LOSS);
            }
            winner = !winner;
        }

        DatabaseIO.writeHistory(fullHistory);
    }

    /**
     * Calculates the win and play rates of each move in the current position
     *
     * @return an array of win rate and play count arrays
     */
    public double[][] getWinRates() {
        if (fullHistory == null) {
            fullHistory = DatabaseIO.readHistory();
        }

        double[][] winRates = new double[COLS][2];
        for (int i = 0; i < COLS; i++) {
            Move move = new Move(board, i);
            if (fullHistory.containsKey(move)) {
                double totalQ = fullHistory.get(move).getScore();
                double count = fullHistory.get(move).getCount();

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

    /**
     * Adds the given move and score to fullHistory
     *
     * @param move the move that has been played
     * @param score the resulting score of the move
     */
    private void addToHistory(Move move, int score) {
        if (fullHistory.containsKey(move)) {
            MoveMetrics moveMetrics = fullHistory.get(move);

            moveMetrics.addScore(score);
            moveMetrics.incrementCount();
        } else {
            fullHistory.put(move, new MoveMetrics(score, 1));
        }
    }
}

