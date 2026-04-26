package squareoff.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceFactoryTest {

    @Test
    void testCreateRegularReturnsNonKing() {
        Piece p = PieceFactory.createRegular(PieceColor.RED);
        assertNotNull(p);
        assertFalse(p.isKing());
        assertEquals(PieceColor.RED, p.getColor());
    }

    @Test
    void testCreateKingReturnsKing() {
        Piece p = PieceFactory.createKing(PieceColor.BLACK);
        assertNotNull(p);
        assertTrue(p.isKing());
        assertEquals(PieceColor.BLACK, p.getColor());
    }

    @Test
    void testFactoryProducesDistinctInstances() {
        Piece a = PieceFactory.createRegular(PieceColor.RED);
        Piece b = PieceFactory.createRegular(PieceColor.RED);
        assertNotSame(a, b, "Factory should hand out fresh instances");
    }

    @Test
    void testPieceToStringContainsColor() {
        Piece p = PieceFactory.createRegular(PieceColor.RED);
        assertTrue(p.toString().contains("RED"));
    }

    @Test
    void testFactoryCanBeInstantiated() {
        assertNotNull(new PieceFactory());
    }

    @Test
    void testCreateKingForBothColors() {
        assertEquals(PieceColor.RED, PieceFactory.createKing(PieceColor.RED).getColor());
        assertEquals(PieceColor.BLACK, PieceFactory.createKing(PieceColor.BLACK).getColor());
    }

    @Test
    void testCreateRegularForBothColors() {
        assertEquals(PieceColor.RED, PieceFactory.createRegular(PieceColor.RED).getColor());
        assertEquals(PieceColor.BLACK, PieceFactory.createRegular(PieceColor.BLACK).getColor());
    }
}
