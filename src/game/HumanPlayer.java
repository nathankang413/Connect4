package game;

import java.util.Scanner;

/**
 * A Human Player which gets inputs from human users
 */
public class HumanPlayer implements Player {
    private final Scanner scanner;

    /**
     * Creates a new HumanPlayer object
     */
    public HumanPlayer() {
        scanner = new Scanner(System.in);
    }

    /**
     * Plays one turn of connect for
     *
     * @param board the game board
     * @return the column to drop the piece, zero-indexed
     */
    public int play(int[][] board, int playerNum) {
        int col = -1;
        boolean invalid = true;
        while (invalid) {
            try {
                System.out.println("Choose a column to drop your piece: ");
                col = scanner.nextInt();
                if (col > 0 && col <= Constants.COLS) {
                    invalid = false;
                }
            } catch (Exception e) {
                scanner.next();
            }
            if (invalid) System.out.println("Invalid input!");
        }
        return col - 1;
    }
}