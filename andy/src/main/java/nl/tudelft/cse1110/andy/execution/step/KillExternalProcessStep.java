package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

public class KillExternalProcessStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        ctx.killExternalProcess();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof KillExternalProcessStep;
    }
}
