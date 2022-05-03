package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;

import java.util.*;

/**
 * Checks for calls to Mockito's verify(), for a specific method name
 * and a specific number of times.
 *
 * Parameters:
 * - The name of the method to verify (just the method name, e.g., "add")
 * - The method type (TEST or AFTEREACH) to look for the verify() called.
 * - The comparator (LT, LTE, GT, GTE, EQ)
 * - The number of expected times the verify() should be called.
 *  * - (optional) A boolean indicating whether to expect a verify "never"
 *
 * Output: returns true if verify() is called for the specific method, the specific
 * number of times.
 */
public class MockitoVerify extends WithinAnnotatedMethod {

    private final String methodToVerify;
    private final int expectedNumberOfOccurrences;
    private final Comparison comparison;
    private final MethodType methodType;

    private final boolean never;

    private int numberOfCallsToVerify = 0;
    private String lastMethodCalled = null;

    public enum MethodType {
        TEST(WithinTestMethod.TEST_ANNOTATIONS),
        AFTEREACH(WithinAfterEach.AFTEREACH_ANNOTATION);

        private final Set<String> annotations;

        MethodType(Set<String> annotations) {
            this.annotations = annotations;
        }

        public Set<String> getAnnotations() {
            return annotations;
        }
    }

    private boolean waitingForNever;

    public MockitoVerify(String methodToVerify, MethodType methodType, Comparison comparison, int expectedNumberOfOccurrences, boolean never) {
        this.methodToVerify = methodToVerify;
        this.methodType = methodType;

        this.comparison = comparison;
        this.expectedNumberOfOccurrences = expectedNumberOfOccurrences;

        this.never = never;
    }

    public MockitoVerify(String methodToVerify, MethodType methodType, Comparison comparison, int expectedNumberOfOccurrences) {
        this(methodToVerify, methodType, comparison, expectedNumberOfOccurrences, false);
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        if(isInTheAnnotatedMethod()) {
            /**
             * JDT visits the methods from the outside in
             * e.g., in verify(a).b(), JDT would first visit b(), and then a().
             * In the case of verify(class, never()).method(), JDT will visit method(), then verify() and then never().
             *
             * Maybe another idea would be to parse mi.toString(), as it will return "verify(a).b()".
             * Not sure which one would work better, but I feel parsing strings is always trickier,
             * and full of cases we can't see...
             */
            String methodName = mi.getName().toString();

            boolean mockitoVerifyCalled = "verify".equals(methodName);


            /**
             * checks whether the verify is about the method
             * never being called.
             * This happens with either a never() or times(0) invocation.
             */
            boolean neverCalled = "never".equals(methodName);
            boolean timesZeroCalled = "times".equals(methodName) && zeroIsPassedToTheMethod(mi);
            boolean neverOrTimesZeroCalled = neverCalled || timesZeroCalled;

            boolean lastMethodCalledWasTheExpectedOne = methodToVerify.equals(lastMethodCalled);

            if (mockitoVerifyCalled && lastMethodCalledWasTheExpectedOne) {
                if(!never) {
                    numberOfCallsToVerify++;
                }
                else waitingForNever = true;
            } else if(neverOrTimesZeroCalled && waitingForNever) {
                numberOfCallsToVerify++;
                waitingForNever = false;
            } else {
                lastMethodCalled = methodName;
                waitingForNever = false;
            }
        }

        return super.visit(mi);
    }

    private boolean zeroIsPassedToTheMethod(MethodInvocation mi) {
        return mi.arguments() != null && mi.arguments().size() == 1 && mi.arguments().get(0) instanceof NumberLiteral && ((NumberLiteral) mi.arguments().get(0)).getToken().equals("0");
    }


    @Override
    public boolean result() {
        return comparison.compare(numberOfCallsToVerify, expectedNumberOfOccurrences);
    }

    @Override
    protected Set<String> annotations() {
        return methodType.getAnnotations();
    }

    public String toString() {
        return "Mockito verifies " + methodToVerify + " method " +
                (expectedNumberOfOccurrences > 0 ? "(" + comparison + " " + expectedNumberOfOccurrences + ")" : "") +
                (never ? "(never or zero interactions)" : "");
    }
}
