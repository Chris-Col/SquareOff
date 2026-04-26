package squareoff.player;

import org.junit.jupiter.api.Test;
import squareoff.model.Board;
import squareoff.model.Move;
import squareoff.model.PieceColor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {

    @Test
    void testGetColorReturnsConstructorColor() {
        HumanPlayer player = new HumanPlayer(PieceColor.BLACK);
        assertEquals(PieceColor.BLACK, player.getColor());
    }

    @Test
    void testChooseMoveReturnsLastSelectedMove() {
        HumanPlayer player = new HumanPlayer(PieceColor.RED);
        Move move = new Move(2, 1, 3, 0, false);
        player.setSelectedMove(move);
        assertEquals(move, player.chooseMove(new Board(), List.of(move)));
    }

    @Test
    void testChooseMoveBeforeSelectionIsNull() {
        HumanPlayer player = new HumanPlayer(PieceColor.RED);
        assertNull(player.chooseMove(new Board(), List.of()));
    }
}
