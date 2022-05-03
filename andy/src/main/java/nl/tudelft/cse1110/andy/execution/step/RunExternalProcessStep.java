package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;

public class RunExternalProcessStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {
            RunConfiguration runConfiguration = ctx.getRunConfiguration();

            ExternalProcess externalProcess = runConfiguration.externalProcess();

            externalProcess.launch();

            ctx.setExternalProcess(externalProcess);

            externalProcess.awaitInitialization();

        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunExternalProcessStep;
    }
}
