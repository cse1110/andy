package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompilationTest extends IntegrationTestBase {

    @Test
    void compilationOk() {
        Result result = run(Action.TESTS, "ListUtilsLibrary", "ListUtilsCompilationSuccess");
        assertThat(result.getCompilation().successful()).isTrue();
        assertThat(result.getGenericFailure().hasFailure()).isFalse();
    }

    @Test
    void compilationFailsDuringGradingMeans0() {
        Result result = run(Action.FULL_WITH_HINTS, "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError", "ArrayUtilsInGradingMode");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getFinalGrade()).isEqualTo(0);
        assertThat(result.getTests().wasExecuted()).isFalse();
        assertThat(result.getGenericFailure().hasFailure()).isFalse();

        assertThat(result)
                .has(compilationErrorInLine(29))
                .has(compilationError("not a statement"))
                .has(compilationError("';' expected"));
    }

    @Test
    void compilationWithManyFailures() {
        Result result = run(Action.TESTS,"MathArraysLibrary","MathArraysDifferentCompilationErrors");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getFinalGrade()).isEqualTo(0);
        assertThat(result.getTests().wasExecuted()).isFalse();
        assertThat(result.getGenericFailure().hasFailure()).isFalse();

        assertThat(result)
                .has(compilationErrorInLine(21))
                .has(compilationErrorInLine(25))
                .has(compilationErrorInLine(33))
                .has(sameCompilationErrorManyTimes("cannot find symbol", 3));
    }

    @Test
    void compilationErrorIsInTheConfigFile() {
        Result result = run(Action.TESTS,"ListUtilsLibrary", "ListUtilsCompilationSuccess", "ListUtilsConfigWithCompilationError");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getCompilation().hasConfigurationError()).isTrue();
        assertThat(result.getTests().wasExecuted()).isFalse();
    }


    private static Condition<Result> compilationErrorInLine(int line) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getCompilation().getErrors().stream().anyMatch(e -> e.getLineNumber() == line);
            }
        };
    }

    private static Condition<Result> compilationError(String error) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getCompilation().getErrors().stream().anyMatch(e -> e.getMessage().equals(error));
            }
        };
    }

    private static Condition<Result> sameCompilationErrorManyTimes(String errorType, int times) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getCompilation().getErrors().stream().filter(e -> e.getMessage().contains(errorType)).count() == times;
            }
        };
    }




}
