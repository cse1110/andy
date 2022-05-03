package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks for calls to Mockito's when(), for a specific method name
 * and a specific number of times.
 *
 * Parameters:
 * - The name of the method that when should happen (just the method name, e.g., "add")
 * - The number of expected times the when() should be called.
 *
 * Output: returns true if when() is called for the specific method, the specific
 * number of times.
 */
public class MockitoWhen extends Check {

    private final String methodToSetUp;
    private final int expectedNumberOfOccurrences;
    private final Comparison comparison;

    private int numberOfCallsToWhen = 0;
    private boolean inWhenMode = false;

    public MockitoWhen(String methodToSetUp, Comparison comparison, int expectedNumberOfOccurrences) {
        this.methodToSetUp = methodToSetUp;
        this.comparison = comparison;
        this.expectedNumberOfOccurrences = expectedNumberOfOccurrences;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        String methodName = mi.getName().toString();

        /**
         * As soon as a Mockito's when happen, we wait for the next method call.
         * We know that a when() method was just called because of the inWhenMode variable flag.
         *
         * If the method call just after when() matches the one we are expecting, bingo!
         */
        if(inWhenMode) {
            if(methodToSetUp.equals(methodName)) {
                numberOfCallsToWhen++;
            }

            // we turn off the 'when mode' right after the first method call after it.
            inWhenMode = false;
        } else {
            /**
             * We wait for a call to when().
             */
            boolean mockitoWhenCalled = "when".equals(methodName);
            if (mockitoWhenCalled) {
                inWhenMode = true;
            }
        }

        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return comparison.compare(numberOfCallsToWhen, expectedNumberOfOccurrences);
    }

    public String toString() {
        return methodToSetUp + " is stubbed" + (expectedNumberOfOccurrences > 0 ? " (" + comparison + " " + expectedNumberOfOccurrences + ")" : "");
    }
}
