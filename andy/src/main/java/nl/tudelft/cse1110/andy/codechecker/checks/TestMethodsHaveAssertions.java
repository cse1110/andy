package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.HashSet;
import java.util.Set;

/**
 * Check whether all test methods have at least one assertion.
 * A test method is a test annotated with @Test, @ParameterizedTest, or @Property.
 *
 * Note that if the student writes an auxiliary method for the assertion, this check
 * will fail. If you use it, double check the results.
 *
 * Also note that, for now, the list of assertion methods is unsound. While the common ones
 * are probably covered, we should add all possible assertions.
 *
 * Parameters: none
 *
 * Output: true if all test methods have at least one assertion.
 */
public class TestMethodsHaveAssertions extends WithinTestMethod {

    private boolean containsATestWithoutAssertion = false;
    private boolean currentMethodContainsAssertion = false;

    /**
     * This is, for now, a non-exhaustive list.
     */
    private static Set<String> ASSERT_METHODS = new HashSet<>() {{
        // junit
        add("assertEquals");
        add("assertNotEquals");
        add("assertTrue");
        add("assertFalse");
        add("assertThrows");

        // assertj
        add("assertThat");
        add("assertThatThrownBy");

    }};

    @Override
    public boolean visit(MethodDeclaration md) {
        // we reset the flag that stores whether we found a call to an assertion
        currentMethodContainsAssertion = false;

        return super.visit(md);
    }

    @Override
    public void endVisit(MethodDeclaration md) {
        /**
         * if we are in a test method, and at the end of the method,
         * we did not find any assertions, then, we have a problem.
         */
        if(isInTheAnnotatedMethod() && !currentMethodContainsAssertion) {
            this.containsATestWithoutAssertion = true;
        }

        super.endVisit(md);
    }

    @Override
    public boolean visit(MethodInvocation mi) {
        if(ASSERT_METHODS.contains(mi.getName().toString())) {
            currentMethodContainsAssertion = true;
        }

        return super.visit(mi);
    }

    @Override
    public boolean result() {
        // we negate the result!
        return !containsATestWithoutAssertion;
    }

    public String toString() {
        return "Tests have assertions";
    }


}
