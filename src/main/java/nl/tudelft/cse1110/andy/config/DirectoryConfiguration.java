package nl.tudelft.cse1110.andy.config;

public class DirectoryConfiguration {

    private final String workingDir;
    private final String outputDir;
    private String temporaryDir;

    public DirectoryConfiguration(String workingDir, String reportsDir) {
        this.workingDir = workingDir;
        this.outputDir = reportsDir;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getTemporaryDir() { return temporaryDir; }

    public void setTemporaryDir(String temporaryDir) { this.temporaryDir = temporaryDir; }

}
