package nl.tudelft.cse1110.andy.config.metatest.implementations;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.metatest.evaluators.MetaEvaluator;

public class LibraryMetaTest extends MetaTest {
    private MetaEvaluator evaluator;

    public LibraryMetaTest(int weight, String name, MetaEvaluator evaluator) {
        super(weight, name);
        this.evaluator = evaluator;
    }

    public String evaluate(String oldLibraryCode) {
        return this.evaluator.evaluate(oldLibraryCode);
    }
}