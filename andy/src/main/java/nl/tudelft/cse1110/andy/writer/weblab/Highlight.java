package nl.tudelft.cse1110.andy.writer.weblab;

public class Highlight {

    private final long line;
    private final String message;
    private final HighlightLocation location;
    private final HighlightPurpose purpose;

    public enum HighlightPurpose {
        COMPILATION_ERROR,
        NO_COVERAGE,
        PARTIAL_COVERAGE,
        FULL_COVERAGE
    }

    public enum HighlightLocation {
        LIBRARY,
        SOLUTION
    }

    public Highlight(long line, String message, HighlightLocation location, HighlightPurpose purpose) {
        this.line = line;
        this.message = message;
        this.location = location;
        this.purpose = purpose;
    }

    public long getLine() {
        return line;
    }

    public String getMessage() {
        return message;
    }

    public HighlightLocation getLocation() {
        return location;
    }

    public HighlightPurpose getPurpose() {
        return purpose;
    }

    @Override
    public String toString() {
        return "Highlight{" +
                "line=" + line +
                ", message='" + message + '\'' +
                ", location=" + location +
                ", purpose=" + purpose +
                '}';
    }
}
