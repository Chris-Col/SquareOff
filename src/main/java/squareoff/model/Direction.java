package squareoff.model;

public record Direction(int dRow, int dCol) {

    private static final int CAPTURE_JUMP_FACTOR = 2;

    public int stepRow(int row) {
        return row + dRow;
    }

    public int stepCol(int col) {
        return col + dCol;
    }

    public int jumpRow(int row) {
        return row + CAPTURE_JUMP_FACTOR * dRow;
    }

    public int jumpCol(int col) {
        return col + CAPTURE_JUMP_FACTOR * dCol;
    }
}
