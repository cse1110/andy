package nl.tudelft.cse1110.andy.result;

import java.util.Collections;
import java.util.List;

public class CoverageLineByLine {


    private final List<Integer> fullyCoveredLines;
    private final List<Integer> partiallyCoveredLines;
    private final List<Integer> notCoveredLines;

    public CoverageLineByLine(List<Integer> fullyCoveredLines, List<Integer> partiallyCoveredLines, List<Integer> notCoveredLines) {
        this.fullyCoveredLines = fullyCoveredLines;
        this.partiallyCoveredLines = partiallyCoveredLines;
        this.notCoveredLines = notCoveredLines;
    }

    public List<Integer> getFullyCoveredLines() {
        return Collections.unmodifiableList(fullyCoveredLines);
    }

    public List<Integer> getPartiallyCoveredLines() {
        return Collections.unmodifiableList(partiallyCoveredLines);
    }

    public List<Integer> getNotCoveredLines() {
        return Collections.unmodifiableList(notCoveredLines);
    }

    @Override
    public String toString() {
        return "CoverageLineByLine{" +
                "fullyCoveredLines=" + fullyCoveredLines +
                ", partiallyCoveredLines=" + partiallyCoveredLines +
                ", notCoveredLines=" + notCoveredLines +
                '}';
    }
}
