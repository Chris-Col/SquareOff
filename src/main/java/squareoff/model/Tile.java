package squareoff.model;

public class Tile {

    private Piece piece;
    private final boolean playable;

    public Tile(boolean playable) {
        this.playable = playable;
    }

    public boolean isPlayable() {
        return playable;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public void clear() {
        this.piece = null;
    }
}
