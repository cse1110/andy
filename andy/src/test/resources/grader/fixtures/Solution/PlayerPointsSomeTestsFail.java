package delft;

import org.junit.jupiter.api.Test;
import delft.PlayerPoints;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerPointsTest {

    private final PlayerPoints pp = new PlayerPoints();

    // error in test: 30*3 should be 30+50
    @Test
    void lessPoints() {
        assertEquals(30*3, pp.totalPoints(30, 5));
    }

    // error in test: 30*3 should be 30+50
    @Test
    void manyPointsButLittleLives() {
        assertEquals(300*3, pp.totalPoints(300, 1));
    }

    // error in test: 30+50 should be 30*3
    @Test
    void manyPointsAndManyLives() {
        assertEquals(500+30, pp.totalPoints(500, 10));
    }

    @Test
    void betweenLessAndManyLives() {
        assertEquals(500*3, pp.totalPoints(500, 3));
        assertEquals(500+30, pp.totalPoints(500, 2));
    }
}
