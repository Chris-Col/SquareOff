package squareoff.engine;

import squareoff.model.Board;
import squareoff.model.KingPiece;
import squareoff.model.Move;
import squareoff.model.PieceColor;
import squareoff.model.RegularPiece;

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

    @Test
    void testFindBestMoveReturnsNullForEmptyMoves() {
        Board board = createEmptyBoard();
        MiniMax minimax = new MiniMax(3);
        assertNull(minimax.findBestMove(board, PieceColor.RED, List.of()));
    }

    @Test
    void testFindBestMoveReturnsNullForNullMoves() {
        Board board = createEmptyBoard();
        MiniMax minimax = new MiniMax(3);
        assertNull(minimax.findBestMove(board, PieceColor.RED, null));
    }

    @Test
    void testEvaluateOnInitialBoardIsZero() {
        Board board = new Board();
        MiniMax minimax = new MiniMax(1);
        assertEquals(0, minimax.evaluate(board, PieceColor.RED),
                "Symmetric piece counts should evaluate to zero");
    }

    @Test
    void testEvaluateFavorsOwnPieces() {
        Board board = createEmptyBoard();
        board.getTile(3, 2).setPiece(new RegularPiece(PieceColor.RED));
        board.getTile(3, 4).setPiece(new RegularPiece(PieceColor.RED));
        board.getTile(4, 3).setPiece(new RegularPiece(PieceColor.BLACK));
        MiniMax minimax = new MiniMax(1);
        assertTrue(minimax.evaluate(board, PieceColor.RED) > 0,
                "Two friendly pieces vs one enemy should be positive for RED");
    }

    @Test
    void testEvaluateScoresKingsHigher() {
        Board board = createEmptyBoard();
        board.getTile(3, 2).setPiece(new KingPiece(PieceColor.RED));
        board.getTile(4, 3).setPiece(new RegularPiece(PieceColor.BLACK));
        MiniMax minimax = new MiniMax(1);
        assertTrue(minimax.evaluate(board, PieceColor.RED) >= 2,
                "A king vs regular piece should give at least +2 advantage");
    }

    @Test
    void testFullBoardSearchAtIncreasingDepths() {
        Board board = new Board();
        MoveValidator validator = new MoveValidator();
        List<Move> redMoves = validator.getAvailableMoves(board, PieceColor.RED);
        for (int depth = 1; depth <= 3; depth++) {
            MiniMax minimax = new MiniMax(depth);
            Move best = minimax.findBestMove(board, PieceColor.RED, redMoves);
            assertNotNull(best, "Depth " + depth + " should always return a move on the opening board");
            assertTrue(redMoves.contains(best));
        }
    }

    @Test
    void testFindBestMoveExploresMinimizingBranch() {
        // Multiple options for both sides forces minimax to recurse through
        // both maximizing and minimizing branches and use alpha-beta pruning.
        Board board = createEmptyBoard();
        board.getTile(2, 1).setPiece(new RegularPiece(PieceColor.RED));
        board.getTile(2, 3).setPiece(new RegularPiece(PieceColor.RED));
        board.getTile(5, 0).setPiece(new RegularPiece(PieceColor.BLACK));
        board.getTile(5, 2).setPiece(new RegularPiece(PieceColor.BLACK));

        MoveValidator validator = new MoveValidator();
        List<Move> redMoves = validator.getAvailableMoves(board, PieceColor.RED);

        MiniMax minimax = new MiniMax(4);
        Move best = minimax.findBestMove(board, PieceColor.RED, redMoves);

        assertNotNull(best, "Should pick a move with multiple options");
        assertTrue(redMoves.contains(best));
    }
}
