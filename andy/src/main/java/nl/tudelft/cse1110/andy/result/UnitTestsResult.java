package nl.tudelft.cse1110.andy.result;

import java.util.Collections;
import java.util.List;

public class UnitTestsResult {
    private final int testsFound;
    private final int testsRan;
    private final int testsSucceeded;
    private final List<TestFailureInfo> failures;
    private final String console;
    private final boolean wasExecuted;

    private UnitTestsResult(boolean wasExecuted, int testsFound, int testsRan, int testsSucceeded, List<TestFailureInfo> failures, String console) {
        this.wasExecuted = wasExecuted;
        this.testsFound = testsFound;
        this.testsRan = testsRan;
        this.testsSucceeded = testsSucceeded;
        this.failures = failures;
        this.console = console;
    }

    public static UnitTestsResult build(int testsFound, int testsRan, int testsSucceeded, List<TestFailureInfo> failures, String console) {
        return new UnitTestsResult(true, testsFound, testsRan, testsSucceeded, failures, console);
    }

    public static UnitTestsResult empty() {
        return new UnitTestsResult(false,0, 0, 0, Collections.emptyList(), null);
    }

    public int getTestsRan() {
        return testsRan;
    }

    public int getTestsSucceeded() {
        return testsSucceeded;
    }

    public int getTestsFound() {
        return testsFound;
    }

    public boolean hasTestsFailingOrFailures() {
        return testsSucceeded < testsFound || !getFailures().isEmpty();
    }

    public boolean hasFailingMessage() {
        return !failures.isEmpty();
    }

    public List<TestFailureInfo> getFailures() {
        return Collections.unmodifiableList(failures);
    }

    public String getConsole() {
        return console;
    }

    public boolean hasConsoleOutput() {
        return console!=null && !console.isEmpty();
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    public boolean didNotGoWell() {
        return wasExecuted && (hasTestsFailingOrFailures() || noTestsRan());
    }

    public boolean noTestsRan() {
        return testsRan == 0;
    }

    @Override
    public String toString() {
        return "UnitTestsResult{" +
                "testsFound=" + testsFound +
                ", testsRan=" + testsRan +
                ", testsSucceeded=" + testsSucceeded +
                ", failures=" + failures +
                ", console='" + console + '\'' +
                ", wasExecuted=" + wasExecuted +
                '}';
    }

    public int getNumberOfFailingTests() {
        return testsFound - testsSucceeded;
    }
}
