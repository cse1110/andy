package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.*;
import org.dom4j.rule.Mode;

import java.util.Collections;
import java.util.List;

import static nl.tudelft.cse1110.andy.grader.config.RunConfiguration.*;
import static nl.tudelft.cse1110.andy.grader.util.ModeUtils.*;

public class ModeSelector {

    private String mode;

    public ModeSelector(String mode) {
        this.mode = mode;
    }

    public List<ExecutionStep> selectMode() {
        switch (mode) {
            case PRACTICE_MODE -> {
                if (hints() || noHints()) {
                    return fullMode();
                } else if (coverage()) {
                    return withCoverage();
                } else {
                    return justTests();
                }
            }
            case EXAM_MODE -> {
                if (coverage()) {
                    return withCoverage();
                } else {
                    return justTests();
                }
            }
            case GRADING_MODE -> {
                return fullMode();
            }
        }
        return Collections.emptyList();
    }

    private List<ExecutionStep> justTests() {
        return List.of(new RunJUnitTestsStep());
    }

    private List<ExecutionStep> withCoverage() {
        return List.of(
                new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep()
        );
    }

    private List<ExecutionStep> fullMode() {
        return List.of(
                new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep(),
                new RunCodeChecksStep(),
                new RunMetaTestsStep(),
                new CalculateFinalGradeStep()
        );
    }

}
