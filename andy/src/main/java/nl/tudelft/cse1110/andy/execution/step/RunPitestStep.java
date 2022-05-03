package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.ClassUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.pitest.mutationtest.commandline.OptionsParser;
import org.pitest.mutationtest.commandline.ParseResult;
import org.pitest.mutationtest.commandline.PluginFilter;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.AnalysisResult;
import org.pitest.mutationtest.tooling.CombinedStatistics;
import org.pitest.mutationtest.tooling.EntryPoint;
import org.pitest.util.Unchecked;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RunPitestStep implements ExecutionStep {


    @Override
    public void execute(Context ctx, ResultBuilder result) {

        // Skip step if disabled
        if (ctx.getRunConfiguration().skipPitest()) {
            return;
        }

        final PluginServices plugins = PluginServices.makeForContextLoader();
        final OptionsParser parser = new OptionsParser(new PluginFilter(plugins));

        String outputPitestDir = createDirectoryForPitest(ctx);
        final ParseResult pr = parser.parse(buildArgs(ctx, outputPitestDir));

        if (!pr.isOk()) {
            result.genericFailure("PITEST: " + pr.getErrorMessage().get());
        } else {
            final ReportOptions data = pr.getOptions();

            // let's silence the sysout and java logging
            PrintStream console = silencePitest();

            // run!
            final CombinedStatistics stats = runReport(ctx, data, plugins);

            System.setOut(console);

            extractAndRemoveReportFolder(outputPitestDir);
            result.logPitest(stats);
        }
    }

    private String createDirectoryForPitest(Context ctx) {
        String outputPitestDir = FilesUtils.concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "pitest");
        FilesUtils.createDirIfNeeded(outputPitestDir);
        return outputPitestDir;
    }

    private String[] buildArgs(Context ctx, String pitestOutputDir) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        List<String> args = new ArrayList<>();

        args.add("--reportDir");
        args.add(pitestOutputDir);

        args.add("--targetClasses");
        args.add(commaSeparated(runCfg.classesUnderTest()));

        args.add("--targetTests");
        args.add(ClassUtils.getTestClass(ctx.getNewClassNames()));

        args.add("--sourceDirs");
        args.add(dirCfg.getWorkingDir());

        args.add("--verbose");
        args.add("false");

        args.add("--classPath");
        List<String> librariesToInclude = compiledClassesPlusLibraries(ctx, dirCfg);
        args.add(commaSeparated(librariesToInclude));

        args.add("--mutators");
        args.add(commaSeparated(runCfg.listOfMutants()));

        return args.stream().toArray(String[]::new);
    }

    private List<String> compiledClassesPlusLibraries(Context ctx, DirectoryConfiguration dirCfg) {
        List<String> toAddToClassPath = new ArrayList<>();
        toAddToClassPath.add(dirCfg.getWorkingDir());

        if(ctx.hasLibrariesToBeIncluded()) {
            toAddToClassPath.addAll(ctx.getLibrariesToBeIncluded());
        }

        return toAddToClassPath;
    }

    private String commaSeparated(List<String> list) {
        return list.stream().collect(Collectors.joining(","));
    }

    private static CombinedStatistics runReport(Context ctx, ReportOptions data,
                                                PluginServices plugins) {

        final EntryPoint e = new EntryPoint();
        File baseDir = new File(ctx.getDirectoryConfiguration().getWorkingDir());
        final AnalysisResult result = e.execute(baseDir, data, plugins, new HashMap<>());
        if (result.getError().isPresent()) {
            throw Unchecked.translateCheckedException(result.getError().get());
        }
        return result.getStatistics().get();

    }

    private void extractAndRemoveReportFolder(String outputPitestDir) {
        /*
         * Pitest creates a subdir with the timestamp, which we want to remove.
         * The report folder "year-month-day-time" will be the only file in .../output/pitest,
         * as every WebLab submission gets a "clean" image.
         */
        try {
            File[] contentsOfPitestOutputDir = FilesUtils.getAllFiles(new File(outputPitestDir));

            if (contentsOfPitestOutputDir.length != 0) {
                File reportFolderToSkip = contentsOfPitestOutputDir[0];

                FileUtils.copyDirectory(reportFolderToSkip, new File(outputPitestDir),true);
                FilesUtils.deleteDirectory(reportFolderToSkip);
            } else {
                throw new RuntimeException("PiTest report was not written to .../output/pitest!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private PrintStream silencePitest() {
        PrintStream console = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Logger pit = Logger.getLogger("PIT");
        Arrays.stream(pit.getHandlers()).forEach(h -> pit.removeHandler(h));
        return console;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunPitestStep;
    }
}
