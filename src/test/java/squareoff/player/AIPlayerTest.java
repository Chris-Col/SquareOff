package squareoff.player;

import squareoff.model.*;
import squareoff.strategy.RandomStrategy;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AIPlayerTest {

    @Test
    void testStrategyInjection() {
        RandomStrategy strategy = new RandomStrategy();
        AIPlayer player = new AIPlayer(PieceColor.RED, strategy);

        Board board = new Board();
        List<Move> validMoves = List.of(
                new Move(2, 1, 3, 0, false),
                new Move(2, 1, 3, 2, false),
                new Move(2, 3, 3, 4, false)
        );

        Move chosen = player.chooseMove(board, validMoves);

        assertNotNull(chosen, "AIPlayer should return a move");
        assertTrue(validMoves.contains(chosen), "Chosen move should be one of the valid moves");
    }

    @Test
    void testGetColorReturnsConstructorColor() {
        AIPlayer player = new AIPlayer(PieceColor.BLACK, new RandomStrategy());
        assertEquals(PieceColor.BLACK, player.getColor());
    }
}
