package squareoff.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingPieceTest {

    @Test
    void testKingMovesAllDirections() {
        Board board = new Board();

        // Clear the entire board
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                board.getTile(r, c).clear();
            }
        }

        // Place a KingPiece at row 4, col 3 (dark square since 4+3=7 is odd)
        KingPiece king = new KingPiece(PieceColor.RED);
        board.getTile(4, 3).setPiece(king);

        List<Move> moves = king.getValidMoves(board, 4, 3);

        // King at (4,3) should be able to move to all 4 diagonal neighbors
        boolean hasUpLeft = false, hasUpRight = false, hasDownLeft = false, hasDownRight = false;

        for (Move move : moves) {
            int dRow = move.toRow() - move.fromRow();
            int dCol = move.toCol() - move.fromCol();
            if (dRow == -1 && dCol == -1) hasUpLeft = true;
            if (dRow == -1 && dCol == 1) hasUpRight = true;
            if (dRow == 1 && dCol == -1) hasDownLeft = true;
            if (dRow == 1 && dCol == 1) hasDownRight = true;
        }

        assertTrue(hasUpLeft, "King should move up-left");
        assertTrue(hasUpRight, "King should move up-right");
        assertTrue(hasDownLeft, "King should move down-left");
        assertTrue(hasDownRight, "King should move down-right");
    }
}
