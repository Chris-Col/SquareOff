package squareoff.engine;

import squareoff.model.Board;
import squareoff.model.GameState;
import squareoff.model.Move;
import squareoff.model.Piece;
import squareoff.model.PieceColor;
import squareoff.observer.GameObserver;
import squareoff.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    static Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final GameState gameState;
    private final Player redPlayer;
    private final Player blackPlayer;
    private final MoveValidator moveValidator;
    private final List<GameObserver> observers;

    public GameEngine(Player redPlayer, Player blackPlayer) {
        this(redPlayer, blackPlayer, new Board(), new MoveValidator());
    }

    public GameEngine(Player redPlayer, Player blackPlayer, Board board, MoveValidator moveValidator) {
        this.redPlayer = redPlayer;
        this.blackPlayer = blackPlayer;
        this.gameState = new GameState(board);
        this.moveValidator = moveValidator;
        this.observers = new ArrayList<>();
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void playTurn() {
        if (gameState.isGameOver()) {
            return;
        }

        Board board = gameState.getBoard();
        PieceColor currentColor = gameState.getCurrentTurn();
        Player currentPlayer = getCurrentPlayer();

        List<Move> availableMoves = moveValidator.getAvailableMoves(board, currentColor);

        if (availableMoves.isEmpty()) {
            gameState.setGameOver(currentColor.opposite());
            logger.info("No moves available for {}", currentColor);
            for (GameObserver observer : observers) {
                observer.onGameOver(currentColor.opposite());
            }
            return;
        }

        Move move = currentPlayer.chooseMove(board, availableMoves);

        Piece pieceBefore = board.getPiece(move.fromRow(), move.fromCol());
        boolean wasKing = pieceBefore.isKing();

        board.executeMove(move);

        for (GameObserver observer : observers) {
            observer.onMoveMade(move, currentColor);
        }

        Piece pieceAfter = board.getPiece(move.toRow(), move.toCol());
        if (!wasKing && pieceAfter.isKing()) {
            for (GameObserver observer : observers) {
                observer.onKingPromotion(move.toRow(), move.toCol());
            }
        }

        PieceColor opponentColor = currentColor.opposite();
        List<Move> opponentMoves = moveValidator.getAvailableMoves(board, opponentColor);
        if (opponentMoves.isEmpty()) {
            gameState.setGameOver(currentColor);
            for (GameObserver observer : observers) {
                observer.onGameOver(currentColor);
            }
        }

        gameState.switchTurn();
    }

    public boolean isGameOver() {
        return gameState.isGameOver();
    }

    public GameState getGameState() {
        return gameState;
    }

    private Player getCurrentPlayer() {
        return gameState.getCurrentTurn() == PieceColor.RED ? redPlayer : blackPlayer;
    }
}
