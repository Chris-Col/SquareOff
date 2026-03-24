package squareoff.engine;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.Piece;
import squareoff.model.PieceColor;

import java.util.ArrayList;
import java.util.List;

public class MoveValidator {

    List<Move> getValidMoves(Board board, PieceColor color) {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                if (board.hasPieceOfColor(r, c, color)) {
                    Piece piece = board.getPiece(r, c);
                    moves.addAll(piece.getValidMoves(board, r, c));
                }
            }
        }
        return List.copyOf(moves);
    }

    List<Move> getCaptureMoves(Board board, PieceColor color) {
        List<Move> captures = new ArrayList<>();
        for (Move move : getValidMoves(board, color)) {
            if (move.isCapture()) {
                captures.add(move);
            }
        }
        return List.copyOf(captures);
    }

    public List<Move> getAvailableMoves(Board board, PieceColor color) {
        List<Move> captures = getCaptureMoves(board, color);
        if (!captures.isEmpty()) {
            return captures;
        }
        return getValidMoves(board, color);
    }
}
