package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JQWikProvideAnnotations extends Check {

    private boolean provideAnnotationIdentified = false;

    private static Set<String> JQWIK_PROVIDE_ANNOTATIONS = new HashSet<>() {{
       add("IntRange");
       add("Positive");
       add("Negative");

       // TODO: add all annotations here
    }};

    @Override
    public boolean visit(MarkerAnnotation node) {
        checkIfThisIsAJQWikAnnotation(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        checkIfThisIsAJQWikAnnotation(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        checkIfThisIsAJQWikAnnotation(node.getTypeName());
        return true;
    }


    private void checkIfThisIsAJQWikAnnotation(Name name) {
        if (JQWIK_PROVIDE_ANNOTATIONS.contains(name.getFullyQualifiedName()))
            provideAnnotationIdentified = true;
    }


    @Override
    public boolean result() {
        return provideAnnotationIdentified;
    }

    public String toString() {
        return "Any of " + JQWIK_PROVIDE_ANNOTATIONS.stream().collect(Collectors.joining(",")) + " is used";
    }
}
