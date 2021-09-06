package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.extractPackageName;
import static nl.tudelft.cse1110.andy.utils.ClassUtils.packageToDirectory;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

public class OrganizeSourceCodeStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        try {
            Arrays.stream(new File(dirCfg.getOutputDir()).listFiles((f, p) -> !p.endsWith("java"))).forEach(File::delete);

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
