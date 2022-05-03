package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks whether the class was mocked via Mockito.mock(..).
 *
 * Parameters:
 * - The name of the class (e.g., "List")
 *
 * Output:
 * - true if the class was mocked at least once.
 */
public class MockClass extends Check {

    private final String classToBeMocked;
    private boolean classWasMocked = false;

    public MockClass(String classToBeMocked) {
        this.classToBeMocked = classToBeMocked;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        // we only keep looking at method invocations if we did not find the mock.
        if(!classWasMocked) {
            boolean mockMethodCalled = "mock".equals(mi.getName().toString());
            boolean paramsIsAClass = mi.arguments() != null && mi.arguments().size() == 1 && mi.arguments().get(0).toString().endsWith(".class");

            // we are almost certain this is a call to Mockito.
            if (mockMethodCalled && paramsIsAClass) {
                String className = mi.arguments().get(0).toString().replace(".class", "");
                classWasMocked = className.equals(classToBeMocked);
            }
        }

        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return classWasMocked;
    }

    public String toString() {
        return classToBeMocked + " is mocked";
    }
}
