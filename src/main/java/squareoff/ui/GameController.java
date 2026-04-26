package squareoff.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squareoff.engine.GameEngine;
import squareoff.engine.MoveValidator;
import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.Piece;
import squareoff.model.PieceColor;
import squareoff.observer.GameObserver;
import squareoff.player.HumanPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameController implements GameObserver {

    static Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final int TILE_SIZE_PX = 70;
    private static final int PIECE_RADIUS_PX = 28;
    private static final int KING_FONT_SIZE = 24;
    private static final int STATUS_PADDING = 8;
    private static final int STATUS_FONT_SIZE = 14;
    private static final Color LIGHT_TILE_COLOR = Color.web("#f0d9b5");
    private static final Color DARK_TILE_COLOR = Color.web("#b58863");
    private static final Color RED_PIECE_COLOR = Color.web("#c0392b");
    private static final Color BLACK_PIECE_COLOR = Color.web("#2c3e50");
    private static final Color SELECTED_HIGHLIGHT = Color.web("#f1c40f");
    private static final Color MOVE_HIGHLIGHT = Color.web("#27ae60");
    private static final Color MOVABLE_HINT = Color.web("#3498db");
    private static final double MOVABLE_HINT_STROKE_PX = 3.0;
    private static final String KING_GLYPH = "K";
    private static final String STATUS_YOUR_TURN = "Your turn - click a piece, then a green square";
    private static final String STATUS_FORCED_CAPTURE = "Forced capture - only pieces outlined in blue can move";
    private static final String STATUS_AI_THINKING = "AI is thinking...";
    private static final String STATUS_GAME_OVER = "Game over";

    private final BorderPane root;
    private final GridPane boardPane;
    private final Label statusLabel;
    private final StringProperty statusText;
    private final GameEngine engine;
    private final MoveValidator validator;
    private final HumanPlayer humanPlayer;
    private final PieceColor humanColor;

    private Integer selectedRow;
    private Integer selectedCol;
    private List<Move> currentLegalMoves;
    private boolean aiBusy;

    public GameController(GameEngine engine, HumanPlayer humanPlayer, PieceColor humanColor) {
        this.engine = engine;
        this.humanPlayer = humanPlayer;
        this.humanColor = humanColor;
        this.validator = new MoveValidator();
        this.boardPane = new GridPane();
        this.statusText = new SimpleStringProperty(STATUS_YOUR_TURN);
        this.statusLabel = buildStatusLabel();
        this.root = new BorderPane(boardPane);
        this.root.setBottom(statusLabel);
        this.currentLegalMoves = new ArrayList<>();
        engine.addObserver(this);
        renderBoard();
    }

    public BorderPane getRoot() {
        return root;
    }

    public GridPane getBoardPane() {
        return boardPane;
    }

    public void start() {
        if (engine.getGameState().getCurrentTurn() == humanColor) {
            beginHumanTurn();
        } else {
            triggerAiTurn();
        }
    }

    private Label buildStatusLabel() {
        Label label = new Label();
        label.textProperty().bind(statusText);
        label.setFont(Font.font(STATUS_FONT_SIZE));
        label.setPadding(new Insets(STATUS_PADDING));
        return label;
    }

    private void renderBoard() {
        boardPane.getChildren().clear();
        Board board = engine.getGameState().getBoard();
        int size = board.getSize();
        boolean flip = humanColor == PieceColor.RED;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int visualRow = flip ? size - 1 - r : r;
                int visualCol = flip ? size - 1 - c : c;
                boardPane.add(buildTile(board, r, c), visualCol, visualRow);
            }
        }
    }

    private StackPane buildTile(Board board, int row, int col) {
        StackPane tile = new StackPane();
        Rectangle bg = new Rectangle(TILE_SIZE_PX, TILE_SIZE_PX);
        bg.setFill(((row + col) % 2 == 0) ? LIGHT_TILE_COLOR : DARK_TILE_COLOR);
        applyHighlight(bg, row, col);
        tile.getChildren().add(bg);
        Piece piece = board.getPiece(row, col);
        if (piece != null) {
            tile.getChildren().add(buildPieceShape(piece, hasMoveFrom(row, col)));
        }
        tile.setAlignment(Pos.CENTER);
        tile.setOnMouseClicked(e -> handleTileClick(row, col));
        return tile;
    }

    private boolean hasMoveFrom(int row, int col) {
        if (engine.getGameState().getCurrentTurn() != humanColor) {
            return false;
        }
        for (Move m : currentLegalMoves) {
            if (m.fromRow() == row && m.fromCol() == col) {
                return true;
            }
        }
        return false;
    }

    private void applyHighlight(Rectangle bg, int row, int col) {
        if (selectedRow != null && selectedRow == row && selectedCol == col) {
            bg.setFill(SELECTED_HIGHLIGHT);
        } else if (isMoveDestination(row, col)) {
            bg.setFill(MOVE_HIGHLIGHT);
        }
    }

    private boolean isMoveDestination(int row, int col) {
        if (selectedRow == null) {
            return false;
        }
        for (Move m : currentLegalMoves) {
            if (m.fromRow() == selectedRow && m.fromCol() == selectedCol
                    && m.toRow() == row && m.toCol() == col) {
                return true;
            }
        }
        return false;
    }

    private StackPane buildPieceShape(Piece piece, boolean movableHint) {
        StackPane wrapper = new StackPane();
        Circle circle = new Circle(PIECE_RADIUS_PX);
        circle.setFill(piece.getColor() == PieceColor.RED ? RED_PIECE_COLOR : BLACK_PIECE_COLOR);
        circle.setStroke(movableHint ? MOVABLE_HINT : Color.WHITE);
        circle.setStrokeWidth(movableHint ? MOVABLE_HINT_STROKE_PX : 1.0);
        circle.setMouseTransparent(true);
        wrapper.getChildren().add(circle);
        if (piece.isKing()) {
            Text crown = new Text(KING_GLYPH);
            crown.setFill(Color.WHITE);
            crown.setFont(Font.font(KING_FONT_SIZE));
            crown.setMouseTransparent(true);
            wrapper.getChildren().add(crown);
        }
        wrapper.setMouseTransparent(true);
        return wrapper;
    }

    private void handleTileClick(int row, int col) {
        logger.info("Tile click ({},{}) - turn={} aiBusy={} selected={},{} legalMoves={}",
                row, col, engine.getGameState().getCurrentTurn(), aiBusy,
                selectedRow, selectedCol, currentLegalMoves.size());
        if (engine.isGameOver() || aiBusy) {
            return;
        }
        if (engine.getGameState().getCurrentTurn() != humanColor) {
            return;
        }
        if (selectedRow == null) {
            trySelectPiece(row, col);
            return;
        }
        Move chosen = findMove(row, col);
        if (chosen != null) {
            executeHumanMove(chosen);
            return;
        }
        if (canSwitchSelectionTo(row, col)) {
            clearSelection();
            trySelectPiece(row, col);
            return;
        }
        logger.info("Click ignored - keeping selection at ({},{})", selectedRow, selectedCol);
    }

    private boolean canSwitchSelectionTo(int row, int col) {
        Board board = engine.getGameState().getBoard();
        if (!board.hasPieceOfColor(row, col, humanColor)) {
            return false;
        }
        return currentLegalMoves.stream()
                .anyMatch(m -> m.fromRow() == row && m.fromCol() == col);
    }

    private void trySelectPiece(int row, int col) {
        Board board = engine.getGameState().getBoard();
        if (!board.hasPieceOfColor(row, col, humanColor)) {
            return;
        }
        boolean hasMove = currentLegalMoves.stream()
                .anyMatch(m -> m.fromRow() == row && m.fromCol() == col);
        if (!hasMove) {
            logger.info("Piece at ({},{}) has no legal moves this turn", row, col);
            return;
        }
        selectedRow = row;
        selectedCol = col;
        logger.info("Selected piece at ({},{})", row, col);
        renderBoard();
    }

    private Move findMove(int toRow, int toCol) {
        for (Move m : currentLegalMoves) {
            if (m.fromRow() == selectedRow && m.fromCol() == selectedCol
                    && m.toRow() == toRow && m.toCol() == toCol) {
                return m;
            }
        }
        return null;
    }

    private void executeHumanMove(Move move) {
        logger.info("Executing human move {}", move);
        humanPlayer.setSelectedMove(move);
        endHumanTurn();
        engine.playTurn();
        renderBoard();
        if (engine.isGameOver()) {
            statusText.set(STATUS_GAME_OVER);
            showGameOver();
            return;
        }
        triggerAiTurn();
    }

    private void clearSelection() {
        selectedRow = null;
        selectedCol = null;
    }

    private void endHumanTurn() {
        clearSelection();
        currentLegalMoves = new ArrayList<>();
    }

    private void beginHumanTurn() {
        Board board = engine.getGameState().getBoard();
        currentLegalMoves = validator.getAvailableMoves(board, humanColor);
        boolean forcedCapture = !currentLegalMoves.isEmpty()
                && currentLegalMoves.stream().allMatch(Move::isCapture);
        statusText.set(forcedCapture ? STATUS_FORCED_CAPTURE : STATUS_YOUR_TURN);
        logger.info("Human turn begins - {} legal moves available (forcedCapture={})",
                currentLegalMoves.size(), forcedCapture);
        renderBoard();
    }

    private void triggerAiTurn() {
        if (engine.isGameOver()) {
            statusText.set(STATUS_GAME_OVER);
            showGameOver();
            return;
        }
        aiBusy = true;
        statusText.set(STATUS_AI_THINKING);
        Task<Void> aiTask = new Task<>() {
            @Override
            protected Void call() {
                engine.playTurn();
                return null;
            }
        };
        aiTask.setOnSucceeded(e -> onAiTurnComplete());
        aiTask.setOnFailed(e -> handleAiFailure(aiTask.getException()));
        Thread t = new Thread(aiTask, "ai-turn");
        t.setDaemon(true);
        t.start();
    }

    private void handleAiFailure(Throwable cause) {
        logger.error("AI turn failed", cause);
        aiBusy = false;
        statusText.set("AI error: " + (cause == null ? "unknown" : cause.getMessage()));
    }

    private void onAiTurnComplete() {
        aiBusy = false;
        renderBoard();
        if (engine.isGameOver()) {
            statusText.set(STATUS_GAME_OVER);
            showGameOver();
            return;
        }
        beginHumanTurn();
    }

    private void showGameOver() {
        PieceColor winner = engine.getGameState().getWinner();
        String message = (winner == null) ? "Draw" : winner + " wins!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        alert.showAndWait();
    }

    @Override
    public void onMoveMade(Move move, PieceColor byColor) {
        Platform.runLater(this::renderBoard);
    }

    @Override
    public void onKingPromotion(int row, int col) {
        logger.info("King promotion at ({}, {})", row, col);
    }

    @Override
    public void onGameOver(PieceColor winner) {
        logger.info("Game over observed. Winner: {}", winner);
    }
}
