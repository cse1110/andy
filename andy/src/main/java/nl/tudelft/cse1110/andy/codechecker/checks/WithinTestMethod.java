package nl.tudelft.cse1110.andy.codechecker.checks;

import java.util.HashSet;
import java.util.Set;

public abstract class WithinTestMethod extends WithinAnnotatedMethod {

    public static Set<String> TEST_ANNOTATIONS =
            new HashSet<>() {{
                add("Test"); // junit
                add("ParameterizedTest"); // junit
                add("Property"); // jqwik
            }};

    protected Set<String> annotations() {
        return TEST_ANNOTATIONS;
    }

}
