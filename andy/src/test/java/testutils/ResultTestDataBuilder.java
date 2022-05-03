package testutils;

import nl.tudelft.cse1110.andy.grade.GradeWeight;
import nl.tudelft.cse1110.andy.result.*;

import java.util.Arrays;
import java.util.List;

public class ResultTestDataBuilder {

    private String genericFailureMessage = null;
    private String genericFailureStepName = null;
    private String genericFailureExceptionMessage = null;
    private Integer genericFailureExternalProcessExitCode = null;
    private String genericFailureExternalProcessErrorMessages = null;
    private CompilationResult compilation = CompilationResult.empty();
    private UnitTestsResult testResults = UnitTestsResult.empty();
    private MutationTestingResult mutationResults = MutationTestingResult.empty();
    private CodeChecksResult codeCheckResults = CodeChecksResult.empty();
    private CoverageResult coverageResults = CoverageResult.empty();
    private MetaTestsResult metaTestResults = MetaTestsResult.empty();
    private int finalGrade = 0;
    private double timeInSeconds = 10;
    private GradeWeight weights = new GradeWeight(0.25f, 0.25f, 0.25f, 0.25f);
    private String successMessage = null;

    public ResultTestDataBuilder withCompilationFail(CompilationErrorInfo... errors) {
        compilation = CompilationResult.compilationFail(Arrays.asList(errors));
        return this;
    }

    public ResultTestDataBuilder withGenericFailure(String message) {
        genericFailureMessage = message;
        return this;
    }

    public ResultTestDataBuilder withGenericFailureStep(String step) {
        genericFailureStepName = step;
        return this;
    }

    public ResultTestDataBuilder withGenericFailureExceptionMessage(String e) {
        genericFailureExceptionMessage = e;
        return this;
    }

    public ResultTestDataBuilder withGenericFailureExternalProcessExitCode(Integer exitCode) {
        genericFailureExternalProcessExitCode = exitCode;
        return this;
    }

    public ResultTestDataBuilder withGenericFailureExternalProcessErrorMessages(String errorMessages) {
        genericFailureExternalProcessErrorMessages = errorMessages;
        return this;
    }

    public ResultTestDataBuilder withCoverageResult(CoverageResult coverageResult) {
        coverageResults = coverageResult;
        return this;
    }

    public ResultTestDataBuilder withGrade(int grade) {
        finalGrade = grade;
        return this;
    }

    public ResultTestDataBuilder withMutationTestingResults(int passed, int total) {
        mutationResults = MutationTestingResult.build(passed, total);
        return this;
    }

    public ResultTestDataBuilder withCodeCheckResults(List<CodeCheckResult> list) {
        codeCheckResults = CodeChecksResult.build(list);
        return this;
    }

    public ResultTestDataBuilder withMetaTestResults(List<MetaTestResult> list) {
        metaTestResults = MetaTestsResult.build(
                (int) list.stream().filter(MetaTestResult::succeeded).count(),
                list.size(),
                list
        );
        return this;
    }

    public ResultTestDataBuilder withWeights(float branchCoverageWeight, float mutationCoverageWeight, float metaTestsWeight, float codeChecksWeight) {
        weights = new GradeWeight(branchCoverageWeight, mutationCoverageWeight, metaTestsWeight, codeChecksWeight);
        return this;
    }

    public ResultTestDataBuilder withTestResults(int testsFound, int testsRan, int testsSucceeded, List<TestFailureInfo> failures, String console) {
        testResults = UnitTestsResult.build(testsFound, testsRan, testsSucceeded, failures, console);
        return this;
    }

    public ResultTestDataBuilder withSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
        return this;
    }

    public Result build() {
        GenericFailure genericFailure = GenericFailure.build(genericFailureMessage, genericFailureStepName, genericFailureExceptionMessage, genericFailureExternalProcessExitCode, genericFailureExternalProcessErrorMessages);

        return new Result(compilation, testResults, mutationResults, codeCheckResults, coverageResults, metaTestResults, finalGrade, genericFailure, timeInSeconds, weights, successMessage);
    }
}
