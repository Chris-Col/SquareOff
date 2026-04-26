package squareoff.engine;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.Piece;
import squareoff.model.PieceColor;

import java.util.ArrayList;
import java.util.List;

public class MiniMax {

    private static final int REGULAR_PIECE_VALUE = 1;
    private static final int KING_PIECE_VALUE = 3;

    private final int maxDepth;

    public MiniMax(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Move findBestMove(Board board, PieceColor color, List<Move> validMoves) {
        if (validMoves == null || validMoves.isEmpty()) {
            return null;
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (Move move : validMoves) {
            Board copy = board.copy();
            copy.executeMove(move);
            int score = minimax(copy, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, color);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean maximizing, PieceColor aiColor) {
        PieceColor currentColor = maximizing ? aiColor : aiColor.opposite();
        List<Move> moves = getAllMovesForColor(board, currentColor);
        if (depth == 0 || moves.isEmpty()) {
            return evaluate(board, aiColor);
        }
        return maximizing
                ? maximize(board, depth, alpha, beta, aiColor, moves)
                : minimize(board, depth, alpha, beta, aiColor, moves);
    }

    private int maximize(Board board, int depth, int alpha, int beta, PieceColor aiColor, List<Move> moves) {
        int maxEval = Integer.MIN_VALUE;
        for (Move move : moves) {
            Board copy = board.copy();
            copy.executeMove(move);
            int eval = minimax(copy, depth - 1, alpha, beta, false, aiColor);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);
            if (alpha >= beta) {
                break;
            }
        }
        return maxEval;
    }

    private int minimize(Board board, int depth, int alpha, int beta, PieceColor aiColor, List<Move> moves) {
        int minEval = Integer.MAX_VALUE;
        for (Move move : moves) {
            Board copy = board.copy();
            copy.executeMove(move);
            int eval = minimax(copy, depth - 1, alpha, beta, true, aiColor);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);
            if (beta <= alpha) {
                break;
            }
        }
        return minEval;
    }

    private List<Move> getAllMovesForColor(Board board, PieceColor color) {
        List<Move> allMoves = collectMoves(board, color);
        List<Move> captures = filterCaptures(allMoves);
        return captures.isEmpty() ? allMoves : captures;
    }

    private List<Move> collectMoves(Board board, PieceColor color) {
        List<Move> allMoves = new ArrayList<>();
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                if (board.hasPieceOfColor(r, c, color)) {
                    allMoves.addAll(board.getPiece(r, c).getValidMoves(board, r, c));
                }
            }
        }
        return allMoves;
    }

    private List<Move> filterCaptures(List<Move> moves) {
        List<Move> captures = new ArrayList<>();
        for (Move move : moves) {
            if (move.isCapture()) {
                captures.add(move);
            }
        }
        return captures;
    }

    int evaluate(Board board, PieceColor color) {
        int score = 0;
        PieceColor opponent = color.opposite();

        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                Piece piece = board.getPiece(r, c);
                if (piece != null) {
                    if (piece.getColor() == color) {
                        score += piece.isKing() ? KING_PIECE_VALUE : REGULAR_PIECE_VALUE;
                    } else if (piece.getColor() == opponent) {
                        score -= piece.isKing() ? KING_PIECE_VALUE : REGULAR_PIECE_VALUE;
                    }
                }
            }
        }

        return score;
    }
}
