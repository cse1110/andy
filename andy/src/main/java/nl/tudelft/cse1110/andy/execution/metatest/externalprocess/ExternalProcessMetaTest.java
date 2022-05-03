package nl.tudelft.cse1110.andy.execution.metatest.externalprocess;

import nl.tudelft.cse1110.andy.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.execution.metatest.AbstractMetaTest;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.step.RunJUnitTestsStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.io.IOException;

public class ExternalProcessMetaTest extends AbstractMetaTest {
    private final ExternalProcess externalProcess;

    public ExternalProcessMetaTest(int weight, String name, ExternalProcess externalProcess) {
        super(weight, name);
        this.externalProcess = externalProcess;
    }

    public void startExternalProcess() throws IOException {
        this.externalProcess.launch();
        this.externalProcess.awaitInitialization();
    }

    public void killExternalProcess() {
        this.externalProcess.kill();
    }

    @Override
    public boolean execute(Context ctx, DirectoryConfiguration dirCfg, RunConfiguration runCfg) throws Exception {
        /* Start the meta external process */
        this.startExternalProcess();

        /* Run the test suite using our existing JUnit runner */
        Context jUnitContext = new Context(Action.META_TEST);
        jUnitContext.setRunConfiguration(new DefaultRunConfiguration(ctx.getRunConfiguration().classesUnderTest()));
        ResultBuilder metaResultBuilder = new ResultBuilder(null, null);

        RunJUnitTestsStep jUnitStep = new RunJUnitTestsStep();
        jUnitStep.execute(ctx, metaResultBuilder);

        /* Kill the meta external process */
        this.killExternalProcess();

        /*
         * Status and possible error messages of the meta external process are ignored.
         * The external process may crash as part of normal operation as it is supposed to be a
         * buggy implementation due to the nature of meta tests.
         */

        /* Check the result. If there's a failing test, the test suite is good! */
        int testsRan = metaResultBuilder.getTestResults().getTestsRan();
        int testsSucceeded = metaResultBuilder.getTestResults().getTestsSucceeded();
        boolean passesTheMetaTest = testsSucceeded < testsRan;

        return passesTheMetaTest;
    }
}
