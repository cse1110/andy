package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.apache.commons.cli.*;

import java.io.File;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;

public class Andy {

    private static final CommandLineParser parser = new DefaultParser();
    private static final Options options = new Options();

    public static void main(String[] args) throws ParseException {
        Context ctx = (args.length > 0)
                ? buildContextWithCommandLineArguments(args)
                : buildContextWithEnvironmentVariables();

        ResultBuilder result = new ResultBuilder(ctx, new GradeCalculator());
        ResultWriter writer = new WebLabResultWriter(ctx, new RandomAsciiArtGenerator());
        ExecutionFlow flow = ExecutionFlow.build(ctx, result, writer);

        flow.run();

        System.out.println(readFile(new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "stdout.txt"))));
    }

    private static void addCommandLineArguments() {
        options.addOption("e", "exercise", true, "Exercise name in the assignments repo.");
        options.addOption("d", "directory", true, "Base directory of the exercise");
        options.addOption("o", "output", true, "Output directory of the exercise");
        options.addOption("f", "full-with-hints", false, "Run Andy completely");
        options.addOption("F", "full-without-hints", false, "Run Andy without hints");
        options.addOption("c", "coverage", false, "Run Andy with only coverage");
        options.addOption("t", "tests", false, "Run Andy only with tests");
        options.addOption("T", "meta-tests", false, "Run Andy only with meta tests");
    }

    private static Context buildContextWithCommandLineArguments(String[] args) throws ParseException {
        addCommandLineArguments();

        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption('e')) return buildContext(cmd);
        if (!cmd.hasOption('d')) { System.out.println("Error: no base directory supplied, please use the flag -d.");   System.exit(-1); }
        if (!cmd.hasOption('o')) { System.out.println("Error: no output directory supplied, please use the flag -o."); System.exit(-1); }

        return buildContext(cmd);
    }

    private static Context buildContextWithEnvironmentVariables() {
        if (System.getenv("ACTION")      == null) { System.out.println("No ACTION environment variable.");      System.exit(-1); }
        if (System.getenv("WORKING_DIR") == null) { System.out.println("No WORKING_DIR environment variable."); System.exit(-1); }
        if (System.getenv("OUTPUT_DIR")  == null) { System.out.println("No OUTPUT_DIR environment variable.");  System.exit(-1); }

        return buildContext(null);
    }

    private static Context buildContext(CommandLine cmd) {
        Action action = (cmd == null) ? Action.valueOf(System.getenv("ACTION"))
                : cmd.hasOption('f') ? Action.FULL_WITH_HINTS
                : cmd.hasOption('F') ? Action.FULL_WITHOUT_HINTS
                : cmd.hasOption('c') ? Action.COVERAGE
                : cmd.hasOption('T') ? Action.META_TEST
                : Action.TESTS;
        Context ctx = new Context(action);

        DirectoryConfiguration dirCfg;
        if (cmd == null) {
            dirCfg = new DirectoryConfiguration(System.getenv("WORKING_DIR"), System.getenv("OUTPUT_DIR"));
        } else {
            dirCfg = (cmd.hasOption('e'))
                   ? new DirectoryConfiguration(System.getProperty("user.dir") + "/assignments/" + cmd.getOptionValue('e'), System.getProperty("user.dir") + "/output/" + cmd.getOptionValue('e'))
                   : new DirectoryConfiguration(cmd.getOptionValue('d'), cmd.getOptionValue('o'));
        }

        ctx.setDirectoryConfiguration(dirCfg);

        return ctx;
    }

}
