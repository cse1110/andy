package nl.tudelft.cse1110.andy.codechecker.checks;

/**
 * Counts the number of JQWik's @Property.
 *
 * Parameters:
 * - operator (LT, LTE, GT, GTE, EQ)
 * - the minimum number of expected properties
 *
 * Output: true if number of @Property in the file is >= passed value; false otherwise.
 */
public class JQWikProperty extends UsageOfAnAnnotationTemplate {

    public JQWikProperty(Comparison comparison, int minimumNumberOfUsage) {
        super(comparison, minimumNumberOfUsage);
    }

    @Override
    protected String annotationName() {
        return "Property";
    }

    public String toString() {
        return "Number of JQWik property(ies) is " + comparison.toString() + " " + minimumNumberOfUsage;
    }


}
