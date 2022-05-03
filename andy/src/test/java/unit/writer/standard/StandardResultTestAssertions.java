package unit.writer.standard;

import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.Condition;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.not;


public class StandardResultTestAssertions {

    protected static Condition<String> containsRegex(String regex) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

    public static Condition<String> containsString(String str) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.contains(str);
            }
        };
    }

    public static Condition<String> startsWithString(String str) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.startsWith(str);
            }
        };
    }

    public static Condition<String> numberOfJUnitTestsPassing(int numberOfTestsPassing) {
        return containsRegex("--- JUnit execution\\n" + numberOfTestsPassing + "\\/\\d+ passed");
    }

    public static Condition<String> testResults() {
        return containsString("--- JUnit execution");
    }

    public static Condition<String> totalNumberOfJUnitTests(int numberOfTests) {
        return containsRegex("--- JUnit execution\\n\\d+\\/" + numberOfTests + " passed");
    }

    public static Condition<String> jUnitTestFailing(String testName, String message) {
        return containsRegex("- " + testName + ":\n" + message);
    }

    public static Condition<String> noJUnitTestsFound() {
        return containsString("Warning\nWe do not see any tests.");
    }

    public static Condition<String> unexpectedError() {
        return containsString("Something unexpected just happened");
    }

    public static Condition<String> compilationFailure() {
        return containsString("We could not compile the code. See the compilation errors below:");
    }

    public static Condition<String> compilationFailureConfigurationError() {
        return containsString("There might be a problem with this exercise");
    }


    public static Condition<String> compilationSuccess() {
        return containsString("--- Compilation\nSuccess");
    }


    public static Condition<String> compilationErrorOnLine(int lineNumber) {
        return containsRegex("- line " + lineNumber + ":\\s");
    }


    public static Condition<String> compilationErrorType(String errorType) {
        return containsRegex("- line \\d+: " + errorType);
    }

    public static Condition<String> versionInformation(String version, String commitId, String timestamp) {
        return containsString(String.format("Andy v%s-%s (%s)\n", version, commitId, timestamp));
    }

    public static Condition<String> versionInformation(VersionInformation versionInformation) {
        return versionInformation(versionInformation.getVersion(),
                versionInformation.getCommitId(),
                versionInformation.getBuildTimestamp());
    }

    public static Condition<String> partiallyCoveredLine(int line) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {

                int start = value.indexOf("Partially covered lines: ");
                if(start == -1)
                    return false;

                int end = value.indexOf("\n", start);

                String linesAsString = value.substring(start+"Partially covered lines: ".length(), end);
                String[] listOfLines = linesAsString.split(", ");
                return Arrays.stream(listOfLines).anyMatch(l -> l.equals(String.valueOf(line)));
            }
        };
    }

    public static Condition<String> notCoveredLine(int line) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {

                int start = value.indexOf("Lines not covered: ");
                if(start == -1)
                    return false;

                int end = value.indexOf("\n", start);

                String linesAsString = value.substring(start+"Lines not covered: ".length(), end);
                String[] listOfLines = linesAsString.split(", ");
                return Arrays.stream(listOfLines).anyMatch(l -> l.equals(String.valueOf(line)));
            }
        };
    }

    public static Condition<String> linesCovered(int numberOfLinesCovered) {
        return containsRegex("Line coverage: " + numberOfLinesCovered + "/\\d+");
    }

    public static Condition<String> instructionsCovered(int numberOfInstructionsCovered) {
        return containsRegex("Instruction coverage: " + numberOfInstructionsCovered + "/\\d+");
    }

    public static Condition<String> branchesCovered(int numberOfBranchesCovered) {
        return containsRegex("Branch coverage: " + numberOfBranchesCovered + "/\\d+");
    }

    public static Condition<String> metaTestsPassing(int numberOfMetaTestsPassing) {
        return containsRegex("--- Meta tests\n" + numberOfMetaTestsPassing + "/\\d+ passed");
    }

    public static Condition<String> metaTests(int numberOfMetaTests) {
        return containsRegex("--- Meta tests\n\\d+/" + numberOfMetaTests + " passed");
    }

    public static Condition<String> metaTestFailing(String metaTestName) {
        return containsRegex("Meta test: " + metaTestName + " \\(.*\\) FAILED");
    }

    public static Condition<String> metaTestPassing(String metaTestName) {
        return containsRegex("Meta test: " + metaTestName + " \\(.*\\) PASSED");
    }

    public static Condition<String> finalGrade(int score) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                // the message in the output string is correct
                boolean messageIsCorrect = value.contains("Final grade: " + score + "/100");

                return messageIsCorrect;
            }
        };
    }

    public static Condition<String> finalGradeInXml(String reportDir, int score) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return resultXmlHasCorrectGrade(reportDir, score);
            }
        };
    }

    private static boolean resultXmlHasCorrectGrade(String workDir, int score) {
        File resultXml = new File(FilesUtils.concatenateDirectories(workDir, "results.xml"));
        String resultXmlContent = FilesUtils.readFile(resultXml);

        int passes = StringUtils.countMatches(resultXmlContent, "<testcase/>");
        int fails = StringUtils.countMatches(resultXmlContent, "<testcase><failure></failure></testcase>");
        boolean resultXmlIsCorrect = passes == score && passes+fails==100;
        return resultXmlIsCorrect;
    }

    public static Condition<String> mutationScore(int mutantsKilled, int totalMutants) {
        return containsString("--- Mutation testing\n" + mutantsKilled + "/" + totalMutants);
    }

    public static Condition<String> allTestsNeedToPassMessage() {
        return containsString("You must ensure that all tests are passing");
    }

    public static Condition<String> scoreOfCodeChecks(int points, int total) {
        return containsString("--- Code checks\n"+points + "/" + total + " passed");
    }

    public static Condition<String> codeCheck(String description, boolean pass, int weight) {
        String expectedCheck = String.format("%s: %s (weight: %d)",
                description,
                pass ? "PASS" : "FAIL",
                weight);

        return containsString(expectedCheck);
    }

    public static Condition<String> totalTimeItTookToExecute() {
        return containsRegex("took \\d+(.\\d)? seconds");
    }

    public static Condition<String> consoleOutput (String output){
        return containsString(output);
    }

    public static Condition<String> consoleOutputExists () {
        return containsString("- Console output");
    }

    public static Condition<String> fullGradeDescription(String check, int scored, int total, double weight) {
        return containsString(String.format("%s: %d/%d (overall weight=%.2f)%s", check, scored, total, weight,
                (total > 0 && weight == 0 ? " (0 gives full points)" : "")));
    }

    public static Condition<String> mode(String mode) {
        return containsRegex(String.format("Andy is running in %s mode.", mode));
    }

    public static Condition<String> noMetaTests() {
        return not(containsRegex("--- Meta tests"));
    }


    public static Condition<String> noCodeChecks() {
        return not(containsRegex("--- Code checks"));
    }

    public static Condition<String> noCodeChecksToBeAssessed() {
        return containsString("--- Code checks\nNo code checks to be assessed");
    }

    public static Condition<String> noFinalGrade() {
        return not(containsRegex("--- Assessment"));
    }

    public static Condition<String> zeroScoreExplanation() {
        return containsRegex("Final test score is (shown as )?0/100");
    }

    public static Condition<String> noJacocoCoverage() {
        return not(containsRegex("--- JaCoCo coverage"));
    }

    public static Condition<String> noPitestCoverage() {
        return not(containsRegex("--- Mutation testing"));
    }

    public static Condition<String> genericFailure(String failure) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.contains("we are facing a failure") && value.contains(failure);
            }
        };
    }

}

