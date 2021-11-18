package game.util;

/**
 * TODO: DOCS
 */
public class MoveHistory { // TODO: implement usage
    private int score, count;

    /**
     * TODO: docs
     *
     * @param score
     * @param count
     */
    public MoveHistory(int score, int count) {
        this.score = score;
        this.count = count;
    }

    /**
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * @param score
     */
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
