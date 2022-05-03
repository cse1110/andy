package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public abstract class Check extends ASTVisitor {

    public void check(CompilationUnit cu) {
        cu.accept(this);
    }

    public abstract boolean result();
}
