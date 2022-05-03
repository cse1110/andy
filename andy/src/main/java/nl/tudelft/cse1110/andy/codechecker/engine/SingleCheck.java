package nl.tudelft.cse1110.andy.codechecker.engine;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Objects;

public class SingleCheck extends CheckType {

    private final boolean flip;
    private final Check check;

    public SingleCheck(int weight, String description, boolean flip, Check check) {
        super(weight, description);
        this.flip = flip;
        this.check = check;
    }

    public SingleCheck(String description, Check check) {
        this(1, description, false, check);
    }

    public SingleCheck(Check check) {
        this(1, check.toString(), false, check);
    }

    public SingleCheck(String description, boolean flip, Check check) {
        this(1, description, flip, check);
    }
    public SingleCheck(boolean flip, Check check) {
        this(1, check.toString() + (flip ? " (NOT)":""), flip, check);
    }

    public SingleCheck(int weight, String description, Check check) {
        this(weight, description, false, check);
    }
    public SingleCheck(int weight, Check check) {
        this(weight, check.toString(), false, check);
    }

    @Override
    public boolean getFinalResult() {
        assert check != null : "You have to run the check first";

        // this is basically the same as return flip ? !result : result
        // in other words, if the flip boolean is active, it flips the result of the checker
        // but it's not every day that you can write an xor, is it?
        return flip ^ check.result();
    }

    @Override
    public void runCheck(CompilationUnit cu) {
        this.check.check(cu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleCheck that = (SingleCheck) o;
        return flip == that.flip && check.equals(that.check);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flip, check);
    }
}
