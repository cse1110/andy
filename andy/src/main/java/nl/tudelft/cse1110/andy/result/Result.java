package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.grade.GradeWeight;

import java.util.Optional;

public class Result {

    private final CompilationResult compilation;
    private final UnitTestsResult tests;
    private final MutationTestingResult mutationTesting;
    private final CodeChecksResult codeChecks;
    private final CoverageResult coverage;
    private final MetaTestsResult metaTests;
    private final int finalGrade;
    private final GenericFailure genericFailure;
    private final double timeInSeconds;
    private final GradeWeight weights;
    private final String successMessage;

    public Result(CompilationResult compilation, UnitTestsResult tests, MutationTestingResult mutationTesting, CodeChecksResult codeChecks, CoverageResult coverage, MetaTestsResult metaTests, int finalGrade, GenericFailure genericFailure, double timeInSeconds, GradeWeight weights, String successMessage) {
        this.compilation = compilation;
        this.tests = tests;
        this.mutationTesting = mutationTesting;
        this.codeChecks = codeChecks;
        this.coverage = coverage;
        this.metaTests = metaTests;
        this.finalGrade = finalGrade;
        this.genericFailure = genericFailure;
        this.timeInSeconds = timeInSeconds;
        this.weights = weights;
        this.successMessage = successMessage;

        if(finalGrade < 0 || finalGrade>100)
            throw new RuntimeException("Invalid final grade");
    }

    public Result(CompilationResult compilation, double timeInSeconds) {
        this(compilation, UnitTestsResult.empty(), MutationTestingResult.empty(), CodeChecksResult.empty(), CoverageResult.empty(), MetaTestsResult.empty(), 0, GenericFailure.noFailure(), timeInSeconds, null, null);
    }

    public CompilationResult getCompilation() {
        return compilation;
    }

    public UnitTestsResult getTests() {
        return tests;
    }

    public MutationTestingResult getMutationTesting() {
        return mutationTesting;
    }

    public CodeChecksResult getCodeChecks() {
        return codeChecks;
    }

    public CoverageResult getCoverage() {
        return coverage;
    }

    public MetaTestsResult getMetaTests() {
        return metaTests;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    public double getTimeInSeconds() {
        return timeInSeconds;
    }

    public GradeWeight getWeights() {
        return weights;
    }

    public Optional<String> getSuccessMessage() {
        return Optional.ofNullable(successMessage);
    }

    public GenericFailure getGenericFailure() {
        return genericFailure;
    }

    public boolean hasFailed() {
        return !compilation.successful() || tests.hasTestsFailingOrFailures() || hasGenericFailure();
    }

    public boolean hasGenericFailure() {
        return genericFailure.hasFailure();
    }

    @Override
    public String toString() {
        return "Result{" +
                "compilation=" + compilation +
                ", tests=" + tests +
                ", mutationTesting=" + mutationTesting +
                ", codeChecks=" + codeChecks +
                ", coverage=" + coverage +
                ", metaTests=" + metaTests +
                ", finalGrade=" + finalGrade +
                ", genericFailure='" + genericFailure + '\'' +
                ", timeInSeconds=" + timeInSeconds +
                ", weights=" + weights +
                ", successMessage=" + successMessage +
                '}';
    }

    public boolean isFullyCorrect() {
        return finalGrade == 100;
    }
}
