package squareoff.ui;

import squareoff.model.PieceColor;
import squareoff.strategy.MoveStrategy;

public record GameSetup(PieceColor humanColor, MoveStrategy aiStrategy, String aiLabel) {
}
