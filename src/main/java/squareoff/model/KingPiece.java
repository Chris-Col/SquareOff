package squareoff.model;

public class KingPiece extends Piece {

    public KingPiece(PieceColor color) {
        super(color);
    }

    @Override
    protected int[][] getMoveDirections() {
        return new int[][] { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
    }

    @Override
    public boolean isKing() {
        return true;
    }
}
