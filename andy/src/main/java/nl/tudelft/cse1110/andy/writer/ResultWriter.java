package nl.tudelft.cse1110.andy.writer;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.result.Result;

public interface ResultWriter {

    public void write(Context ctx, Result result);

    public void uncaughtError(Context ctx, Throwable t);
}
