package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * This step replaces the classloader with one that sees the folder
 * with the compiled classes.
 */
public class ReplaceClassloaderStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        try {
            String pathToAddToClassloader = dirCfg.getWorkingDir();
            replaceClassloader(pathToAddToClassloader, result);
        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }

    private void replaceClassloader(String pathToAddToClassloader, ResultBuilder result) {
        List<Path> additionalClasspathEntries = Arrays.asList(Paths.get(pathToAddToClassloader));
        URL[] urls = additionalClasspathEntries.stream().map(this::toURL).toArray(URL[]::new);
        ClassLoader currentClassloader = Thread.currentThread().getContextClassLoader();
        ClassLoader customClassLoader = URLClassLoader.newInstance(urls, currentClassloader);

        Thread.currentThread().setContextClassLoader(customClassLoader);
    }

    private URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        }
        catch (Exception ex) {
            throw new RuntimeException("Invalid classpath entry: " + path, ex);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ReplaceClassloaderStep;
    }
}
