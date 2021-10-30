package game;

import game.players.AIPlayer;
import game.players.HumanPlayer;
import game.players.Player;

import static game.Constants.Game.*;

/**
 * A Connect 4 Game
 * Maintains Human and AI players
 */

public class ConnectGame {
    private static ConnectGame instance;
    private final int[][] board; // -1 - empty, 0 - player1, 1 - player2
    protected Player[] players;
    private int currPlayer;


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

    public int getCurrPlayerNum() { return currPlayer; }

    public Player getCurrPlayer() { return players[currPlayer]; }

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

    public void runAITurn() {
        if (players[currPlayer] instanceof HumanPlayer) {
            throw new RuntimeException("runAITurn was called when it is a Human's turn.");
        }
        dropPiece(players[currPlayer].play(board, currPlayer));
        currPlayer = 1 - currPlayer;
    }

    public void runHumanTurn(int col) {
        if (!(players[currPlayer] instanceof HumanPlayer)) {
            throw new RuntimeException("runHumanTurn was called when it is an AI's turn.");
        }
        dropPiece(col);
        currPlayer = 1 - currPlayer;
    }

    /**
     * Drops a piece in the given column
     *
     * @param col    the column to drop the piece in
     * @return the position at which the piece landed
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
}
