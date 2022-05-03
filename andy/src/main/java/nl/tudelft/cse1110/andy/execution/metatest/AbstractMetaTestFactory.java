package nl.tudelft.cse1110.andy.execution.metatest;

import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.execution.metatest.library.evaluators.InsertAtEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.library.evaluators.LineReplacementEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.library.evaluators.MetaEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.library.evaluators.StringReplacementEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.externalprocess.ExternalProcessMetaTest;
import nl.tudelft.cse1110.andy.execution.metatest.library.LibraryMetaTest;

public class AbstractMetaTestFactory {

    public AbstractMetaTest withStringReplacement(int weight, String name, String old, String replacement) {
        MetaEvaluator evaluator = new StringReplacementEvaluator(old, replacement);
        return new LibraryMetaTest(weight, name, evaluator);
    }

    public AbstractMetaTest withStringReplacement(String name, String old, String replacement) {
        return withStringReplacement(1, name, old, replacement);
    }

    public AbstractMetaTest withLineReplacement(int weight, String name, int start, int end, String replacement) {
        MetaEvaluator evaluator = new LineReplacementEvaluator(start, end, replacement);
        return new LibraryMetaTest(weight, name, evaluator);
    }

    public AbstractMetaTest withLineReplacement(String name, int start, int end, String replacement) {
        return withLineReplacement(1, name, start, end, replacement);
    }

    public AbstractMetaTest insertAt(int weight, String name, int lineToInsertStartingIn1, String contentToAdd) {
        MetaEvaluator evaluator = new InsertAtEvaluator(lineToInsertStartingIn1, contentToAdd);
        return new LibraryMetaTest(weight, name, evaluator);
    }

    public AbstractMetaTest insertAt(String name, int lineToInsertStartingIn1, String contentToAdd) {
        return insertAt(1, name, lineToInsertStartingIn1, contentToAdd);
    }

    public AbstractMetaTest withExternalProcess(int weight, String name, ExternalProcess externalProcess) {
        return new ExternalProcessMetaTest(weight, name, externalProcess);
    }

    public AbstractMetaTest withExternalProcess(String name, ExternalProcess externalProcess) {
        return withExternalProcess(1, name, externalProcess);
    }
}
