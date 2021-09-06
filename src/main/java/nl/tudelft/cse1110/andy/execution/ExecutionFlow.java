package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.step.CompilationStep;
import nl.tudelft.cse1110.andy.execution.step.GetRunConfigurationStep;
import nl.tudelft.cse1110.andy.execution.step.OrganizeSourceCodeStep;
import nl.tudelft.cse1110.andy.execution.step.ReplaceClassloaderStep;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.extractPackageName;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

public class ExecutionFlow {
    private final Context ctx;
    private final ResultBuilder result;
    private LinkedList<ExecutionStep> steps;
    private final ResultWriter writer;

    private ExecutionFlow(Context ctx, ResultBuilder result, ResultWriter writer) {
        this.steps = new LinkedList<>();
        steps.addAll(0, basicSteps());

        this.writer = writer;
        this.ctx = ctx;
        this.result = result;
        this.ctx.setFlow(this);
    }

    public void run() {
        try {
            do {
                ExecutionStep currentStep = steps.pollFirst();
                try {
                    currentStep.execute(ctx, result);
                } catch (Throwable e) {
                    result.genericFailure(currentStep, e);
                }
            } while (!steps.isEmpty() && !result.hasFailed());

            Result solutionResult = result.build();
            writer.write(solutionResult);
        } catch(Throwable t) {
            // in case something even totally unexpected happens, we log it.
            writer.uncaughtError(t);
        } finally {
            revertSourceCode();
        }
    }

    public void addSteps(List<ExecutionStep> steps) {
        this.steps.addAll(steps);
    }

    public static ExecutionFlow build(Context ctx, ResultBuilder result, ResultWriter writer) {
        return new ExecutionFlow(ctx, result, writer);
    }

    private List<ExecutionStep> basicSteps() {
        return Arrays.asList(new OrganizeSourceCodeStep(), new CompilationStep(), new ReplaceClassloaderStep(), new GetRunConfigurationStep());
    }

    private void revertSourceCode() {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        if (dirCfg.getWorkingDir().startsWith("/var")) return;

        try {
            List<String> listOfFiles = filePathsAsString(getAllJavaFiles(dirCfg.getTemporaryDir()));
            for (String pathOfJavaClass : listOfFiles) {
                String content = new String(Files.readAllBytes(Paths.get(pathOfJavaClass)));

                String packageName = extractPackageName(content);
                String directory = dirCfg.getWorkingDir() + "/src/" + (pathOfJavaClass.contains("Test") ? "test/java/" : "main/java/");
                String directoryName = concatenateDirectories(directory, packageName);

                moveFile(pathOfJavaClass, directoryName, new File(pathOfJavaClass).getName());
            }

            FileUtils.deleteDirectory(new File(dirCfg.getTemporaryDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
