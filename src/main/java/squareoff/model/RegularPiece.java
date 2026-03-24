package squareoff.model;

public class RegularPiece extends Piece {

    public RegularPiece(PieceColor color) {
        super(color);
    }

    @Override
    protected int[][] getMoveDirections() {
        int dir = (getColor() == PieceColor.RED) ? 1 : -1;
        return new int[][] { { dir, -1 }, { dir, 1 } };
    }

    @Override
    public boolean isKing() {
        return false;
    }
}
