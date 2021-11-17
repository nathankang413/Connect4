package game.util;

/**
 * TODO: DOCS
 */
public class MoveHistory implements Comparable<MoveHistory> { // TODO: implement usage
    private final String boardState;
    private int score, count;

    /**
     * TODO: docs
     *
     * @param boardState
     * @param score
     * @param count
     */
    public MoveHistory(String boardState, int score, int count) {
        this.boardState = boardState;
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
        return this.boardState.compareTo(o.boardState);
    }

    /**
     * @return
     */
    public String getBoardState() {
        return boardState;
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
