package game;

import game.players.AIPlayer;
import game.players.HumanPlayer;
import game.players.Player;

import static game.Constants.Game.*;

/**
 * A Singleton Connect 4 Game
 * Maintains Human and AI players
 */

public class ConnectGame {
    // TODO: make private (can create getter methods)
    private static ConnectGame instance;
    private final int[][] board; // -1 - empty, 0 - player1, 1 - player2
    protected Player[] players;
    private int currPlayer;

    public static void initialize(int numPlayers) {
        if (instance != null) throw new RuntimeException("ConnectGame has already been initialized.");
        instance = new ConnectGame(numPlayers);
    }

    public static ConnectGame getInstance() {
        return instance;
    }

    /**
     * Creates a new ConnectGame object with the given number of players
     * Remaining players are filled with AI
     *
     * @param numPlayers should be between 0 and 2, inclusive
     */
    private ConnectGame(int numPlayers) {
        // numPlayers: 0 - 2 AI, 1 - 1 Human/AI, 2 - 2 Human
        if (numPlayers > 2 || numPlayers < 0) {
            throw new IllegalArgumentException("numPlayers " + numPlayers + "is outside range 0-2");
        }

        // initialize correct number of Human/AI Players
        players = new Player[2];
        for (int i = 0; i < 2; i++) {
            if (i < numPlayers) {
                players[i] = new HumanPlayer();
            } else
                players[i] = new AIPlayer();
        }
        // initialize empty board
        board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }
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

    /**
     * Runs the main game loop
     *
     * @param startPlayer the starting player, either 0 or 1
     */
    public double playGame(int startPlayer) {
        return playGame(startPlayer, true);
    }

    public double playGame(int startPlayer, boolean showBoard) {
        // game loop
        currPlayer = startPlayer;
//        while (checkWin() < 0) {
//            if (showBoard) {
//                System.out.println();
//                System.out.println("Player " + (currPlayer + 1) + "'s Turn");
//                printBoard();
//            }
//            try {
//                dropPiece(players[currPlayer].play(board, currPlayer), currPlayer);
//            } catch (IllegalArgumentException e) {
//                if (showBoard)
//                    System.out.println("Player " + (currPlayer + 1) + " played an illegal move.");
//                return 1 - currPlayer;
//            }
//            currPlayer = 1 - currPlayer;
//        }
//
//        if (showBoard) {
//            System.out.println();
//            System.out.println("Player " + (checkWin() + 1) + " Wins!!");
//            printBoard();
//        }
//
        return checkWin();
    }

    /**
     * Getter method for board
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
                    case EMPTY:
                        System.out.print(" ");
                        break;
                    case PLAYER_1:
                        System.out.print("X");
                        break;
                    case PLAYER_2:
                        System.out.print("O");
                        break;
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

    public int[] move(int col) {
        int[] res = dropPiece(col, currPlayer);
        currPlayer = 1 - currPlayer;
        return res;
    }

    /**
     * Drops a piece in the given column
     *
     * @param col    the column to drop the piece in
     * @param player the player dropping the piece, either 0 or 1
     * @return the position at which the piece landed
     */
    private int[] dropPiece(int col, int player) {
        if (board[0][col] != EMPTY) {
            throw new IllegalArgumentException("Column " + (col + 1) + " is full.");
        }
        int i;
        for (i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == EMPTY) {
                board[i][col] = player;
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
    private double checkWin() {
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
}
