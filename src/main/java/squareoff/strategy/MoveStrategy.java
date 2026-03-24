package squareoff.strategy;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;

import java.util.List;

public interface MoveStrategy {
    Move selectMove(Board board, PieceColor color, List<Move> validMoves);
}
