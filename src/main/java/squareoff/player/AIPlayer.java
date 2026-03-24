package squareoff.player;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;
import squareoff.strategy.MoveStrategy;
import java.util.List;

public class AIPlayer implements Player {
    private final PieceColor color;
    private final MoveStrategy strategy;

    public AIPlayer(PieceColor color, MoveStrategy strategy) {
        this.color = color;
        this.strategy = strategy;
    }

    @Override
    public Move chooseMove(Board board, List<Move> validMoves) {
        return strategy.selectMove(board, color, validMoves);
    }

    @Override
    public PieceColor getColor() {
        return color;
    }
}
