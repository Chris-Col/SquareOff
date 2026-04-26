package squareoff.model;

public class Board {

    private static final int SIZE = 8;
    private static final int STARTING_PIECE_ROWS_PER_SIDE = 3;
    private static final int RED_FRONT_ROW = STARTING_PIECE_ROWS_PER_SIDE - 1;
    private static final int BLACK_FRONT_ROW = SIZE - STARTING_PIECE_ROWS_PER_SIDE;

    private final Tile[][] tiles;

    public Board() {
        tiles = new Tile[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boolean playable = (r + c) % 2 != 0;
                tiles[r][c] = new Tile(playable);
                if (playable) {
                    placeStartingPiece(r, c);
                }
            }
        }
    }

    private void placeStartingPiece(int r, int c) {
        if (r <= RED_FRONT_ROW) {
            tiles[r][c].setPiece(PieceFactory.createRegular(PieceColor.RED));
        } else if (r >= BLACK_FRONT_ROW) {
            tiles[r][c].setPiece(PieceFactory.createRegular(PieceColor.BLACK));
        }
    }

    Board(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public boolean isInBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public boolean isEmpty(int r, int c) {
        return isInBounds(r, c) && tiles[r][c].isPlayable() && tiles[r][c].isEmpty();
    }

    public boolean hasPieceOfColor(int r, int c, PieceColor color) {
        if (!isInBounds(r, c)) {
            return false;
        }
        Piece piece = tiles[r][c].getPiece();
        return piece != null && piece.getColor() == color;
    }

    public Piece getPiece(int r, int c) {
        if (!isInBounds(r, c)) {
            return null;
        }
        return tiles[r][c].getPiece();
    }

    public Tile getTile(int r, int c) {
        return tiles[r][c];
    }

    public void executeMove(Move move) {
        Piece piece = tiles[move.fromRow()][move.fromCol()].getPiece();
        tiles[move.fromRow()][move.fromCol()].clear();

        if (move.isCapture()) {
            tiles[move.capturedRow()][move.capturedCol()].clear();
        }

        if (!piece.isKing()) {
            if (piece.getColor() == PieceColor.RED && move.toRow() == SIZE - 1) {
                piece = PieceFactory.createKing(piece.getColor());
            } else if (piece.getColor() == PieceColor.BLACK && move.toRow() == 0) {
                piece = PieceFactory.createKing(piece.getColor());
            }
        }

        tiles[move.toRow()][move.toCol()].setPiece(piece);
    }

    public Board copy() {
        Tile[][] newTiles = new Tile[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boolean playable = tiles[r][c].isPlayable();
                newTiles[r][c] = new Tile(playable);
                Piece piece = tiles[r][c].getPiece();
                if (piece != null) {
                    Piece newPiece = piece.isKing()
                            ? PieceFactory.createKing(piece.getColor())
                            : PieceFactory.createRegular(piece.getColor());
                    newTiles[r][c].setPiece(newPiece);
                }
            }
        }
        return new Board(newTiles);
    }

    public int getSize() {
        return SIZE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece piece = tiles[r][c].getPiece();
                if (piece == null) {
                    sb.append(tiles[r][c].isPlayable() ? "." : " ");
                } else if (piece.getColor() == PieceColor.RED) {
                    sb.append(piece.isKing() ? "R" : "r");
                } else {
                    sb.append(piece.isKing() ? "B" : "b");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
