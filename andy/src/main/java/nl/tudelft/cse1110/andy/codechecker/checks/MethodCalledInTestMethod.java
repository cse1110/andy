package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks whether a method was invoked in a test method.
 *
 * Parameter:
 * - the name of the method
 *
 * Output:
 * - true if the method was invoked at least once in any test method.
 *
 */
public class MethodCalledInTestMethod extends WithinTestMethod {

    private final String methodToBeCalled;
    private boolean methodWasCalled = false;

    public MethodCalledInTestMethod(String methodToBeCalled) {
        this.methodToBeCalled = methodToBeCalled;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        if(isInTheAnnotatedMethod()) {
            String methodName = mi.getName().toString();

            if (methodToBeCalled.equals(methodName))
                methodWasCalled = true;
        }

        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return methodWasCalled;
    }

    public String toString() {
        return methodToBeCalled + " method is called";
    }
}
