package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Checks whether a specific Arbitrary is defined.
 * Looks for method returns of Arbitrary<X>
 *
 * Parameters:
 * - the type to look for. Empty means any type.
 *
 * Output:
 * - true if a method returns it at least once
 */
public class JQWikArbitrary extends Check {

    private final String type;
    private boolean arbitraryIsReturned = false;

    public JQWikArbitrary(String type) {
        this.type = type;
    }

    public JQWikArbitrary() {
        this.type = null;
    }

    @Override
    public boolean visit(MethodDeclaration mi) {

        boolean specificType = type != null;

        if(mi.getReturnType2()!=null) {
            String returnType = mi.getReturnType2().toString();

            if(specificType && String.format("Arbitrary<%s>", type).equals(returnType)) {
                arbitraryIsReturned = true;
            } else if (!specificType && returnType.startsWith("Arbitrary<")){
                arbitraryIsReturned = true;
            }
        }

        return super.visit(mi);
    }

    @Override
    public boolean result() {
        return arbitraryIsReturned;
    }

    public String toString() {
        if(type == null)
            return "any JQWikArbitrary is defined";
        else
            return "JQWiKArbitrary " + type + " is defined";
    }
}
