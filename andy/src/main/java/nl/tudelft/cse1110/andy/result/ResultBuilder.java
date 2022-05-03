package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckType;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.grade.GradeValues;
import nl.tudelft.cse1110.andy.grade.GradeWeight;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.pitest.mutationtest.tooling.CombinedStatistics;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.*;

import static javax.tools.Diagnostic.Kind.ERROR;
import static nl.tudelft.cse1110.andy.utils.ExceptionUtils.exceptionMessage;
import static nl.tudelft.cse1110.andy.utils.JUnitUtils.*;

public class ResultBuilder {

    private final Context ctx;
    private final GradeCalculator gradeCalculator;

    // junit callback so that we get extra info from the test runs
    private Map<TestIdentifier, ReportEntry> additionalReports = new HashMap<>();

    // it will be set in case of a generic failure. If one happens, the output is purely the generic failure
    private GenericFailure genericFailureObject = GenericFailure.noFailure();
    private String genericFailureMessage;
    private String genericFailureStepName;
    private String genericFailureExceptionMessage;
    private Integer genericFailureExternalProcessExitCode;
    private String genericFailureExternalProcessErrorMessages;

    // just so we know the time it took
    private long startTime = System.nanoTime();

    private CompilationResult compilation = CompilationResult.empty();
    private UnitTestsResult testResults = UnitTestsResult.empty();
    private MutationTestingResult mutationResults = MutationTestingResult.empty();
    private CodeChecksResult codeCheckResults = CodeChecksResult.empty();
    private CoverageResult coverageResults = CoverageResult.empty();
    private MetaTestsResult metaTestResults = MetaTestsResult.empty();

    public ResultBuilder(Context ctx, GradeCalculator gradeCalculator) {
        this.ctx = ctx;
        this.gradeCalculator = gradeCalculator;
    }

    /*
     * Compilation
     */
    public void compilationSuccess() {
        this.compilation = CompilationResult.compilationOk();
    }

    public void compilationFail(List<Diagnostic<? extends JavaFileObject>> errors) {
        List<CompilationErrorInfo> compilationErrors = new ArrayList<>();

        for(Diagnostic<? extends JavaFileObject> diagnostic : errors) {
            if (diagnostic.getKind() == ERROR) {
                long lineNumber = diagnostic.getLineNumber();
                String message = diagnostic.getMessage(null);
                String name = diagnostic.getSource().getName();

                compilationErrors.add(new CompilationErrorInfo(name, lineNumber, message));
            }
        }

        this.compilation = CompilationResult.compilationFail(compilationErrors);
    }

    public void compilationSecurityFail(String message) {
        this.compilation = CompilationResult.compilationFail(List.of(
                new CompilationErrorInfo("Solution.java", 1, message)
        ));
    }

    /*
     * JUnit tests
     */
    public void logJUnitRun(TestExecutionSummary summary, String console) {

        // statistics about tests found, executed, and succeeded
        int testsFound = (int) summary.getTestsFoundCount();
        int testsRan = (int) summary.getTestsStartedCount();
        int testsSucceeded = (int) summary.getTestsSucceededCount();

        // get test failures, if any
        List<TestFailureInfo> failures = new ArrayList<>();
        for (TestExecutionSummary.Failure failure : summary.getFailures()) {
            TestFailureInfo testFailureInfo = extractTestFailure(failure);
            failures.add(testFailureInfo);
        }

        this.testResults = UnitTestsResult.build(testsFound, testsRan, testsSucceeded, failures, console);
    }

    public void logAdditionalReport(TestIdentifier testIdentifier, ReportEntry report) {
        this.additionalReports.put(testIdentifier, report);
    }

    private TestFailureInfo extractTestFailure(TestExecutionSummary.Failure failure) {
        boolean isParameterizedTest = failure.getTestIdentifier().getUniqueId().contains("test-template-invocation");
        boolean isPBT = failure.getTestIdentifier().getUniqueId().contains("property");

        String testCase;
        String message = null;
        if(isParameterizedTest) {
            String methodName = getParameterizedMethodName(failure);
            String testCaseNumber = getParameterizedTestCaseNumber(failure);

            testCase = String.format("%s (%d)", methodName, Integer.parseInt(testCaseNumber));
            message = failure.getException().toString();
        } else {
            String methodName = failure.getTestIdentifier().getDisplayName();
            testCase = methodName;

            if (isPBT) {
                if (this.additionalReports.containsKey(failure.getTestIdentifier())) {
                    message = this.additionalReports.get(failure.getTestIdentifier()).getKeyValuePairs().toString();
                }

            } else {
                message = String.format("%s", simplifyTestErrorMessage(failure));
            }
        }

        return new TestFailureInfo(testCase, message);
    }

    /*
     * Log pitest
     */
    public void logPitest(CombinedStatistics stats) {

        int killedMutants = (int)(stats.getMutationStatistics().getTotalDetectedMutations());

        // get the total number of mutants, considering that the teacher may have overriden it
        int totalNumberOfMutants;
        int mutationsToConsider = ctx.getRunConfiguration().numberOfMutationsToConsider();
        boolean numberOfMutantsToConsiderIsOverridden = mutationsToConsider != -1;
        if (numberOfMutantsToConsiderIsOverridden) {
            totalNumberOfMutants = mutationsToConsider;
        } else {
            totalNumberOfMutants = (int)(stats.getMutationStatistics().getTotalMutations());
        }

        // make the number of killed mutants bounded by the total number of mutants
        killedMutants = Math.min(killedMutants, totalNumberOfMutants);

        this.mutationResults = MutationTestingResult.build(killedMutants, totalNumberOfMutants);
    }

    /*
     * Code checks
     */
    public void logCodeChecks(CheckScript script) {

        List<CodeCheckResult> checkResults = new ArrayList<>();

        if (script.hasChecks()) {
            List<CheckType> checks = script.getChecks();
            for (CheckType check : checks) {
                checkResults.add(new CodeCheckResult(check.getDescription(), check.getWeight(), check.getFinalResult()));
            }
        }

        this.codeCheckResults = CodeChecksResult.build(checkResults);
    }

    /*
     * Coverage
     */
    public void logJacoco(Collection<IClassCoverage> coverages) {
        int totalCoveredLines = coverages.stream().mapToInt(coverage -> coverage.getLineCounter().getCoveredCount()).sum();
        int totalLines = coverages.stream().mapToInt(coverage -> coverage.getLineCounter().getTotalCount()).sum();

        int totalCoveredInstructions = coverages.stream().mapToInt(coverage -> coverage.getInstructionCounter().getCoveredCount()).sum();
        int totalInstructions = coverages.stream().mapToInt(coverage -> coverage.getInstructionCounter().getTotalCount()).sum();

        int totalCoveredBranches = coverages.stream().mapToInt(coverage -> coverage.getBranchCounter().getCoveredCount()).sum();
        int totalBranches = coverages.stream().mapToInt(coverage -> coverage.getBranchCounter().getTotalCount()).sum();

        CoverageLineByLine coverageLineByLine = getCoverageLineByLine(coverages);

        this.coverageResults = CoverageResult.build(totalCoveredLines, totalLines,
                totalCoveredInstructions, totalInstructions,
                totalCoveredBranches, totalBranches, coverageLineByLine);
    }

    private CoverageLineByLine getCoverageLineByLine(Collection<IClassCoverage> coverages) {
        List<Integer> fullyCoveredLines = new ArrayList<>();
        List<Integer> partiallyCoveredLines = new ArrayList<>();
        List<Integer> notCoveredLines = new ArrayList<>();

        for (IClassCoverage coverage : coverages) {
            for (IMethodCoverage method : coverage.getMethods()) {
                for(int line = method.getFirstLine(); line <= method.getLastLine(); line++) {
                    int totalBranches = method.getLine(line).getBranchCounter().getTotalCount();
                    int missedBranches = method.getLine(line).getBranchCounter().getMissedCount();
                    boolean lineTouched = method.getLine(line).getInstructionCounter().getTotalCount() == 0 || method.getLine(line).getInstructionCounter().getCoveredCount() > 0;

                    boolean fullCoverage = lineTouched && missedBranches == 0;
                    boolean partialCoverage = lineTouched && missedBranches > 0 && totalBranches > 0;

                    if(fullCoverage) {
                        fullyCoveredLines.add(line);
                    } else if(partialCoverage) {
                        partiallyCoveredLines.add(line);
                    } else {
                        notCoveredLines.add(line);
                    }
                }
            }
        }

        return new CoverageLineByLine(fullyCoveredLines, partiallyCoveredLines, notCoveredLines);
    }

    /*
     * Meta tests
     */
    public void logMetaTests(int score, int totalTests, List<MetaTestResult> metaTestResults) {
        this.metaTestResults.addResults(score, totalTests, metaTestResults);
    }

    /*
     * Generic failures
     */
    public void genericFailure(String msg) {
        this.genericFailureMessage = msg;
        this.buildGenericFailure();
    }

    public void genericFailure(String step, String genericFailureExceptionMessage) {
        this.genericFailureStepName = step;
        this.genericFailureExceptionMessage = genericFailureExceptionMessage;
        this.buildGenericFailure();
    }

    public void genericFailure(ExecutionStep step, Throwable e) {
        this.genericFailure(step.getClass().getSimpleName(), exceptionMessage(e));
    }

    /*
     * Build the final result
     */
    public Result build() {
        double timeInSeconds = calculateTimeItTookToRun();

        if(!compilation.successful()) {
            return new Result(compilation, timeInSeconds);
        } else {
            GradeValues grades = GradeValues.fromResults(coverageResults, codeCheckResults, mutationResults, metaTestResults);
            GradeWeight weights = GradeWeight.fromConfig(ctx.getRunConfiguration().weights());
            String successMessage = ctx.getRunConfiguration().successMessage();

            this.checkExternalProcessExit();

            int finalGrade = calculateFinalGrade(grades, weights);

            return new Result(compilation,
                    testResults,
                    mutationResults,
                    codeCheckResults,
                    coverageResults,
                    metaTestResults,
                    finalGrade,
                    genericFailureObject,
                    timeInSeconds,
                    weights,
                    successMessage);
        }
    }

    private void buildGenericFailure() {
        this.genericFailureObject = GenericFailure.build(genericFailureMessage, genericFailureStepName, genericFailureExceptionMessage,
                genericFailureExternalProcessExitCode, genericFailureExternalProcessErrorMessages);
    }

    private void checkExternalProcessExit() {
        ExternalProcess process = ctx.getExternalProcess();
        if (!process.hasExitedNormally()) {
            this.genericFailureExternalProcessExitCode = process.getExitCode();
            this.genericFailureExternalProcessErrorMessages = process.getErrorMessages();
            this.buildGenericFailure();
        }
    }

    private double calculateTimeItTookToRun() {
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        double timeInSeconds = (double) elapsedTime / 1_000_000_000.0;
        return timeInSeconds;
    }

    private int calculateFinalGrade(GradeValues grades, GradeWeight weights) {
        if(hasFailed())
            return 0;

        if(!ctx.getModeActionSelector().shouldCalculateAndShowGrades())
            return 0;

        int finalGrade = gradeCalculator.calculateFinalGrade(grades, weights);
        return finalGrade;
    }

    public boolean hasFailed() {
        boolean compilationFailed = compilation!=null && !compilation.successful();
        boolean unitTestsFailed = testResults != null && testResults.didNotGoWell();
        boolean hasGenericFailure = genericFailureObject.hasFailure();

        return compilationFailed || unitTestsFailed || hasGenericFailure;
    }

    public UnitTestsResult getTestResults() {
        return testResults;
    }
}
