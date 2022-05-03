package nl.tudelft.cse1110.andy.codechecker.checks;

/**
 * Counts the number of JUnit tests in a file
 * More specifically, it counts the number of
 * '@Test' annotations.
 *
 * Parameters:
 * - operator (LT, LTE, GT, GTE, EQ)
 * - minimumNumberOfExpectedTests
 *
 * Output: true if number of @Test in the file is >= passed value; false otherwise.
 */
public class NumberOfTests extends UsageOfAnAnnotationTemplate {

    public NumberOfTests(Comparison comparison, int minimumNumberOfUsage) {
        super(comparison, minimumNumberOfUsage);
    }

    @Override
    protected String annotationName() {
        return "Test";
    }

    public String toString() {
        return "Number of tests " + comparison + " " + minimumNumberOfUsage;
    }


}
