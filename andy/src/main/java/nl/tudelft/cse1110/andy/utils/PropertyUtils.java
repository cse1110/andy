package nl.tudelft.cse1110.andy.utils;

import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {
    public static VersionInformation getVersionInformation() {
        Properties versionProperties = new Properties();
        VersionInformation versionInformation;
        try {
            versionProperties.load(PropertyUtils.class.getClassLoader().getResourceAsStream("version.properties"));
            versionInformation = new VersionInformation(
                    versionProperties.getProperty("project.version"),
                    versionProperties.getProperty("git.build.time"),
                    versionProperties.getProperty("git.commit.id")
            );
        } catch (IOException e) {
            System.out.println("Could not load Andy version information");
            e.printStackTrace();
            versionInformation = new VersionInformation(
                    "INVALID_VERSION",
                    "INVALID_BUILD_TIMESTAMP",
                    "INVALID_COMMIT_ID");
        }

        return versionInformation;
    }
}
