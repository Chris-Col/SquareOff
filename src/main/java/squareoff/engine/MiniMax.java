package squareoff.engine;

import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.Piece;
import squareoff.model.PieceColor;

import java.util.ArrayList;
import java.util.List;

public class MiniMax {

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

        if (maximizing) {
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
        } else {
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
    }

    private List<Move> getAllMovesForColor(Board board, PieceColor color) {
        List<Move> allMoves = new ArrayList<>();
        boolean hasCapture = false;

        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                if (board.hasPieceOfColor(r, c, color)) {
                    Piece piece = board.getPiece(r, c);
                    List<Move> pieceMoves = piece.getValidMoves(board, r, c);
                    for (Move move : pieceMoves) {
                        if (move.isCapture()) {
                            hasCapture = true;
                        }
                        allMoves.add(move);
                    }
                }
            }
        }

        if (hasCapture) {
            List<Move> captures = new ArrayList<>();
            for (Move move : allMoves) {
                if (move.isCapture()) {
                    captures.add(move);
                }
            }
            return captures;
        }

        return allMoves;
    }

    int evaluate(Board board, PieceColor color) {
        int score = 0;
        PieceColor opponent = color.opposite();

        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                Piece piece = board.getPiece(r, c);
                if (piece != null) {
                    if (piece.getColor() == color) {
                        score += piece.isKing() ? 3 : 1;
                    } else if (piece.getColor() == opponent) {
                        score -= piece.isKing() ? 3 : 1;
                    }
                }
            }
        }

        return score;
    }
}
