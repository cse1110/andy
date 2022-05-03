package nl.tudelft.cse1110.andy.result;

import java.util.Collections;
import java.util.List;

public class CoverageResult {
    private final boolean wasExecuted;
    private final int totalCoveredLines;
    private final int totalLines;
    private final int totalCoveredInstructions;
    private final int totalInstructions;
    private final int totalCoveredBranches;
    private final int totalBranches;
    private final CoverageLineByLine coverageLineByLine;

    private CoverageResult(boolean wasExecuted, int totalCoveredLines, int totalLines, int totalCoveredInstructions, int totalInstructions, int totalCoveredBranches, int totalBranches, CoverageLineByLine coverageLineByLine) {
        this.wasExecuted = wasExecuted;

        this.totalCoveredLines = totalCoveredLines;
        this.totalLines = totalLines;
        this.totalCoveredInstructions = totalCoveredInstructions;
        this.totalInstructions = totalInstructions;
        this.totalCoveredBranches = totalCoveredBranches;
        this.totalBranches = totalBranches;
        this.coverageLineByLine = coverageLineByLine;
    }

    public static CoverageResult build(int totalCoveredLines, int totalLines, int totalCoveredInstructions, int totalInstructions, int totalCoveredBranches, int totalBranches, CoverageLineByLine coverageLineByLine) {
        return new CoverageResult(true, totalCoveredLines, totalLines, totalCoveredInstructions, totalInstructions, totalCoveredBranches, totalBranches, coverageLineByLine);
    }

    public static CoverageResult empty() {
        return new CoverageResult(false, 0, 0, 0, 0, 0, 0, new CoverageLineByLine(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
    }

    public int getCoveredLines() {
        return totalCoveredLines;
    }

    public int getTotalNumberOfLines() {
        return totalLines;
    }

    public int getCoveredInstructions() {
        return totalCoveredInstructions;
    }

    public int getTotalNumberOfInstructions() {
        return totalInstructions;
    }

    public int getCoveredBranches() {
        return totalCoveredBranches;
    }

    public int getTotalNumberOfBranches() {
        return totalBranches;
    }

    public List<Integer> getFullyCoveredLines() {
        return coverageLineByLine.getFullyCoveredLines();
    }

    public List<Integer> getPartiallyCoveredLines() {
        return coverageLineByLine.getPartiallyCoveredLines();
    }

    public List<Integer> getNotCoveredLines() {
        return coverageLineByLine.getNotCoveredLines();
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    @Override
    public String toString() {
        return "CoverageResult{" +
                "wasExecuted=" + wasExecuted +
                ", totalCoveredLines=" + totalCoveredLines +
                ", totalLines=" + totalLines +
                ", totalCoveredInstructions=" + totalCoveredInstructions +
                ", totalInstructions=" + totalInstructions +
                ", totalCoveredBranches=" + totalCoveredBranches +
                ", totalBranches=" + totalBranches +
                ", coverageLineByLine=" + coverageLineByLine +
                '}';
    }
}
