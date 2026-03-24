package squareoff.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegularPieceTest {

    @Test
    void testRegularPieceMovesForward() {
        Board board = new Board();

        // Clear the entire board
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                board.getTile(r, c).clear();
            }
        }

        // Place a RED RegularPiece at row 3, col 2 (dark square since 3+2=5 is odd)
        RegularPiece piece = new RegularPiece(PieceColor.RED);
        board.getTile(3, 2).setPiece(piece);

        List<Move> moves = piece.getValidMoves(board, 3, 2);
        assertFalse(moves.isEmpty(), "Should have valid moves");

        for (Move move : moves) {
            assertTrue(move.toRow() > move.fromRow(),
                    "RED pieces should move forward (increasing row)");
        }
    }
}
