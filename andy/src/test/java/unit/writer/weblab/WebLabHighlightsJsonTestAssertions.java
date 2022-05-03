package unit.writer.weblab;

import org.assertj.core.api.Condition;

import static unit.writer.standard.StandardResultTestAssertions.containsString;

public class WebLabHighlightsJsonTestAssertions {

    public static Condition<String> highlightLineFullyCovered(int line) {
        return libraryLine(line, "100% coverage", "FULL_COVERAGE");
    }

    public static Condition<String> highlightLinePartiallyCovered(int line) {
        return libraryLine(line, "Partial coverage", "PARTIAL_COVERAGE");
    }

    public static Condition<String> highlightLineNotCovered(int line) {
        return libraryLine(line, "No coverage", "NO_COVERAGE");
    }

    public static Condition<String> highlightCompilationError(int line, String message) {
        return solutionLine(line, message, "COMPILATION_ERROR");
    }

    private static Condition<String> libraryLine(int line, String message, String purpose) {
        return line(line, message, "LIBRARY", purpose);
    }

    private static Condition<String> solutionLine(int line, String message, String purpose) {
        return line(line, message, "SOLUTION", purpose);
    }

    private static Condition<String> line(int line, String message, String location, String purpose) {
        return containsString("{\"line\":" + line + "," +
                              "\"message\":\"" + message + "\"," +
                              "\"location\":\"" + location + "\"," +
                              "\"purpose\":\"" + purpose + "\"}");
    }

}
