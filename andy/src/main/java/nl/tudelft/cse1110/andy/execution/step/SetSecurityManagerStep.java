package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.security.AndySecurityManager;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

public class SetSecurityManagerStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        if (System.getSecurityManager() instanceof AndySecurityManager) {
            return;
        }

        System.setSecurityManager(new AndySecurityManager());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SetSecurityManagerStep;
    }
}
