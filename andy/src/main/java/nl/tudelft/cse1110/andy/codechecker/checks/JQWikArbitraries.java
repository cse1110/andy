package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.Set;

/**
 * Checks whether a specific Arbitraries.method() is used.
 *
 * Parameters:
 * - the Arbitrary to look for.
 *
 * Output:
 * - true if used at least once
 */
public class JQWikArbitraries extends WithinAnnotatedMethod {

    private final String arbitraryMethodToLookFor;
    private boolean arbitrariesIsUsed = false;

    public JQWikArbitraries(String arbitraryMethodToLookFor) {
        this.arbitraryMethodToLookFor = arbitraryMethodToLookFor;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        String methodName = mi.getName().toString();

        if(arbitraryMethodToLookFor.equals(methodName)) {
            arbitrariesIsUsed = true;
        }

        return super.visit(mi);
    }

    @Override
    public boolean result() {
        return arbitrariesIsUsed;
    }

    @Override
    protected Set<String> annotations() {
        return Set.of("Provide");
    }

    public String toString() {
        return "JQWikArbitrary " + arbitraryMethodToLookFor + " is used";
    }
}
