package squareoff.player;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;
import java.util.List;

public class HumanPlayer implements Player {
    private final PieceColor color;
    private Move selectedMove;

    public HumanPlayer(PieceColor color) {
        this.color = color;
    }

    public void setSelectedMove(Move move) {
        this.selectedMove = move;
    }

    @Override
    public Move chooseMove(Board board, List<Move> validMoves) {
        return selectedMove;
    }

    @Override
    public PieceColor getColor() {
        return color;
    }
}
