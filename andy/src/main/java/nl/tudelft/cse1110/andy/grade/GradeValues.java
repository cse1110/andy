package nl.tudelft.cse1110.andy.grade;

import nl.tudelft.cse1110.andy.result.CodeChecksResult;
import nl.tudelft.cse1110.andy.result.CoverageResult;
import nl.tudelft.cse1110.andy.result.MetaTestsResult;
import nl.tudelft.cse1110.andy.result.MutationTestingResult;

public class GradeValues {

    private int coveredBranches;
    private int totalBranches;

    private int detectedMutations;
    private int totalMutations;

    private int metaTestsPassed;
    private int totalMetaTests;

    private int checksPassed;
    private int totalChecks;

    public int getCoveredBranches() {
        return coveredBranches;
    }

    public int getTotalBranches() {
        return totalBranches;
    }

    public int getDetectedMutations() {
        return detectedMutations;
    }

    public int getTotalMutations() {
        return totalMutations;
    }

    public int getMetaTestsPassed() {
        return metaTestsPassed;
    }

    public int getTotalMetaTests() {
        return totalMetaTests;
    }

    public int getChecksPassed() {
        return checksPassed;
    }

    public int getTotalChecks() {
        return totalChecks;
    }

    public void setBranchGrade(int coveredBranches, int totalBranches) {
        this.coveredBranches = coveredBranches;
        this.totalBranches = totalBranches;
    }

    public void setMutationGrade(int detectedMutations, int totalMutations) {
        this.detectedMutations = detectedMutations;
        this.totalMutations = totalMutations;
    }

    public void setMetaGrade(int metaTestsPassed, int totalMetaTests) {
        this.metaTestsPassed = metaTestsPassed;
        this.totalMetaTests = totalMetaTests;
    }

    public void setCheckGrade(int checksPassed, int totalChecks) {
        this.checksPassed = checksPassed;
        this.totalChecks = totalChecks;
    }

    public static GradeValues fromResults(CoverageResult coverageResults, CodeChecksResult codeCheckResults, MutationTestingResult mutationResults, MetaTestsResult metaTestResults) {
        GradeValues grades = new GradeValues();
        grades.setBranchGrade(coverageResults.getCoveredBranches(), coverageResults.getTotalNumberOfBranches());
        grades.setCheckGrade(codeCheckResults.getNumberOfPassedChecks(), codeCheckResults.getTotalNumberOfChecks());
        grades.setMutationGrade(mutationResults.getKilledMutants(), mutationResults.getTotalNumberOfMutants());
        grades.setMetaGrade(metaTestResults.getPassedMetaTests(), metaTestResults.getTotalTests());

        return grades;
    }

}