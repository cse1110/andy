package nl.tudelft.cse1110.andy.writer.weblab;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;

public class Submission {

    private final Action action;
    private final SubmissionMetaData metaData;
    private final Result result;

    public Submission(Action action, SubmissionMetaData metaData, Result result) {
        this.action = action;
        this.metaData = metaData;
        this.result = result;
    }

}
