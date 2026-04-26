package squareoff.strategy;

public enum Difficulty {
    EASY(2),
    HARD(5);

    private final int depth;

    Difficulty(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
