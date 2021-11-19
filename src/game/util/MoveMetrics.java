package game.util;

/**
 * A record to store the count of games played from a certain move
 * and the total score of those games
 */
public class MoveMetrics {
    private int score, count;

    /**
     * Creates a new MoveMetrics class with the given score and count
     *
     * @param score the total score of the move
     * @param count the number of times the move has been played
     */
    public MoveMetrics(int score, int count) {
        this.score = score;
        this.count = count;
    }


    public int getScore() {
        return score;
    }

    public int getCount() {
        return count;
    }

    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Increases the play count by 1
     */
    public void incrementCount() {
        this.count++;
    }
}
