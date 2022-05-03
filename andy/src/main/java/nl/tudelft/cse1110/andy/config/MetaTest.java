package nl.tudelft.cse1110.andy.config;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.execution.metatest.AbstractMetaTestFactory;

public interface MetaTest {
    String getName();

    int getWeight();

    String getNameAndWeight();

    boolean execute(Context ctx, DirectoryConfiguration dirCfg, RunConfiguration runCfg) throws Exception;

    static MetaTest withStringReplacement(int weight, String name, String old, String replacement) {
        return new AbstractMetaTestFactory().withStringReplacement(weight, name, old, replacement);
    }

    static MetaTest withStringReplacement(String name, String old, String replacement) {
        return new AbstractMetaTestFactory().withStringReplacement(name, old, replacement);
    }

    static MetaTest withLineReplacement(int weight, String name, int start, int end, String replacement) {
        return new AbstractMetaTestFactory().withLineReplacement(weight, name, start, end, replacement);
    }

    static MetaTest withLineReplacement(String name, int start, int end, String replacement) {
        return new AbstractMetaTestFactory().withLineReplacement(name, start, end, replacement);
    }

    static MetaTest insertAt(int weight, String name, int lineToInsertStartingIn1, String contentToAdd) {
        return new AbstractMetaTestFactory().insertAt(weight, name, lineToInsertStartingIn1, contentToAdd);
    }

    static MetaTest insertAt(String name, int lineToInsertStartingIn1, String contentToAdd) {
        return new AbstractMetaTestFactory().insertAt(name, lineToInsertStartingIn1, contentToAdd);
    }

    static MetaTest withExternalProcess(int weight, String name, ExternalProcess externalProcess) {
        return new AbstractMetaTestFactory().withExternalProcess(weight, name, externalProcess);
    }

    static MetaTest withExternalProcess(String name, ExternalProcess externalProcess) {
        return new AbstractMetaTestFactory().withExternalProcess(name, externalProcess);
    }
}
