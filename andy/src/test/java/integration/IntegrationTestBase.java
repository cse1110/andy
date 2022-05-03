package integration;

import com.google.common.io.Files;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.EmptyWriter;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;

import static nl.tudelft.cse1110.andy.utils.ResourceUtils.resourceFolder;

public abstract class IntegrationTestBase {

    protected final File workDir   = Files.createTempDir();
    protected final File reportDir = Files.createTempDir();
    protected Context ctx;

    @AfterEach
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(workDir);
        FileUtils.deleteDirectory(reportDir);
    }

    public Result run(Action action,
                      String libraryFile,
                      String solutionFile,
                      String configurationFile) {
        if (configurationFile != null) {
            copyConfigurationFile(configurationFile);
        }

        copyFiles(libraryFile, solutionFile);

        this.ctx = new Context(action);

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir.toString(),
                reportDir.toString()
        );

        ctx.setDirectoryConfiguration(dirCfg);

        ResultBuilder resultBuilder = new ResultBuilder(ctx, new GradeCalculator());

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            ResultWriter writer = getWriter();
            ExecutionFlow flow = ExecutionFlow.build(ctx, resultBuilder, writer);

            addSteps(flow);

            flow.run();
        } catch(Exception e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }

        return resultBuilder.build();
    }

    protected ResultWriter getWriter() {
        return new EmptyWriter();
    }

    public Result run(Action action, String libraryFile, String solutionFile) {
        return this.run(action, libraryFile, solutionFile, null);
    }

    public Result run(String libraryFile, String solutionFile, String configurationFile) {
        return this.run(Action.FULL_WITH_HINTS, libraryFile, solutionFile, configurationFile);
    }


    protected void addSteps(ExecutionFlow flow) {
    }

    protected void copyFiles(String libraryFile, String solutionFile) {
        String dirWithLibrary = resourceFolder("/grader/fixtures/Library/");
        String dirWithSolution = resourceFolder("/grader/fixtures/Solution/");
        File library = new File(dirWithLibrary +  libraryFile + ".java");
        File solution = new File(dirWithSolution + solutionFile + ".java");

        String dirToCopy = workDir.toString();

        File copiedLibrary = FilesUtils.copyFile(library.getAbsolutePath(), dirToCopy).toFile();
        File copiedSolution = FilesUtils.copyFile(solution.getAbsolutePath(), dirToCopy).toFile();

        copiedLibrary.renameTo(new File(copiedLibrary.getParentFile() + "/Library.java"));
        copiedSolution.renameTo(new File(copiedSolution.getParentFile() + "/Solution.java"));
    }

    protected void copyConfigurationFile(String configurationFile) {
        String dirWithConfiguration = resourceFolder("/grader/fixtures/Config/");

        File config = new File(dirWithConfiguration + configurationFile + ".java");
        File copied = FilesUtils.copyFile(config.getAbsolutePath(), workDir.toString()).toFile();

        copied.renameTo(new File(copied.getParentFile() + "/Configuration.java"));
    }

}
