package nl.tudelft.cse1110.andy.writer.standard;

public class VersionInformation {
    private String version;
    private String buildTimestamp;
    private String commitId;

    public VersionInformation(String version, String buildTimestamp, String commitId) {
        this.version = version;
        this.buildTimestamp = buildTimestamp;
        this.commitId = commitId;
    }

    public String getVersion() {
        return version;
    }

    public String getBuildTimestamp() {
        return buildTimestamp;
    }

    public String getCommitId() {
        return commitId;
    }
}
