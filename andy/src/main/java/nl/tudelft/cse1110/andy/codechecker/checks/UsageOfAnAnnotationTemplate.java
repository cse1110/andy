package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

public abstract class UsageOfAnAnnotationTemplate extends Check {
    protected final int minimumNumberOfUsage;
    protected final Comparison comparison;
    private int numberOfTestsInCU = 0;

    public UsageOfAnAnnotationTemplate(Comparison comparison, int minimumNumberOfUsage) {
        this.comparison = comparison;
        this.minimumNumberOfUsage = minimumNumberOfUsage;
        assert minimumNumberOfUsage >= 0;
    }

    @Override
    public boolean visit(MarkerAnnotation node) {
        count(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        count(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        count(node.getTypeName());
        return true;
    }

    private void count(Name name) {
        if (annotationName().equals(name.getFullyQualifiedName()))
            numberOfTestsInCU++;
    }

    protected abstract String annotationName();

    @Override
    public boolean result() {
        return comparison.compare(numberOfTestsInCU, minimumNumberOfUsage);
    }
}
