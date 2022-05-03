package nl.tudelft.cse1110.andy.config;

public class DirectoryConfiguration {

    private final String workingDir;
    private final String outputDir;

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


}
