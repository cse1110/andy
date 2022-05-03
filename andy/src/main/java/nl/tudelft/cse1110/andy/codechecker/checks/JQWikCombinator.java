package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks whether combinator is used.
 * It simply looks for a method call to a 'combine(...)' method
 *
 * Output:
 * - true if used at least once
 */
public class JQWikCombinator extends Check {

    private boolean combineIsUsed = false;

    @Override
    public boolean visit(MethodInvocation mi) {

        String methodName = mi.getName().toString();

        if("combine".equals(methodName)) {
            combineIsUsed = true;
        }

        return super.visit(mi);
    }

    @Override
    public boolean result() {
        return combineIsUsed;
    }

    public String toString() {
        return "Combinators.Combine(...) is used";
    }
}
