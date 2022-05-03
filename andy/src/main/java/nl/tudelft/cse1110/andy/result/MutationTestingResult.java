package nl.tudelft.cse1110.andy.result;

public class MutationTestingResult {

    private final boolean wasExecuted;
    private final int killedMutants;
    private final int totalNumberOfMutants;

    private MutationTestingResult(boolean wasExecuted, int killedMutants, int totalNumberOfMutants) {
        this.wasExecuted = wasExecuted;
        this.killedMutants = killedMutants;
        this.totalNumberOfMutants = totalNumberOfMutants;

        if(killedMutants > totalNumberOfMutants)
            throw new RuntimeException("Number of killed mutants is greater than the total number of mutants.");
    }

    public static MutationTestingResult build(int killedMutants, int totalNumberOfMutants) {
        return new MutationTestingResult(true, killedMutants, totalNumberOfMutants);
    }

    public static MutationTestingResult empty() {
        return new MutationTestingResult(false, 0, 0);
    }

    public int getKilledMutants() {
        return killedMutants;
    }

    public int getTotalNumberOfMutants() {
        return totalNumberOfMutants;
    }

    public boolean allMutantsWereKilled() {
        return killedMutants == totalNumberOfMutants;
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    @Override
    public String toString() {
        return "MutationTestingResult{" +
                "wasExecuted=" + wasExecuted +
                ", killedMutants=" + killedMutants +
                ", totalNumberOfMutants=" + totalNumberOfMutants +
                '}';
    }
}
