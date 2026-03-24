package squareoff.engine;

import squareoff.model.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveValidatorTest {

    private Board createEmptyBoard() {
        Board board = new Board();
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                board.getTile(r, c).clear();
            }
        }
        return board;
    }

    @Test
    void testForcedCapture() {
        Board board = createEmptyBoard();
        MoveValidator validator = new MoveValidator();

        // Place a RED piece at (3, 2) — dark square (3+2=5 odd)
        board.getTile(3, 2).setPiece(new RegularPiece(PieceColor.RED));

        // Place a BLACK piece at (4, 3) — dark square (4+3=7 odd)
        // RED can capture by jumping to (5, 4) — dark square (5+4=9 odd)
        board.getTile(4, 3).setPiece(new RegularPiece(PieceColor.BLACK));

        // RED can also move forward-left to (4, 1) — dark square (4+1=5 odd)
        // So RED has both a regular move and a capture available

        List<Move> available = validator.getAvailableMoves(board, PieceColor.RED);

        assertFalse(available.isEmpty(), "Should have available moves");
        for (Move move : available) {
            assertTrue(move.isCapture(), "When captures exist, only captures should be returned");
        }
    }

    @Test
    void testCaptureRemovesPiece() {
        Board board = createEmptyBoard();

        // Place a RED piece at (3, 2)
        board.getTile(3, 2).setPiece(new RegularPiece(PieceColor.RED));

        // Place a BLACK piece at (4, 3) to be captured
        board.getTile(4, 3).setPiece(new RegularPiece(PieceColor.BLACK));

        // Capture move: RED jumps from (3,2) over (4,3) to (5,4)
        Move capture = new Move(3, 2, 5, 4, true);
        board.executeMove(capture);

        assertNull(board.getPiece(4, 3), "Captured piece should be removed");
        assertNull(board.getPiece(3, 2), "Source square should be empty");
        assertNotNull(board.getPiece(5, 4), "Piece should be at destination");
    }
}
