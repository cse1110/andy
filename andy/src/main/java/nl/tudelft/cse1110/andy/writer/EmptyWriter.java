package nl.tudelft.cse1110.andy.writer;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.result.Result;

public class EmptyWriter implements ResultWriter {
    @Override
    public void write(Context ctx, Result result) {

    }

    @Override
    public void uncaughtError(Context ctx, Throwable t) {

    }
}
