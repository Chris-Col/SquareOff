package squareoff.player;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;
import java.util.List;

public interface Player {
    Move chooseMove(Board board, List<Move> validMoves);
    PieceColor getColor();
}
