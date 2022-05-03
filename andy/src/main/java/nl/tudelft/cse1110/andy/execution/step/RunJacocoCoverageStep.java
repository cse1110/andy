package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.utils.FromBytesClassLoader;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.ClassUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.clazzNameAsPath;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJacocoCoverageStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {

        // Skip step if disabled
        if (ctx.getRunConfiguration().skipJacoco()) {
            return;
        }

        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        try {
            /* Get the names of the test class and the library classes.*/
            String testClass = ClassUtils.getTestClass(ctx.getNewClassNames());

            final IRuntime runtime = new LoggerRuntime();

            /*
             * We need to instrument the SUT to be able to generate a JaCoCo report.
             * We then override the non-instrumented classes with the instrumented ones using
             * a custom class loader. For the sake of simplicity we instrument all classes using a custom method.
             * We also save the old class loader to restore it later.
             */
            final Instrumenter instr = new Instrumenter(runtime);

            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            FromBytesClassLoader classLoader = new FromBytesClassLoader(currentClassLoader);

            this.instrumentAllInDirectory(instr, new File(dirCfg.getWorkingDir()), classLoader, "");

            Thread.currentThread().setContextClassLoader(classLoader);

            final RuntimeData data = new RuntimeData();
            runtime.startup(data);

            /* After instrumenting classes, we need to execute them.*/
            this.executeJUnitTests(testClass);

            /*
             * After instrumenting and running, we can continue to reporting.
             * This will generate the JaCoCo report.
             */
            final ExecutionDataStore executionData = new ExecutionDataStore();
            final SessionInfoStore sessionInfos = new SessionInfoStore();
            data.collect(executionData, sessionInfos, false);
            runtime.shutdown();

            final CoverageBuilder coverageBuilder = new CoverageBuilder();
            final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);

            /*
             * We analyze the library files that have been specified in the config.
             * These classes will have a coverage report.
             */
            for (String libraryClass : runCfg.classesUnderTest()) {
                InputStream originalLibrary = this.getClassAsInputStream(dirCfg.getWorkingDir(), libraryClass);
                analyzer.analyzeClass(originalLibrary, libraryClass);
                originalLibrary.close();
            }

            Collection<IClassCoverage> coverages = coverageBuilder.getClasses();
            result.logJacoco(coverages);

            /* Generate an HTML report.*/
            this.generateReport(dirCfg, testClass, coverageBuilder, executionData, sessionInfos);

            /* Restore the old class loader to get the non-instrumented classes back.*/
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    /**Instrument all classes in a directory.*/
    private void instrumentAllInDirectory(Instrumenter instr, File directory, FromBytesClassLoader classLoader, String start) throws IOException {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                InputStream originalLibrary = new FileInputStream(file.getAbsolutePath());
                String className = start + "." + file.getName().replace(".class", "").replace('/', '.');

                final byte[] instrumentedLibrary = instr.instrument(originalLibrary, className);
                originalLibrary.close();
                classLoader.addDefinition(className, instrumentedLibrary);
            } else if (file.isDirectory()) {
                if (!start.equals("")) {
                    instrumentAllInDirectory(instr, file, classLoader, start + "." + file.getName());
                } else {
                    instrumentAllInDirectory(instr, file, classLoader, file.getName());
                }
            }
        }
    }

    /* Get a compiled Java class as an InputStream. */
    private InputStream getClassAsInputStream(String filepath, String className) throws IOException {
        String nameOfTheClassFile = clazzNameAsPath(className);
        String pathToClass = concatenateDirectories(filepath, nameOfTheClassFile);
        return new FileInputStream(pathToClass);
    }

    /* Run the specified JUnit test file */
    private void executeJUnitTests(String testClass) {
        Launcher launcher = LauncherFactory.create();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(testClass))
                .build();
        launcher.execute(request);
    }

    /* Generate an HTML report from JaCoCo based on the coverage analysis of our classes. */
    private void generateReport(DirectoryConfiguration dirCfg, String testClass,
                                CoverageBuilder coverageBuilder, ExecutionDataStore executionData,
                                SessionInfoStore sessionInfos) throws IOException {
        final HTMLFormatter htmlFormatter = new HTMLFormatter();

        String outputJacocoDir = concatenateDirectories(dirCfg.getOutputDir(), "jacoco");
        FilesUtils.createDirIfNeeded(outputJacocoDir);

        final IReportVisitor visitor = htmlFormatter
                .createVisitor(new FileMultiReportOutput(new File(outputJacocoDir)));

        visitor.visitInfo(sessionInfos.getInfos(),
                executionData.getContents());

        visitor.visitBundle(coverageBuilder.getBundle(testClass),
                new DirectorySourceFileLocator(new File(dirCfg.getWorkingDir()), "utf-8", 4));

        visitor.visitEnd();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunJacocoCoverageStep;
    }
}
