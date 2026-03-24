package squareoff.engine;

import squareoff.model.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MiniMaxTest {

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
    void testAISelectsCapture() {
        Board board = createEmptyBoard();
        MoveValidator validator = new MoveValidator();

        // Place a RED piece at (3, 2)
        board.getTile(3, 2).setPiece(new RegularPiece(PieceColor.RED));

        // Place a BLACK piece at (4, 3) — can be captured
        board.getTile(4, 3).setPiece(new RegularPiece(PieceColor.BLACK));

        List<Move> validMoves = validator.getAvailableMoves(board, PieceColor.RED);

        MiniMax minimax = new MiniMax(3);
        Move bestMove = minimax.findBestMove(board, PieceColor.RED, validMoves);

        assertNotNull(bestMove, "MiniMax should return a move");
        assertTrue(bestMove.isCapture(), "MiniMax should select the capture move");
    }
}
