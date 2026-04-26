package squareoff.strategy;

import squareoff.engine.MiniMax;
import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;

import java.util.List;

public class MiniMaxStrategy implements MoveStrategy {

    private final MiniMax miniMax;
    private final Difficulty difficulty;

    public MiniMaxStrategy(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.miniMax = new MiniMax(difficulty.getDepth());
    }

    @Override
    public Move selectMove(Board board, PieceColor color, List<Move> validMoves) {
        if (validMoves == null || validMoves.isEmpty()) {
            return null;
        }
        return miniMax.findBestMove(board, color, validMoves);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
