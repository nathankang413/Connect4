package game.util;

/**
 * TODO: DOCS
 */
public class MoveHistory implements Comparable<MoveHistory> { // TODO: implement usage
    private final Move move;
    private int score, count;

    /**
     * TODO: docs
     *
     * @param move
     * @param score
     * @param count
     */
    public MoveHistory(Move move, int score, int count) {
        this.move = move;
        this.score = score;
        this.count = count;
    }

    /**
     * TODO: docs
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(MoveHistory o) {
        return move.toString().compareTo(o.getMove().toString());
    }

    /**
     * @return
     */
    public Move getMove() {
        return move;
    }

    /**
     * @return
     */
    public double getScore() {
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
