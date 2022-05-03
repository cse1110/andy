package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * Checks whether any loop (for, for each, while, do while)
 * happens in a test method
 *
 * Parameter:
 * (no parameters)
 *
 * Output:
 * - true if there exists at least one loop in any test method
 *
 */
public class LoopInTestMethods extends WithinTestMethod {

    private boolean loopFound = false;

    public boolean visit(EnhancedForStatement node) {
        if(isInTheAnnotatedMethod()) loopFound = true;
        return super.visit(node);
    }

    public boolean visit(ForStatement node) {
        if(isInTheAnnotatedMethod()) loopFound = true;
        return super.visit(node);
    }

    public boolean visit(WhileStatement node) {
        if(isInTheAnnotatedMethod()) loopFound = true;
        return super.visit(node);
    }

    public boolean visit(DoStatement node) {
        if(isInTheAnnotatedMethod()) loopFound = true;
        return super.visit(node);
    }

    @Override
    public boolean result() {
        return loopFound;
    }

    public String toString() {
        return "Loop is used in test methods";
    }
}
