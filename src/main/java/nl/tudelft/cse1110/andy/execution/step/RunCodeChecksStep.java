package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.io.FileNotFoundException;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.findSolution;

public class RunCodeChecksStep implements ExecutionStep {
    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        CheckScript script = runCfg.checkScript();
        try {
            script.runChecks(findSolution(dirCfg.getWorkingDir()));
            result.logCodeChecks(script);
        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunCodeChecksStep;
    }
}
