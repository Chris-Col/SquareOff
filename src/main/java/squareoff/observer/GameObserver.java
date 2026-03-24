package squareoff.observer;

import squareoff.model.Move;
import squareoff.model.PieceColor;

public interface GameObserver {
    void onMoveMade(Move move, PieceColor byColor);
    void onKingPromotion(int row, int col);
    void onGameOver(PieceColor winner);
}
