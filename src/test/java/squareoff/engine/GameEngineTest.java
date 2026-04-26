package squareoff.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;
import squareoff.model.RegularPiece;
import squareoff.observer.GameObserver;
import squareoff.player.HumanPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private HumanPlayer red;
    private HumanPlayer black;

    @BeforeEach
    void setUp() {
        red = new HumanPlayer(PieceColor.RED);
        black = new HumanPlayer(PieceColor.BLACK);
    }

    private Board emptyBoard() {
        Board board = new Board();
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                board.getTile(r, c).clear();
            }
        }
        return board;
    }

    @Test
    void testPlayTurnSwitchesTurn() {
        GameEngine engine = new GameEngine(red, black);
        Move move = new Move(2, 1, 3, 0, false);
        red.setSelectedMove(move);
        engine.playTurn();
        assertEquals(PieceColor.BLACK, engine.getGameState().getCurrentTurn());
    }

    @Test
    void testPlayTurnNotifiesObserver() {
        GameEngine engine = new GameEngine(red, black);
        List<Move> recorded = new ArrayList<>();
        engine.addObserver(new GameObserver() {
            @Override
            public void onMoveMade(Move move, PieceColor by) { recorded.add(move); }
            @Override
            public void onKingPromotion(int row, int col) { }
            @Override
            public void onGameOver(PieceColor winner) { }
        });
        red.setSelectedMove(new Move(2, 1, 3, 0, false));
        engine.playTurn();
        assertEquals(1, recorded.size());
    }

    @Test
    void testRemoveObserverStopsNotifications() {
        GameEngine engine = new GameEngine(red, black);
        int[] counter = { 0 };
        GameObserver counting = new GameObserver() {
            @Override
            public void onMoveMade(Move move, PieceColor by) { counter[0]++; }
            @Override
            public void onKingPromotion(int row, int col) { }
            @Override
            public void onGameOver(PieceColor winner) { }
        };
        engine.addObserver(counting);
        engine.removeObserver(counting);
        red.setSelectedMove(new Move(2, 1, 3, 0, false));
        engine.playTurn();
        assertEquals(0, counter[0]);
    }

    @Test
    void testNoAvailableMovesEndsGame() {
        Board board = emptyBoard();
        board.getTile(7, 0).setPiece(new RegularPiece(PieceColor.RED));
        GameEngine engine = new GameEngine(red, black, board, new MoveValidator());
        engine.playTurn();
        assertTrue(engine.isGameOver());
        assertEquals(PieceColor.BLACK, engine.getGameState().getWinner());
    }

    @Test
    void testGameOverShortCircuitsPlayTurn() {
        Board board = emptyBoard();
        GameEngine engine = new GameEngine(red, black, board, new MoveValidator());
        engine.playTurn();
        PieceColor winner = engine.getGameState().getWinner();
        engine.playTurn();
        assertEquals(winner, engine.getGameState().getWinner());
    }
}
