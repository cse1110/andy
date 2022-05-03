package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.result.ResultBuilder;

public interface ExecutionStep {
    void execute(Context ctx, ResultBuilder result);
}
