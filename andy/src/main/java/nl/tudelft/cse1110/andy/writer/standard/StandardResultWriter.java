package nl.tudelft.cse1110.andy.writer.standard;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.result.*;
import nl.tudelft.cse1110.andy.utils.ImportUtils;
import nl.tudelft.cse1110.andy.utils.PropertyUtils;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.Highlight;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nl.tudelft.cse1110.andy.utils.ExceptionUtils.exceptionMessage;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.writeToFile;

public class StandardResultWriter implements ResultWriter {

    private final VersionInformation versionInformation;
    private final RandomAsciiArtGenerator asciiArtGenerator;

    private StringBuilder toDisplay = new StringBuilder();
    private List<Highlight> highlights = new ArrayList<>();

    public StandardResultWriter(VersionInformation versionInformation, RandomAsciiArtGenerator asciiArtGenerator) {
        this.versionInformation = versionInformation;
        this.asciiArtGenerator = asciiArtGenerator;
    }

    public StandardResultWriter() {
        this(PropertyUtils.getVersionInformation(), new RandomAsciiArtGenerator());
    }

    @Override
    public void write(Context ctx, Result result) {
        writeStdOutFile(ctx, result);
    }

    @Override
    public void uncaughtError(Context ctx, Throwable t) {
        StringBuilder errorMsg = new StringBuilder();

        errorMsg.append("\n\n*** ERROR ***\n");
        errorMsg.append("Something unexpected just happened. Please forward this message to the teacher.\n\n");
        errorMsg.append(exceptionMessage(t));

        File stdoutTxt = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "stdout.txt"));
        writeToFile(stdoutTxt, errorMsg.toString());
    }

    private void writeStdOutFile(Context ctx, Result result) {
        printVersionInformation();

        boolean hasFailure = printFailure(result);

        if(!hasFailure) {
            printCompilationResult(result.getCompilation());
            printTestResults(result.getTests());
            printCoverageResults(result.getCoverage());
            printMutationTestingResults(result.getMutationTesting());
            printCodeCheckResults(ctx, result.getCodeChecks());
            printMetaTestResults(ctx, result.getMetaTests());
            printFinalGrade(ctx, result);
            printModeAndTimeToRun(ctx, result.getTimeInSeconds());
        }

        File stdoutTxt = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "stdout.txt"));
        writeToFile(stdoutTxt, toDisplay.toString());
    }

    private boolean printFailure(Result result) {
        if (result.hasGenericFailure()) {
            toDisplay.append("Oh, we are facing a failure that we cannot recover from.\n");

            result.getGenericFailure().getStepName()
                    .ifPresent(s -> toDisplay.append(String.format("The failure occurred in %s.\n", s)));

            Optional<String> exceptionMessage = result.getGenericFailure().getExceptionMessage();
            if (exceptionMessage.isPresent()) {
                toDisplay.append("Please, send the message below to the teaching team:\n");
                toDisplay.append("---\n");
                toDisplay.append(exceptionMessage.get());
                toDisplay.append("---\n");
            }

            Optional<Integer> externalProcessExitCode = result.getGenericFailure().getExternalProcessExitCode();
            if (externalProcessExitCode.isPresent()) {
                toDisplay.append(String.format("External process crashed with exit code %d.\n",
                        externalProcessExitCode.get()));
                Optional<String> externalProcessErrorMessages = result.getGenericFailure().getExternalProcessErrorMessages();
                if (externalProcessErrorMessages.isPresent() &&
                    !externalProcessErrorMessages.get().isEmpty()) {
                    toDisplay.append(String.format("    Error message:\n%s\n",
                            externalProcessErrorMessages.get()));
                }
            }

            result.getGenericFailure().getGenericFailureMessage().ifPresent(s -> toDisplay.append(s));

            return true;
        }

        return false;
    }


    private void printFinalGrade(Context ctx, Result result) {
        // we only show grades in specific modes and actions
        // if ModeActionSelector is not injected yet (i.e., it's null), it's because compilation fail.
        // in this case, we give it a zero, no matter the mode.
        boolean shouldShowGrades = modeActionSelector(ctx) != null && modeActionSelector(ctx).shouldCalculateAndShowGrades();
        if (!shouldShowGrades) {
            printZeroGradeReason(ctx);
            return;
        }

        int finalGrade = result.getFinalGrade();

        l("");
        l("--- Assessment");

        // describe the weights and grades per component
        if(result.getCompilation().successful()) {
            printGradeCalculationDetails("Branch coverage", result.getCoverage().getCoveredBranches(), result.getCoverage().getTotalNumberOfBranches(), result.getWeights().getBranchCoverageWeight());
            printGradeCalculationDetails("Mutation coverage", result.getMutationTesting().getKilledMutants(), result.getMutationTesting().getTotalNumberOfMutants(), result.getWeights().getMutationCoverageWeight());
            printGradeCalculationDetails("Code checks", result.getCodeChecks().getNumberOfPassedChecks(), result.getCodeChecks().getTotalNumberOfChecks(), result.getWeights().getCodeChecksWeight());
            printGradeCalculationDetails("Meta tests", result.getMetaTests().getPassedMetaTests(), result.getMetaTests().getTotalTests(), result.getWeights().getMetaTestsWeight());
            l("");
        }

        // print the final grade
        l(String.format("Final grade: %d/100", finalGrade));

        // print success message and some nice ascii art if it's full grade!
        this.printFullGradeMessages(result);
    }

    private void printFullGradeMessages(Result result) {
        if (!result.isFullyCorrect()) {
            return;
        }

        result.getSuccessMessage().ifPresent(msg -> l("\n" + msg));

        String randomAsciiArt = asciiArtGenerator.getRandomAsciiArt();
        l("");
        l(randomAsciiArt);
    }

    private void printZeroGradeReason(Context ctx) {
        var mas = modeActionSelector(ctx);
        if (mas == null) {
            return;
        }

        StringBuilder reason = new StringBuilder();

        reason.append("\nFinal test score is ");

        if (mas.getMode() == Mode.EXAM) {
            reason.append("shown as 0/100 during the exam and will be calculated after the exam is over.");
        } else {
            reason.append("0/100 ");
            if (mas.getAction() != Action.FULL_WITH_HINTS && mas.getAction() != Action.FULL_WITHOUT_HINTS) {
                reason.append("as you are only ");
                if (mas.getAction() == Action.TESTS)
                    reason.append("checking if your tests pass");
                else if (mas.getAction() == Action.COVERAGE)
                    reason.append("running code coverage");
            }
            reason.append(". ");

            reason.append("To have your code graded, click on one of the buttons to assess your submission (with or without hints).");
        }

        l(reason.toString());
    }

    private void printGradeCalculationDetails(String what, int score, int total, double weight) {
        l(String.format("%s: %d/%d (overall weight=%.2f)%s", what, score, total, weight,
                (total > 0 && weight==0 ? " (0 gives full points)":"") ));
    }

    private void printModeAndTimeToRun(Context ctx, double timeInSeconds) {
        if(modeActionSelector(ctx)!=null)
            l(String.format("\nAndy is running in %s mode and took %.1f seconds to assess your solution.", modeActionSelector(ctx).getMode().toString(), timeInSeconds));
        else
            l(String.format("\nAndy took %.1f seconds to assess your question.", timeInSeconds));
    }

    private void printMetaTestResults(Context ctx, MetaTestsResult metaTests) {
        if(!metaTests.wasExecuted())
            return;

        boolean allHints = modeActionSelector(ctx).shouldShowFullHints();
        boolean onlyResult = modeActionSelector(ctx).shouldShowPartialHints();

        if(!allHints && !onlyResult)
            return;

        l("\n--- Meta tests");
        l(String.format("%d/%d passed", metaTests.getPassedMetaTests(), metaTests.getTotalTests()));

        if (allHints) {
            for (MetaTestResult metaTestResult : metaTests.getMetaTestResults()) {
                if (metaTestResult.succeeded()) {
                    l(String.format("Meta test: %s (weight: %d) PASSED", metaTestResult.getName(), metaTestResult.getWeight()));
                } else {
                    l(String.format("Meta test: %s (weight: %d) FAILED", metaTestResult.getName(), metaTestResult.getWeight()));
                }
            }
        }

    }

    private void printCoverageResults(CoverageResult coverage) {
        if(!coverage.wasExecuted())
            return;

        l("\n--- JaCoCo coverage");

        l(String.format("Line coverage: %d/%d", coverage.getCoveredLines(), coverage.getTotalNumberOfLines()));
        l(String.format("Instruction coverage: %d/%d", coverage.getCoveredInstructions(), coverage.getTotalNumberOfInstructions()));
        l(String.format("Branch coverage: %d/%d", coverage.getCoveredBranches(), coverage.getTotalNumberOfBranches()));

        if(!coverage.getPartiallyCoveredLines().isEmpty()) {
            l(String.format("Partially covered lines: %s",
                    coverage.getPartiallyCoveredLines().stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", "))));
        }

        if(!coverage.getNotCoveredLines().isEmpty()) {
            l(String.format("Lines not covered: %s",
                    coverage.getNotCoveredLines().stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", "))));
        }

    }

    private void printCodeCheckResults(Context ctx, CodeChecksResult codeChecks) {
        if(!codeChecks.wasExecuted())
            return;

        boolean allHints = modeActionSelector(ctx).shouldShowFullHints();
        boolean onlyResult = modeActionSelector(ctx).shouldShowPartialHints();

        if(!allHints && !onlyResult)
            return;

        if(!codeChecks.hasChecks()) {
            l("\n--- Code checks");
            l("No code checks to be assessed");
        }
        else {
            l("\n--- Code checks");
            l(String.format("%d/%d passed", codeChecks.getNumberOfPassedChecks(), codeChecks.getTotalNumberOfChecks()));

            if(allHints) {
                for (CodeCheckResult result : codeChecks.getCheckResults()) {
                    l(String.format("%s: %s (weight: %d)",
                            result.getDescription(),
                            result.passed() ? "PASS" : "FAIL",
                            result.getWeight()));
                }

            }
        }

    }

    private void printMutationTestingResults(MutationTestingResult mutationTesting) {
        if(!mutationTesting.wasExecuted())
            return;

        l("\n--- Mutation testing");
        l(String.format("%d/%d killed", mutationTesting.getKilledMutants(), mutationTesting.getTotalNumberOfMutants()));
    }

    private void printTestResults(UnitTestsResult tests) {

        if(!tests.wasExecuted())
            return;

        l("\n--- JUnit execution");
        if (tests.getTestsFound() == 0) {
            noTestsFound(tests);
        } else {
            l(String.format("%d/%d passed", tests.getTestsSucceeded(), tests.getTestsFound()));

            if(tests.hasFailingMessage()) {
                l("");

                for (TestFailureInfo failure : tests.getFailures()) {
                    l("- " + failure.getTestCase() + ":");
                    if(failure.hasMessage())
                        l(failure.getMessage());
                }

                l("You must ensure that all tests are passing! Stopping the assessment.");
            }

            if(tests.hasConsoleOutput()) {
                l("\n--- Console output");
                l(tests.getConsole());
            }

        }
    }

    private void noTestsFound(UnitTestsResult tests) {
        l(
                "Warning\nWe do not see any tests.\n" +
                        "Please check for the following JUnit pre-conditions:\n" +
                        "- Normal tests must be annotated with \"@Test\"\n" +
                        "- Parameterized tests must be annotated with \"@ParameterizedTest\"\n" +
                        "- Method sources must be static and provided as: \"@MethodSource(\"generator\")\" e.g.\n" +
                        "- Property based tests must be annotated with \"@Property\"\n" +
                        "- @BeforeAll and @AfterAll methods should be static\n" +
                        "- @BeforeEach methods should be non-static");
    }

    private void printCompilationResult(CompilationResult compilation) {
        if(compilation.successful()) {
            l("--- Compilation\nSuccess");
        } else {
            l("We could not compile the code. See the compilation errors below:");
            List<CompilationErrorInfo> compilationErrors = compilation.getErrors();

            for(CompilationErrorInfo error : compilationErrors) {

                String message = error.getMessage();
                long lineNumber = error.getLineNumber();
                l(String.format("- line %d: %s",
                        lineNumber,
                        message));

                Optional<String> importLog = ImportUtils.checkMissingImport(message);
                importLog.ifPresent(this::l);

                highlights.add(new Highlight(lineNumber, message, Highlight.HighlightLocation.SOLUTION, Highlight.HighlightPurpose.COMPILATION_ERROR));
            }

            if(compilation.hasConfigurationError()) {
                l("\n**WARNING:** There might be a problem with this exercise. "+
                        "Please contact the teaching staff so they can fix this as quickly" +
                        "as possible. Thank you for your help! :)");
            }
        }
    }

    private void printVersionInformation() {
        l(String.format("Andy v%s-%s (%s)\n",
                versionInformation.getVersion(),
                versionInformation.getCommitId(),
                versionInformation.getBuildTimestamp()
        ));
    }

    private void l(String line) {
        toDisplay.append(line);
        toDisplay.append("\n");
    }

    private ModeActionSelector modeActionSelector(Context ctx) {
        return ctx.getModeActionSelector();
    }


}
