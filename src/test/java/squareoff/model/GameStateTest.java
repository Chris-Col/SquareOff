package squareoff.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState state;

    @BeforeEach
    void setUp() {
        state = new GameState(new Board());
    }

    @Test
    void testInitialTurnIsRed() {
        assertEquals(PieceColor.RED, state.getCurrentTurn());
    }

    @Test
    void testInitialGameNotOver() {
        assertFalse(state.isGameOver());
        assertNull(state.getWinner());
    }

    @Test
    void testSwitchTurnAlternates() {
        state.switchTurn();
        assertEquals(PieceColor.BLACK, state.getCurrentTurn());
        state.switchTurn();
        assertEquals(PieceColor.RED, state.getCurrentTurn());
    }

    @Test
    void testSetGameOverRecordsWinner() {
        state.setGameOver(PieceColor.RED);
        assertTrue(state.isGameOver());
        assertEquals(PieceColor.RED, state.getWinner());
    }

    @Test
    void testGetBoardReturnsConstructorBoard() {
        Board board = new Board();
        GameState s = new GameState(board);
        assertSame(board, s.getBoard());
    }

    @Test
    void testToStringContainsTurnAndStatus() {
        String s = state.toString();
        assertTrue(s.contains("RED"));
        assertTrue(s.contains("GameOver"));
    }
}
