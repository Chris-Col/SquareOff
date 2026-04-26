package squareoff.observer;

import org.junit.jupiter.api.Test;
import squareoff.model.Move;
import squareoff.model.PieceColor;

import static org.junit.jupiter.api.Assertions.*;

class GameLoggerTest {

    @Test
    void testOnMoveMadeRunsWithoutError() {
        GameLogger logger = new GameLogger();
        Move move = new Move(2, 1, 3, 0, false);
        assertDoesNotThrow(() -> logger.onMoveMade(move, PieceColor.RED));
    }

    @Test
    void testOnKingPromotionRunsWithoutError() {
        GameLogger logger = new GameLogger();
        assertDoesNotThrow(() -> logger.onKingPromotion(7, 0));
    }

    @Test
    void testOnGameOverWithWinner() {
        GameLogger logger = new GameLogger();
        assertDoesNotThrow(() -> logger.onGameOver(PieceColor.BLACK));
    }

    @Test
    void testOnGameOverWithDraw() {
        GameLogger logger = new GameLogger();
        assertDoesNotThrow(() -> logger.onGameOver(null));
    }
}
