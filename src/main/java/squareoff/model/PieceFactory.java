package squareoff.model;

public class PieceFactory {

    public static Piece createRegular(PieceColor color) {
        return new RegularPiece(color);
    }

    public static Piece createKing(PieceColor color) {
        return new KingPiece(color);
    }
}
