package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.extractPackageName;
import static nl.tudelft.cse1110.andy.utils.ClassUtils.packageToDirectory;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

public class OrganizeSourceCodeStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        try {
            FileUtils.deleteDirectory(new File(dirCfg.getOutputDir()));

            List<String> listOfFiles = filePathsAsString(getAllJavaFiles(dirCfg.getWorkingDir()));
            for(String pathOfJavaClass : listOfFiles) {
                String content = new String(Files.readAllBytes(Paths.get(pathOfJavaClass)));

                String packageName = extractPackageName(content);
                String directoryName = concatenateDirectories(dirCfg.getWorkingDir(), packageToDirectory(packageName));
                dirCfg.setTemporaryDir(directoryName);

                createDirIfNeeded(directoryName);
                moveFile(pathOfJavaClass, directoryName, new File(pathOfJavaClass).getName());
            }
        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof OrganizeSourceCodeStep;
    }
}
