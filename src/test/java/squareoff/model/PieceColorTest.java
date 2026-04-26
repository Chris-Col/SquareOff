package squareoff.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceColorTest {

    @Test
    void testRedOppositeIsBlack() {
        assertEquals(PieceColor.BLACK, PieceColor.RED.opposite());
    }

    @Test
    void testBlackOppositeIsRed() {
        assertEquals(PieceColor.RED, PieceColor.BLACK.opposite());
    }

    @Test
    void testOppositeIsInvolution() {
        assertEquals(PieceColor.RED, PieceColor.RED.opposite().opposite());
        assertEquals(PieceColor.BLACK, PieceColor.BLACK.opposite().opposite());
    }
}
