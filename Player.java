package Connect4;
import java.util.Scanner;

/**
  A Player interface for the ConnectGame class
 */
interface Player {
    public int play(int[][] board);
}

/**
  A Human Player which gets inputs from human users
*/
class HumanPlayer implements Player {

  private Scanner scanner;

  /**
    Creates a new HumanPlayer object
  */
  public HumanPlayer() {
    scanner = new Scanner(System.in);
  }

  /**
    Plays one turn of connect for
    @param board the game board 
    @return the column to drop the piece, zero-indexed
  */
  public int play(int[][] board) {
    int col;
    do {
      System.out.println("Choose a column to drop your piece :  ");
      col = scanner.nextInt();
    } while (col <= 0 || col > board[0].length);

    return col - 1;
  }

}


/**
  An AI Player which determines moves algorithmically
*/
class AIPlayer implements Player {

  /**
    Plays one turn of connect for
    @param board the game board 
    @return the column to drop the piece, zero-indexed
  */
  public int play(int[][] board) {
    // TODO: AI algorithm
    return 1;
  }

}