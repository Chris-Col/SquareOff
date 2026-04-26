package squareoff.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testInitialSetup() {
        Board board = new Board();
        int redCount = 0;
        int blackCount = 0;

        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                Piece piece = board.getPiece(r, c);
                if (piece != null) {
                    assertTrue((r + c) % 2 != 0, "Pieces should only be on dark squares");
                    if (piece.getColor() == PieceColor.RED) {
                        assertTrue(r <= 2, "Red pieces should be in rows 0-2");
                        redCount++;
                    } else {
                        assertTrue(r >= 5, "Black pieces should be in rows 5-7");
                        blackCount++;
                    }
                }
            }
        }

        assertEquals(12, redCount, "Should have 12 red pieces");
        assertEquals(12, blackCount, "Should have 12 black pieces");
    }

    @Test
    void testKingPromotion() {
        Board board = new Board();

        // Clear the board area we need
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                board.getTile(r, c).clear();
            }
        }

        // Place a RED RegularPiece at row 6, col 1 (dark square)
        board.getTile(6, 1).setPiece(new RegularPiece(PieceColor.RED));

        // Move to row 7, col 0 (dark square) — should promote
        Move move = new Move(6, 1, 7, 0, false);
        board.executeMove(move);

        Piece promoted = board.getPiece(7, 0);
        assertNotNull(promoted, "Piece should exist at destination");
        assertTrue(promoted.isKing(), "Piece should be promoted to king");
        assertEquals(PieceColor.RED, promoted.getColor(), "Promoted piece should still be RED");
    }

    @Test
    void testIsInBoundsBoundaries() {
        Board board = new Board();
        assertTrue(board.isInBounds(0, 0));
        assertTrue(board.isInBounds(7, 7));
        assertFalse(board.isInBounds(-1, 0));
        assertFalse(board.isInBounds(0, -1));
        assertFalse(board.isInBounds(8, 0));
        assertFalse(board.isInBounds(0, 8));
    }

    @Test
    void testHasPieceOfColorOutOfBoundsIsFalse() {
        Board board = new Board();
        assertFalse(board.hasPieceOfColor(-1, 0, PieceColor.RED));
        assertFalse(board.hasPieceOfColor(0, 8, PieceColor.RED));
    }

    @Test
    void testGetPieceOutOfBoundsIsNull() {
        Board board = new Board();
        assertNull(board.getPiece(-1, -1));
        assertNull(board.getPiece(8, 0));
    }

    @Test
    void testCopyIsIndependent() {
        Board board = new Board();
        Board copy = board.copy();
        copy.getTile(2, 1).clear();
        assertNotNull(board.getPiece(2, 1), "Original should be unchanged after mutating copy");
        assertNull(copy.getPiece(2, 1), "Copy should reflect mutation");
    }

    @Test
    void testCopyPreservesKings() {
        Board board = new Board();
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                board.getTile(r, c).clear();
            }
        }
        board.getTile(3, 2).setPiece(new KingPiece(PieceColor.RED));
        Board copy = board.copy();
        Piece copied = copy.getPiece(3, 2);
        assertNotNull(copied);
        assertTrue(copied.isKing(), "Kings should remain kings in the copy");
    }

    @Test
    void testToStringIsNonEmpty() {
        Board board = new Board();
        String text = board.toString();
        assertNotNull(text);
        assertFalse(text.isEmpty());
        assertTrue(text.contains("r") || text.contains("b"),
                "toString should render piece glyphs");
    }

    @Test
    void testIsEmptyOutOfBoundsIsFalse() {
        Board board = new Board();
        assertFalse(board.isEmpty(-1, 0));
    }

    @Test
    void testGetSizeIsEight() {
        assertEquals(8, new Board().getSize());
    }
}
