package nl.tudelft.cse1110.andy.result;

public class MetaTestResult {
    private final String name;
    private final int weight;
    private final boolean succeeded;

    public MetaTestResult(String name, int weight, boolean succeeded) {
        this.name = name;
        this.weight = weight;
        this.succeeded = succeeded;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public boolean succeeded() {
        return succeeded;
    }

    @Override
    public String toString() {
        return "MetaTestResult{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", succeeded=" + succeeded +
                '}';
    }
}
