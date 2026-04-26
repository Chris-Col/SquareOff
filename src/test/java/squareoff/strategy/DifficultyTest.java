package squareoff.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyTest {

    @Test
    void testEasyDepth() {
        assertEquals(2, Difficulty.EASY.getDepth(), "EASY depth should be 2");
    }

    @Test
    void testHardDepth() {
        assertEquals(5, Difficulty.HARD.getDepth(), "HARD depth should be 5");
    }

    @Test
    void testHardSearchesDeeperThanEasy() {
        assertTrue(Difficulty.HARD.getDepth() > Difficulty.EASY.getDepth(),
                "HARD should search deeper than EASY");
    }
}
