/**
  A Connect 4 Game
  Maintains Human and AI players
*/
public class ConnectGame {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int WIN_COND = 4;

    // TODO: make private (can create getter methods)
    public int[][] board; // -1 - empty, 0 - player1, 1 - player2
    public Player[] players;

    /**
      Creates a new ConnectGame object with the given number of players
      Remaining players are filled with AI
      @param numPlayers should be between 0 and 2, inclusive
    */
    public ConnectGame(int numPlayers) {
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
                board[i][j] = -1;
            }
        }
    }

    /**
      Runs the main game loop
      @param startPlayer the starting player, either 0 or 1
    */
    public void playGame(int startPlayer) {

      // game loop
        int currPlayer = startPlayer;
        while (checkWin() < 0) {
            System.out.println();
            System.out.println("Player " + (currPlayer+1) + "'s Turn");
            printBoard();
            dropPiece(players[currPlayer].play(board, currPlayer), currPlayer);
            currPlayer = (currPlayer + 1) % 2;
        }

        System.out.println();
        System.out.println("Player " + (checkWin()+1) + " Wins!!");
        printBoard();
    }

    /**
     Displays the text version of the board
     */
    public void printBoard() {

        // iterate through rows
        for (int i = 0; i < ROWS; i++) {

            System.out.print("|");

            // iterate through cols
            for (int j = 0; j < COLS; j++) {

                // show the correct piece
                switch (board[i][j]) {
                    case -1:
                        System.out.print(" ");
                        break;
                    case 0:
                        System.out.print("X");
                        break;
                    case 1:
                        System.out.print("O");
                        break;
                }
                System.out.print("|");

            }
            System.out.println();
        }

        // column labels
        for (int i=0; i < COLS; i++) {
            System.out.print("-" + (i+1));
        }
        System.out.println("-");
    }

    /**
     Drops a piece in the given column
     @param col the column to drop the piece in
     @param player the player dropping the piece, either 0 or 1
     */
    private void dropPiece(int col, int player) {
        if (board[0][col] != -1) {
            throw new IllegalArgumentException("Column " + (col + 1) + " is full.");
        }
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == -1) {
                board[i][col] = player;
                break;
            }
        }
    }

    /**
     Checks the board state for a 4-in-a-row
     @return -1 for no win, 0 or 1 for which player wins
     */
    private int checkWin() {

        int[][] dirs = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {0, -1}};

        // brute force scanning right, right-down, down
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {

                if (board[i][j] > -1) {
                    int count = 1;

                    for (int[] dir : dirs) { // check in all directions
                        while (count<4 && board[i+count*dir[0]][j+count*dir[1]]==board[i][j]) {
                            count++;
                        }
                        if (count >= 4)
                            return board[i][j];
                    }

                }
            }
        }

        return -1;
    }
}