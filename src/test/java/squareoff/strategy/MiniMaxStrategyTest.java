package squareoff.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import squareoff.engine.MoveValidator;
import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;
import squareoff.model.RegularPiece;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MiniMaxStrategyTest {

    private Board emptyBoard;
    private MoveValidator validator;

    @BeforeEach
    void setUp() {
        emptyBoard = new Board();
        for (int r = 0; r < emptyBoard.getSize(); r++) {
            for (int c = 0; c < emptyBoard.getSize(); c++) {
                emptyBoard.getTile(r, c).clear();
            }
        }
        validator = new MoveValidator();
    }

    @Test
    void testReturnsNullForEmptyMoves() {
        MiniMaxStrategy strategy = new MiniMaxStrategy(Difficulty.EASY);
        assertNull(strategy.selectMove(emptyBoard, PieceColor.RED, List.of()));
    }

    @Test
    void testReturnsNullForNullMoves() {
        MiniMaxStrategy strategy = new MiniMaxStrategy(Difficulty.EASY);
        assertNull(strategy.selectMove(emptyBoard, PieceColor.RED, null));
    }

    @Test
    void testSelectsCaptureWhenAvailable() {
        emptyBoard.getTile(3, 2).setPiece(new RegularPiece(PieceColor.RED));
        emptyBoard.getTile(4, 3).setPiece(new RegularPiece(PieceColor.BLACK));

        List<Move> validMoves = validator.getAvailableMoves(emptyBoard, PieceColor.RED);
        MiniMaxStrategy strategy = new MiniMaxStrategy(Difficulty.HARD);
        Move chosen = strategy.selectMove(emptyBoard, PieceColor.RED, validMoves);

        assertNotNull(chosen, "Strategy should return a move");
        assertTrue(chosen.isCapture(), "Strategy should choose the capture move");
    }

    @Test
    void testGetDifficulty() {
        MiniMaxStrategy strategy = new MiniMaxStrategy(Difficulty.HARD);
        assertEquals(Difficulty.HARD, strategy.getDifficulty());
    }
}
