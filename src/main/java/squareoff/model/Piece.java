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
        for (Direction dir : getMoveDirections()) {
            addStepMoveIfLegal(board, row, col, dir, moves);
            addCaptureMoveIfLegal(board, row, col, dir, moves);
        }
        return List.copyOf(moves);
    }

    private void addStepMoveIfLegal(Board board, int row, int col, Direction dir, List<Move> moves) {
        int newRow = dir.stepRow(row);
        int newCol = dir.stepCol(col);
        if (board.isInBounds(newRow, newCol) && board.isEmpty(newRow, newCol)) {
            moves.add(new Move(row, col, newRow, newCol, false));
        }
    }

    private void addCaptureMoveIfLegal(Board board, int row, int col, Direction dir, List<Move> moves) {
        int adjacentRow = dir.stepRow(row);
        int adjacentCol = dir.stepCol(col);
        int landingRow = dir.jumpRow(row);
        int landingCol = dir.jumpCol(col);
        if (board.isInBounds(landingRow, landingCol)
                && board.isEmpty(landingRow, landingCol)
                && board.isInBounds(adjacentRow, adjacentCol)
                && board.hasPieceOfColor(adjacentRow, adjacentCol, color.opposite())) {
            moves.add(new Move(row, col, landingRow, landingCol, true));
        }
    }

    protected abstract List<Direction> getMoveDirections();

    public abstract boolean isKing();

    @Override
    public String toString() {
        return getColor() + " " + getClass().getSimpleName();
    }
}
