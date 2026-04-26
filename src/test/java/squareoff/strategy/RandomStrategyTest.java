package squareoff.strategy;

import org.junit.jupiter.api.Test;
import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomStrategyTest {

    @Test
    void testReturnsNullForEmptyMoves() {
        RandomStrategy strategy = new RandomStrategy();
        assertNull(strategy.selectMove(new Board(), PieceColor.RED, List.of()));
    }

    @Test
    void testReturnsNullForNullMoves() {
        RandomStrategy strategy = new RandomStrategy();
        assertNull(strategy.selectMove(new Board(), PieceColor.RED, null));
    }

    @Test
    void testReturnsOneOfTheMoves() {
        RandomStrategy strategy = new RandomStrategy();
        List<Move> moves = List.of(
                new Move(2, 1, 3, 0, false),
                new Move(2, 1, 3, 2, false),
                new Move(2, 3, 3, 4, false)
        );
        Move chosen = strategy.selectMove(new Board(), PieceColor.RED, moves);
        assertTrue(moves.contains(chosen));
    }

    @Test
    void testSingleMoveAlwaysSelected() {
        RandomStrategy strategy = new RandomStrategy();
        Move only = new Move(2, 1, 3, 0, false);
        for (int i = 0; i < 5; i++) {
            assertEquals(only, strategy.selectMove(new Board(), PieceColor.RED, List.of(only)));
        }
    }
}
