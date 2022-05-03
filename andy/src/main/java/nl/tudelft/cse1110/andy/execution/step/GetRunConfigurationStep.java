package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.util.NoSuchElementException;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.allClassesButTestingAndConfigOnes;
import static nl.tudelft.cse1110.andy.utils.ClassUtils.getConfigurationClass;

public class GetRunConfigurationStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

            Class<?> runConfigurationClass = Class.forName(getConfigurationClass(ctx.getNewClassNames()), false, currentClassLoader);
            RunConfiguration runConfiguration = (RunConfiguration) runConfigurationClass.getDeclaredConstructor().newInstance();

            ctx.setRunConfiguration(runConfiguration);

            this.addExecutionSteps(ctx, result);
        } catch (NoSuchElementException ex) {
            // There's no configuration set. We put a default one!
            RunConfiguration runConfiguration = new DefaultRunConfiguration(allClassesButTestingAndConfigOnes(ctx.getNewClassNames()));
            ctx.setRunConfiguration(runConfiguration);

            this.addExecutionSteps(ctx, result);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    private void addExecutionSteps(Context ctx, ResultBuilder result) {
        ExecutionFlow flow = ctx.getFlow();

        ModeActionSelector modeActionSelector = createModeSelector(ctx, result);
        flow.addSteps(modeActionSelector.getSteps());
    }

    private ModeActionSelector createModeSelector(Context ctx, ResultBuilder result) {
        RunConfiguration runConfiguration = ctx.getRunConfiguration();

        Mode mode = runConfiguration.mode();
        Action action = ctx.getAction();

        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);
        ctx.setModeSelector(modeActionSelector);

        return modeActionSelector;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GetRunConfigurationStep;
    }
}
