package game.util;

/**
 * TODO: DOCS
 */
public class MoveMetrics {
    private int score, count;

    /**
     * TODO: docs
     *
     * @param score
     * @param count
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
