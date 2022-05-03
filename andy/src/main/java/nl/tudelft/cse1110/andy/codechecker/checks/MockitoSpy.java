package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks whether the test class uses spies.
 *
 * Parameters: none
 *
 * Output: returns true if Mockito spies are used; false otherwise.
 */
public class MockitoSpy extends Check {

    private int numberOfSpies = 0;

    @Override
    public boolean visit(MethodInvocation mi) {

        /**
         * JDT visits the methods from the outside in
         * e.g., in verify(a).b(), JDT would first visit b(), and then a()
         *
         * Maybe another idea would be to parse mi.toString(), as it will return "verify(a).b()".
         * Not sure which one would work better, but I feel parsing strings is always trickier,
         * and full of cases we can't see...
         */
        String methodName = mi.getName().toString();

        boolean mockitoSpyCalled = "spy".equals(methodName);
        if (mockitoSpyCalled) {
            numberOfSpies++;
        }

        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return numberOfSpies > 0;
    }

    public String toString() {
        return "Spies are used";
    }
}
