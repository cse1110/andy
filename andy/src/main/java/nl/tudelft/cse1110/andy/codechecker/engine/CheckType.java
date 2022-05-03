package nl.tudelft.cse1110.andy.codechecker.engine;

import org.eclipse.jdt.core.dom.CompilationUnit;

public abstract class CheckType {

    protected int weight;
    protected String description;

    public CheckType(int weight, String description) {
        this.weight = weight;
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public abstract boolean getFinalResult();
    public abstract void runCheck(CompilationUnit cu);

    public String getDescription() {
        return description;
    }
}
