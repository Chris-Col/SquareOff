package squareoff.strategy;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements MoveStrategy {

    private final Random random = new Random();

    @Override
    public Move selectMove(Board board, PieceColor color, List<Move> validMoves) {
        if (validMoves == null || validMoves.isEmpty()) {
            return null;
        }
        return validMoves.get(random.nextInt(validMoves.size()));
    }
}
