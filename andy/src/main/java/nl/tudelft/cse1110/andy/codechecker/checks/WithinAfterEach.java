package nl.tudelft.cse1110.andy.codechecker.checks;

import java.util.HashSet;
import java.util.Set;

public abstract class WithinAfterEach extends WithinAnnotatedMethod {

    public static Set<String> AFTEREACH_ANNOTATION = new HashSet<>() {{
        add("AfterEach");
    }};

    protected Set<String> annotations() {
        return AFTEREACH_ANNOTATION;
    }
}
