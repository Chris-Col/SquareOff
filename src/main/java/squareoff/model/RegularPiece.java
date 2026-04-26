package squareoff.model;

import java.util.List;

public class RegularPiece extends Piece {

    private static final int RED_FORWARD_DELTA = 1;
    private static final int BLACK_FORWARD_DELTA = -1;
    private static final int LEFT_DELTA = -1;
    private static final int RIGHT_DELTA = 1;

    public RegularPiece(PieceColor color) {
        super(color);
    }

    @Override
    protected List<Direction> getMoveDirections() {
        int forward = (getColor() == PieceColor.RED) ? RED_FORWARD_DELTA : BLACK_FORWARD_DELTA;
        return List.of(
                new Direction(forward, LEFT_DELTA),
                new Direction(forward, RIGHT_DELTA)
        );
    }

    @Override
    public boolean isKing() {
        return false;
    }
}
