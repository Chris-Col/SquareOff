package squareoff.observer;

import squareoff.model.Move;
import squareoff.model.PieceColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLogger implements GameObserver {
    static Logger logger = LoggerFactory.getLogger(GameLogger.class);

    @Override
    public void onMoveMade(Move move, PieceColor byColor) {
        logger.info("{} moved from ({},{}) to ({},{})", byColor,
                move.fromRow(), move.fromCol(), move.toRow(), move.toCol());
    }

    @Override
    public void onKingPromotion(int row, int col) {
        logger.info("King promoted at ({}, {})", row, col);
    }

    @Override
    public void onGameOver(PieceColor winner) {
        if (winner == null) {
            logger.info("Game over! Draw");
        } else {
            logger.info("Game over! Winner: {}", winner);
        }
    }
}
