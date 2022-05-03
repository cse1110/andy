package delft;

class Piecewise {

    /**
     * Checks whether the specified x and y coordinates form a
     * point that is in the domain meeting the following criteria:
     * - 1 < x <= 10
     * - 1 <= y <= 10
     * - y <= 14 - x
     *
     * @param x  x coordinate
     * @param y  y coordinate
     * @return true if point is in domain
     */
    public boolean isInRange(int x, int y) {
        return (x > 1 && x <= 10) &&
                (y >= 1 && y <= 10) &&
                (y <= (14 - x));
    }
}
