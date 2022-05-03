package nl.tudelft.cse1110.andy.codechecker.checks;

public interface Comparison {
    Comparison LT = ((actual, expected) -> actual<expected);
    Comparison LTE = ((actual, expected) -> actual<=expected);
    Comparison GT = ((actual, expected) -> actual>expected);
    Comparison GTE = ((actual, expected) -> actual>=expected);
    Comparison EQ = ((actual, expected) -> actual==expected);

    boolean compare(int actual, int expected);
}
