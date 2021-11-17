package game.util;

public class MoveHistory implements Comparable<MoveHistory> {
    private final String boardState;
    private int score, count;

    public MoveHistory(String boardState, int score, int count) {
        this.boardState = boardState;
        this.score = score;
        this.count = count;
    }

    @Override
    public int compareTo(MoveHistory o) {
        return this.boardState.compareTo(o.boardState);
    }

    public String getBoardState() {
        return boardState;
    }

    public double getScore() {
        return score;
    }

    public int getCount() {
        return count;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void incrementCount() {
        this.count++;
    }
}
