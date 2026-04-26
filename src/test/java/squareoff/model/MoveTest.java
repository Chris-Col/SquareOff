package squareoff.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    void testCapturedRowIsMidpoint() {
        Move move = new Move(3, 2, 5, 4, true);
        assertEquals(4, move.capturedRow(), "Captured row should be midpoint");
    }

    @Test
    void testCapturedColIsMidpoint() {
        Move move = new Move(3, 2, 5, 4, true);
        assertEquals(3, move.capturedCol(), "Captured column should be midpoint");
    }

    @Test
    void testToStringContainsCoordinates() {
        Move move = new Move(0, 1, 2, 3, false);
        String text = move.toString();
        assertTrue(text.contains("0"));
        assertTrue(text.contains("3"));
    }

    @Test
    void testToStringMarksCapture() {
        Move capture = new Move(3, 2, 5, 4, true);
        assertTrue(capture.toString().contains("capture"),
                "Capture moves should be labelled in toString");
    }

    @Test
    void testToStringOmitsLabelForRegular() {
        Move regular = new Move(0, 1, 1, 2, false);
        assertFalse(regular.toString().contains("capture"));
    }

    @Test
    void testRecordAccessors() {
        Move move = new Move(1, 2, 3, 4, false);
        assertEquals(1, move.fromRow());
        assertEquals(2, move.fromCol());
        assertEquals(3, move.toRow());
        assertEquals(4, move.toCol());
        assertFalse(move.isCapture());
    }
}
