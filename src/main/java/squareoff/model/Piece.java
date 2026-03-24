package squareoff.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {

    private final PieceColor color;

    protected Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public final List<Move> getValidMoves(Board board, int row, int col) {
        List<Move> moves = new ArrayList<>();
        for (int[] dir : getMoveDirections()) {
            int dRow = dir[0];
            int dCol = dir[1];

            int newRow = row + dRow;
            int newCol = col + dCol;

            if (board.isInBounds(newRow, newCol) && board.isEmpty(newRow, newCol)) {
                moves.add(new Move(row, col, newRow, newCol, false));
            }

            int jumpRow = row + 2 * dRow;
            int jumpCol = col + 2 * dCol;

            if (board.isInBounds(jumpRow, jumpCol) && board.isEmpty(jumpRow, jumpCol)
                    && board.isInBounds(newRow, newCol)
                    && board.hasPieceOfColor(newRow, newCol, color.opposite())) {
                moves.add(new Move(row, col, jumpRow, jumpCol, true));
            }
        }
        return List.copyOf(moves);
    }

    protected abstract int[][] getMoveDirections();

    public abstract boolean isKing();

    @Override
    public String toString() {
        return getColor() + " " + getClass().getSimpleName();
    }
}
