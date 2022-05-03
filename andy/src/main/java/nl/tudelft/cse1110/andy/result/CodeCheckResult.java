package nl.tudelft.cse1110.andy.result;

public class CodeCheckResult {
    private final String description;
    private final int weight;
    private final boolean passed;

    public CodeCheckResult(String description, int weight, boolean passed) {
        this.description = description;
        this.weight = weight;
        this.passed = passed;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public boolean passed() {
        return passed;
    }

    @Override
    public String toString() {
        return "CodeCheckResult{" +
                "description='" + description + '\'' +
                ", weight=" + weight +
                ", passed=" + passed +
                '}';
    }
}
