package squareoff.model;

import java.util.List;

public class KingPiece extends Piece {

    private static final List<Direction> ALL_DIAGONALS = List.of(
            new Direction(-1, -1),
            new Direction(-1, 1),
            new Direction(1, -1),
            new Direction(1, 1)
    );

    public KingPiece(PieceColor color) {
        super(color);
    }

    @Override
    protected List<Direction> getMoveDirections() {
        return ALL_DIAGONALS;
    }

    @Override
    public boolean isKing() {
        return true;
    }
}
